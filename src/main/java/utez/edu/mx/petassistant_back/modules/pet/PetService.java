package utez.edu.mx.petassistant_back.modules.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.petassistant_back.modules.pet.dto.PetRequestDTO;
import utez.edu.mx.petassistant_back.modules.pet.dto.PetResponseDTO;
import utez.edu.mx.petassistant_back.modules.user.BeanUser;
import utez.edu.mx.petassistant_back.modules.user.UserRepository;
import utez.edu.mx.petassistant_back.utils.APIResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public APIResponse findAll() {
        try {
            List<PetResponseDTO> pets = petRepository.findAll()
                    .stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            return new APIResponse("Pets retrieved successfully", pets, false, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error retrieving pets", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public APIResponse findById(Long id) {
        try {
            Optional<Pet> petOpt = petRepository.findById(id);
            if (petOpt.isEmpty()) {
                return new APIResponse("Pet not found", true, HttpStatus.NOT_FOUND);
            }
            return new APIResponse("Pet retrieved successfully", convertToResponse(petOpt.get()), false, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error retrieving pet", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse create(PetRequestDTO dto, Long userId) {
        try {
            Optional<BeanUser> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return new APIResponse("User not found", true, HttpStatus.NOT_FOUND);
            }
            Pet pet = new Pet();
            pet.setName(dto.getName());
            pet.setDescription(dto.getDescription());
            pet.setImage(dto.getImage());
            pet.setUser(userOpt.get());

            petRepository.save(pet);
            return new APIResponse("Pet created successfully", convertToResponse(pet), false, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error creating pet", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse delete(Long id) {
        try {
            if (!petRepository.existsById(id)) {
                return new APIResponse("Pet not found", true, HttpStatus.NOT_FOUND);
            }
            petRepository.deleteById(id);
            return new APIResponse("Pet deleted successfully", false, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error deleting pet", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private PetResponseDTO convertToResponse(Pet pet) {
        PetResponseDTO dto = new PetResponseDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setDescription(pet.getDescription());
        dto.setImage(pet.getImage());
        dto.setUserId(pet.getUser().getId());
        return dto;
    }
}
