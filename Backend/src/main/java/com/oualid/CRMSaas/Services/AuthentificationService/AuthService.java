package com.oualid.CRMSaas.Services.AuthentificationService;


import com.oualid.CRMSaas.DTO.ChekAuthResponse;
import com.oualid.CRMSaas.DTO.LoginDTO;
import com.oualid.CRMSaas.DTO.RegisterDTO;
import com.oualid.CRMSaas.Entity.Enum.RoleUser;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Exception.AccountInactiveException;
import com.oualid.CRMSaas.Exception.EmailAlreadyExistsException;
import com.oualid.CRMSaas.Exception.InvalidCredentialsException;
import com.oualid.CRMSaas.Exception.UserNotFoundException;
import com.oualid.CRMSaas.Repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthService(UserRepository userRepo,
                       JWTService jwtService,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder) {

        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    //  REGISTER
    public String register(RegisterDTO dto) {

        //  email déjà utilisé
        if (userRepo.findByEmail(dto.Email()) != null) {
            throw new EmailAlreadyExistsException("Email déjà utilisé");
        }

        User user = User.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.Email())
                .password(passwordEncoder.encode(dto.password()))
                .role(RoleUser.MANAGER)
                .isActive(true)
                .build();

        userRepo.save(user);

        return jwtService.generateJwtToken(user);
    }

    // LOGIN
    public String login(LoginDTO dto) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.email() ,
                            dto.password()
                    )
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Email ou mot de passe incorrect");
        }

        User user = userRepo.findByEmail(dto.email());

        //  user introuvable
        if (user == null) {
            throw new UserNotFoundException("Utilisateur introuvable");
        }
        if(user.getIsActive() == false){
            throw  new AccountInactiveException("compt est non active");
        }

        return jwtService.generateJwtToken(user);
    }

    public ChekAuthResponse chekAuthentification(String username){
        try{
            User user = this.userRepo.findByEmail(username) ;
            if(user == null){
                throw  new UserNotFoundException("user not found ") ;
            }
            // mapping
            return  new ChekAuthResponse(
                    user.getId() ,
                    user.getFirstName()  ,
                    user.getLastName() ,
                    user.getEmail() ,
                    user.getIsActive() ,
                    user.getRole()
            );
        }catch(Exception ex){
            System.out.println("Problemme dans le service "+ ex.getMessage());
            throw new RuntimeException("problemme dans le service ");
        }
    }
}