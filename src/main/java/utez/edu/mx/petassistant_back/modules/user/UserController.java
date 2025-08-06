package utez.edu.mx.petassistant_back.modules.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.petassistant_back.modules.user.dto.UserRequestDTO;
import utez.edu.mx.petassistant_back.utils.APIResponse;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<APIResponse> getAllUsers() {
        APIResponse response = userService.findAll();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getUserById(@PathVariable Long id) {
        APIResponse response = userService.findById(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping
    public ResponseEntity<APIResponse> createUser(@RequestBody UserRequestDTO dto) {
        APIResponse response = userService.register(dto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable Long id) {
        APIResponse response = userService.delete(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        APIResponse response = userService.updateUser(id, dto);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
