package com.oualid.CRMSaas.Web.Controllers.ManagerControllers;

import com.oualid.CRMSaas.DTO.DashboardStatsResponse;
import com.oualid.CRMSaas.DTO.EvolutionPoint;
import com.oualid.CRMSaas.Entity.*;
import com.oualid.CRMSaas.Repository.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manager")
public class ManagerDashboardController {

    private final LeadRepository leadRepository;
    private final OpportunityRepository opportunityRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public ManagerDashboardController(LeadRepository leadRepository,
                                      OpportunityRepository opportunityRepository,
                                      CustomerRepository customerRepository,
                                      UserRepository userRepository) {
        this.leadRepository = leadRepository;
        this.opportunityRepository = opportunityRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard/stats")
    public DashboardStatsResponse getDashboardStats(@AuthenticationPrincipal UserDetails userDetails) {
        User manager = userRepository.findByEmail(userDetails.getUsername());
        Company company = manager.getCompany();
        List<User> companyUsers = userRepository.findByCompany(company);

        List<Lead> allLeads = leadRepository.findByCompany(company);
        List<Opportunity> allOpportunities = opportunityRepository.findByAssignedToIn(companyUsers);
        List<Customer> allCustomers = customerRepository.findAllByCreatedByIn(companyUsers);

        long totalLeads = allLeads.size();
        long totalOpportunities = allOpportunities.size();
        long totalCustomers = allCustomers.size();

        Map<String, Long> leadsByStatus = allLeads.stream()
            .collect(Collectors.groupingBy(l -> l.getStatus().name(), Collectors.counting()));

        Map<String, Long> opportunitiesByStage = allOpportunities.stream()
            .collect(Collectors.groupingBy(o -> o.getStage().name(), Collectors.counting()));

        Map<String, Long> customerMonths = allCustomers.stream()
            .filter(c -> c.getCreatedAt() != null)
            .collect(Collectors.groupingBy(
                c -> c.getCreatedAt().getYear() + "-" + String.format("%02d", c.getCreatedAt().getMonthValue()),
                Collectors.counting()
            ));

        Map<String, Long> opportunityMonths = allOpportunities.stream()
            .filter(o -> o.getCreatedAt() != null)
            .collect(Collectors.groupingBy(
                o -> o.getCreatedAt().getYear() + "-" + String.format("%02d", o.getCreatedAt().getMonthValue()),
                Collectors.counting()
            ));

        Set<String> allKeys = new TreeSet<>(customerMonths.keySet());
        allKeys.addAll(opportunityMonths.keySet());

        List<EvolutionPoint> evolution = new ArrayList<>();
        for (String key : allKeys) {
            String[] parts = key.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            long custCount = customerMonths.getOrDefault(key, 0L);
            long oppCount = opportunityMonths.getOrDefault(key, 0L);
            evolution.add(new EvolutionPoint(year, month, custCount, oppCount));
        }

        return new DashboardStatsResponse(totalLeads, totalOpportunities, totalCustomers, leadsByStatus, opportunitiesByStage, evolution);
    }
}
