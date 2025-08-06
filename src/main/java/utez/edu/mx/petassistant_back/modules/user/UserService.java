package utez.edu.mx.petassistant_back.modules.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.petassistant_back.modules.user.dto.UserRequestDTO;
import utez.edu.mx.petassistant_back.modules.user.dto.UserResponseDTO;
import utez.edu.mx.petassistant_back.utils.APIResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public APIResponse findAll() {
        try {
            List<UserResponseDTO> users = userRepository.findAll()
                    .stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            return new APIResponse("Users retrieved successfully", users, false, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error retrieving users", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public APIResponse findById(Long id) {
        try {
            Optional<BeanUser> userOpt = userRepository.findById(id);
            if (userOpt.isEmpty()) {
                return new APIResponse("User not found", true, HttpStatus.NOT_FOUND);
            }
            return new APIResponse("User retrieved successfully", convertToResponse(userOpt.get()), false, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error retrieving user", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse register(UserRequestDTO dto) {
        try {
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                return new APIResponse("Email already exists", true, HttpStatus.BAD_REQUEST);
            }
            BeanUser user = new BeanUser();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword()); // Aquí podrías aplicar encriptación si deseas
            userRepository.save(user);
            return new APIResponse("User created successfully", convertToResponse(user), false, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error creating user", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse delete(Long id) {
        try {
            if (!userRepository.existsById(id)) {
                return new APIResponse("User not found", true, HttpStatus.NOT_FOUND);
            }
            userRepository.deleteById(id);
            return new APIResponse("User deleted successfully", false, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error deleting user", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public APIResponse login(String email, String password) {
        try {
            Optional<BeanUser> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
                return new APIResponse("Invalid credentials", true, HttpStatus.UNAUTHORIZED);
            }
            return new APIResponse("Login successful", convertToResponse(userOpt.get()), false, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error during login", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse updateUser(Long id, UserRequestDTO dto) {
        try {
            Optional<BeanUser> existingUser = userRepository.findById(id);
            if (existingUser.isEmpty()) {
                return new APIResponse("User not found", true, HttpStatus.NOT_FOUND);
            }

            BeanUser user = existingUser.get();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            userRepository.save(user);

            return new APIResponse("User updated successfully", user, false, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error updating user", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private UserResponseDTO convertToResponse(BeanUser user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
