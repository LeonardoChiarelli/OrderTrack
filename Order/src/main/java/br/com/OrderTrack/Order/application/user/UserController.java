package br.com.OrderTrack.Order.application.user;

import br.com.OrderTrack.Order.application.user.dto.SignInDTO;
import br.com.OrderTrack.Order.application.user.dto.SignUpDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderTrack")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/signUp")
    @Transactional
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpDTO dto) {
        service.signUp(dto);

        return ResponseEntity.ok("UserEntity signed up successfully");
    }

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInDTO dto){
        try{
            return ResponseEntity.ok(service.singIn(dto));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }
}
