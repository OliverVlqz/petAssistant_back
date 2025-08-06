package utez.edu.mx.petassistant_back.modules.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.petassistant_back.modules.pet.dto.PetRequestDTO;
import utez.edu.mx.petassistant_back.utils.APIResponse;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<APIResponse> getAllPets() {
        APIResponse response = petService.findAll();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getPetById(@PathVariable Long id) {
        APIResponse response = petService.findById(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping
    public ResponseEntity<APIResponse> createPet(@RequestBody PetRequestDTO dto,
                                                 @RequestHeader("userId") String userId) {
        System.out.println("UserId header: " + userId);
        Long userIdLong = Long.parseLong(userId);
        APIResponse response = petService.create(dto, userIdLong);
        return new ResponseEntity<>(response, response.getStatus());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deletePet(@PathVariable Long id) {
        APIResponse response = petService.delete(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updatePet(@PathVariable Long id,
                                                 @RequestBody PetRequestDTO dto) {
        APIResponse response = petService.updatePet(id, dto);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
