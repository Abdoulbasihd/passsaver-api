package cm.abdev.passaver.controlers;

import cm.abdev.passaver.models.auth.ERole;
import cm.abdev.passaver.models.auth.Role;
import cm.abdev.passaver.models.auth.User;
import cm.abdev.passaver.payloads.clientside.requests.user.SignInRequestDTO;
import cm.abdev.passaver.payloads.clientside.requests.user.SignUpRequestDTO;
import cm.abdev.passaver.payloads.clientside.responses.BaseResponse;
import cm.abdev.passaver.payloads.clientside.responses.user.SignInData;
import cm.abdev.passaver.repostories.auth.RoleRepository;
import cm.abdev.passaver.repostories.auth.UserRepository;
import cm.abdev.passaver.security.jwt.JwtUtils;
import cm.abdev.passaver.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignInRequestDTO signInRequest)  {


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        SignInData signInData = new SignInData();
        signInData.setUsername(userDetails.getUsername());
        signInData.setEmail(userDetails.getEmail());
        signInData.setRoles(roles);
        signInData.setId(userDetails.getId());
        signInData.setToken(jwt);

        BaseResponse<SignInData> signInDataBaseResponse = new BaseResponse<>();
        signInDataBaseResponse.setData(signInData);
        signInDataBaseResponse.setStatus("SUCCESS");
        signInDataBaseResponse.setCode(200);
        signInDataBaseResponse.setMessage("Authenticated");

        return ResponseEntity.ok(signInDataBaseResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequestDTO signUpRequest) {
        BaseResponse<Void> signUpResponse = new BaseResponse<>();
        signUpResponse.setCode(400);
        signUpResponse.setStatus("FAILED");

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {

            signUpResponse.setMessage("Username already taken");
            return ResponseEntity
                    .badRequest()
                    .body(signUpResponse);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            signUpResponse.setMessage("Email is already in use!");

            return ResponseEntity
                    .badRequest()
                    .body(signUpResponse);
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        BaseResponse<User> userSignUpResponse = new BaseResponse<>();
        userSignUpResponse.setCode(200);
        userSignUpResponse.setStatus("SUCCESS");
        userSignUpResponse.setData(user);

        return ResponseEntity.ok(userSignUpResponse);
    }
}
