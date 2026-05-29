package com.oualid.CRMSaas.Services.ComercialeService;

import com.oualid.CRMSaas.Entity.Customer;
import com.oualid.CRMSaas.Entity.Publicite;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Entity.Enum.RoleUser;
import com.oualid.CRMSaas.Repository.CustomerRepository;
import com.oualid.CRMSaas.Repository.PubliciteRepository;
import com.oualid.CRMSaas.Repository.UserRepository;
import com.oualid.CRMSaas.Services.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PubliciteService {

    private final PubliciteRepository publiciteRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;

    public PubliciteService(PubliciteRepository publiciteRepository,
                            UserRepository userRepository,
                            CustomerRepository customerRepository,
                            EmailService emailService) {
        this.publiciteRepository = publiciteRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.emailService = emailService;
    }

    private User getCommerciale(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) throw new EntityNotFoundException("Utilisateur introuvable");
        if (user.getRole() != RoleUser.COMMERCIAL) throw new SecurityException("Seul un commercial peut gérer les publications");
        return user;
    }

    public Publicite create(String username, String titre, String contenue, MultipartFile image) {
        User commerciale = getCommerciale(username);
        Publicite pub = new Publicite();
        pub.setTitre(titre);
        pub.setContenue(contenue);
        pub.setAssignedToo(commerciale);
        if (image != null && !image.isEmpty()) {
            pub.setImageUrl(saveImage(image));
        }
        return publiciteRepository.save(pub);
    }

    public Page<Publicite> getAll(String username, int page, int size) {
        User commerciale = getCommerciale(username);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return publiciteRepository.findAllByAssignedTooOrderByCreatedAtDesc(commerciale, pageable);
    }

    public Publicite getOne(Long id) {
        return publiciteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publication introuvable"));
    }

    public Publicite update(String username, Long id, String titre, String contenue, MultipartFile image) {
        User commerciale = getCommerciale(username);
        Publicite pub = getOne(id);
        if (!pub.getAssignedToo().getId().equals(commerciale.getId())) {
            throw new SecurityException("Vous ne pouvez modifier que vos propres publications");
        }
        if (titre != null) pub.setTitre(titre);
        if (contenue != null) pub.setContenue(contenue);
        if (image != null && !image.isEmpty()) {
            if (pub.getImageUrl() != null) deleteOldImage(pub.getImageUrl());
            pub.setImageUrl(saveImage(image));
        }
        return publiciteRepository.save(pub);
    }

    public void delete(String username, Long id) {
        User commerciale = getCommerciale(username);
        Publicite pub = getOne(id);
        if (!pub.getAssignedToo().getId().equals(commerciale.getId())) {
            throw new SecurityException("Vous ne pouvez supprimer que vos propres publications");
        }
        if (pub.getImageUrl() != null) deleteOldImage(pub.getImageUrl());
        publiciteRepository.delete(pub);
    }

    public void sendToClients(String username, Long id, List<UUID> clientIds, boolean sendAll) {
        User commerciale = getCommerciale(username);
        Publicite pub = getOne(id);
        if (!pub.getAssignedToo().getId().equals(commerciale.getId())) {
            throw new SecurityException("Vous ne pouvez envoyer que vos propres publications");
        }

        List<Customer> clients;
        if (sendAll) {
            clients = customerRepository.findAllByCreatedBy(commerciale);
        } else {
            Iterable<Customer> iterable = customerRepository.findAllById(clientIds);
            List<Customer> list = new java.util.ArrayList<>();
            iterable.forEach(list::add);
            clients = list;
        }

        String subject = "Nouvelle offre - " + pub.getTitre();
        String body = buildEmailBody(pub);

        for (Customer client : clients) {
            if (client.getEmail() != null && !client.getEmail().isBlank()) {
                try {
                    emailService.sendEmail(client.getEmail(), subject, body);
                    log.info("Email envoyé à {}", client.getEmail());
                } catch (Exception e) {
                    log.error("Échec envoi email à {}: {}", client.getEmail(), e.getMessage());
                }
            }
        }
    }

    private String buildEmailBody(Publicite pub) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;'>");
        sb.append("<h2 style='color: #0f2b6b;'>").append(pub.getTitre()).append("</h2>");
        if (pub.getImageUrl() != null) {
            sb.append("<img src='http://127.0.0.1:8081/").append(pub.getImageUrl())
                    .append("' style='max-width: 100%; border-radius: 8px; margin: 16px 0;' />");
        }
        sb.append("<p style='font-size: 15px; line-height: 1.6; color: #333;'>")
                .append(pub.getContenue()).append("</p>");
        sb.append("<hr style='border: none; border-top: 1px solid #e5e7eb; margin: 24px 0;' />");
        sb.append("<p style='font-size: 12px; color: #9ca3af;'>SmartCRM - Votre partenaire de confiance</p>");
        sb.append("</div>");
        return sb.toString();
    }

    private String saveImage(MultipartFile file) {
        try {
            String uploadDir = "uploads/publicite/";
            Path dirPath = Paths.get(uploadDir);
            if (!Files.exists(dirPath)) Files.createDirectories(dirPath);

            String originalName = file.getOriginalFilename();
            String ext = "";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + ext;
            Path filePath = dirPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            return "uploads/publicite/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'image: " + e.getMessage());
        }
    }

    private void deleteOldImage(String imageUrl) {
        try {
            Path path = Paths.get(imageUrl);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.warn("Impossible de supprimer l'ancienne image: {}", imageUrl);
        }
    }
}
