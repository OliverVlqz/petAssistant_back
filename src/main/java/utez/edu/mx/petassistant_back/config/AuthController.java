package utez.edu.mx.petassistant_back.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.petassistant_back.config.dto.LoginRequestDTO;
import utez.edu.mx.petassistant_back.modules.user.UserService;
import utez.edu.mx.petassistant_back.utils.APIResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<APIResponse> login(@RequestBody LoginRequestDTO loginDTO) {
        APIResponse response = userService.login(loginDTO.getEmail(), loginDTO.getPassword());
        return new ResponseEntity<>(response, response.getStatus());
    }
}
