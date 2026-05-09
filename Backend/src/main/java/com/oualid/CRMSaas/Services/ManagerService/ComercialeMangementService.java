package com.oualid.CRMSaas.Services.ManagerService;


import com.oualid.CRMSaas.DTO.ComercialeRequestDTO;
import com.oualid.CRMSaas.DTO.UserResponseDTO;
import com.oualid.CRMSaas.Entity.AuditLogger;
import com.oualid.CRMSaas.Entity.Enum.RoleUser;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Exception.UserNotFoundException;
import com.oualid.CRMSaas.Repository.UserRepository;
import com.oualid.CRMSaas.Tools.LoggerObserver.EventManager;
import com.oualid.CRMSaas.Tools.PasswordGenerate_Builder.PasswordBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ComercialeMangementService {
    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder;
    private final EventManager eventmanager ;

    public ComercialeMangementService(UserRepository userrepo
            , PasswordEncoder passwordencoder   , EventManager eventManager
                                      ){
        this.userRepository = userrepo ;
        this.passwordEncoder = passwordencoder ;
        this.eventmanager = eventManager ;
    }


    // create new comerciale  :
    public User CreatedNewComerciale(ComercialeRequestDTO datacomerciale , String email ){

        try{
            User manager = this.userRepository.findByEmail(email);
            // generate password :
            String generatePassword = new PasswordBuilder()
                    .length(6)
                    .withNumbers(true)
                    .withUppercase(true)
                    .build();

            System.out.println("password generate : "+ generatePassword);
            User new_comerciale = User.builder()
                    .firstName(datacomerciale.firstName())
                    .lastName(datacomerciale.lastName())
                    .email(datacomerciale.email())
                    .company(manager.getCompany())
                    .isActive(true)
                    .role(RoleUser.COMMERCIAL)
                    .password(this.passwordEncoder.encode(generatePassword))
                    .build();

            User user_created = this.userRepository.save(new_comerciale);
            return  user_created;

        }catch(Exception e){
            // debug
            System.out.println("[EXCEPTION] : "+e.getMessage()) ;
            throw  new RuntimeException("probelmme dans le service ");
        }
    }


    public ResponseEntity<String> BloquerCompt(UUID id_comerciale){
        try{
            User comerciale_bloquer = this.userRepository.findById(id_comerciale).orElseThrow(()->new UserNotFoundException("user not found"));
            comerciale_bloquer.setIsActive(false);
            this.userRepository.save(comerciale_bloquer) ;
            // creation d'une audit logger
            AuditLogger NewAuditlogger =  AuditLogger.builder()
                    .type("BOQUER COMPT COMERCIALE")
                    .log("MANAGER BLOQUER COMPT USER ")
                    .build();
            this.eventmanager.publish(NewAuditlogger);
            return ResponseEntity.status(HttpStatus.CREATED).body("compt est bloque");

        }catch(Exception e){
            // debug
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("problemme dans le service ") ;

        }
    }

    public ResponseEntity<String > DebloquerComerciale(UUID id_comerciale){
        try{
            User comerciale = this.userRepository.findById(id_comerciale).orElseThrow(()-> new UserNotFoundException("user not found")) ;
            comerciale.setIsActive(true);
            this.userRepository.save(comerciale);
            AuditLogger NewAuditlogger =  AuditLogger.builder()
                    .type("DEBOQUER COMPT COMERCIALE")
                    .log("MANAGER DEBLOQUER COMPT USER ")
                    .build();
            this.eventmanager.publish(NewAuditlogger);


            return  ResponseEntity.status(HttpStatus.CREATED).body("acount est debloquer");
        }catch(Exception e){
            // debug
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("problemme dans le service ") ;
        }
    }

    // show comerciale info :
    public UserResponseDTO showComerciale(UUID id_comerciale ){
        try{
            User comercialeSHow = this.userRepository.findById(id_comerciale).orElseThrow(()->new UserNotFoundException("user not found ")) ;
            return new UserResponseDTO(
                    comercialeSHow.getFirstName() ,
                    comercialeSHow.getLastName() ,
                    comercialeSHow.getEmail() ,
                    comercialeSHow.getIsActive() ,
                    comercialeSHow.getRole()
            );

        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("problemme dans le service ") ;
        }

    }

    // retourne user ca depend comapny :

    public List<UserResponseDTO> getALlcommerciauCompany(String username_manager){
        try{
            // recuperation Users :
            User manager = this.userRepository.findByEmail(username_manager);

            List<User> dataUsersResponse = this.userRepository.findByCompany(manager.getCompany());
            // Mapping
            List<UserResponseDTO> dataDtoResponseMapping = new ArrayList<>();
            for(User user : dataUsersResponse){
                UserResponseDTO userresponsedto = new UserResponseDTO(
                       user.getFirstName() ,
                       user.getLastName() ,
                       user.getEmail() ,
                       user.getIsActive() ,
                       user.getRole()
                );
                dataDtoResponseMapping.add(userresponsedto);
            }
            return dataDtoResponseMapping ;

        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("problemme dans le service ") ;
        }

    }


}
