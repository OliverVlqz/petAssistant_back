package utez.edu.mx.petassistant_back.modules.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.petassistant_back.modules.pet.Pet;
import utez.edu.mx.petassistant_back.modules.pet.PetRepository;
import utez.edu.mx.petassistant_back.modules.task.dto.TaskRequestDTO;
import utez.edu.mx.petassistant_back.modules.task.dto.TaskResponseDTO;
import utez.edu.mx.petassistant_back.utils.APIResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PetRepository petRepository;

    @Transactional(readOnly = true)
    public APIResponse findAll() {
        try {
            List<TaskResponseDTO> tasks = taskRepository.findAll()
                    .stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            return new APIResponse("Tasks retrieved successfully", tasks, false, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error retrieving tasks", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public APIResponse findById(Long id) {
        try {
            Optional<Task> taskOpt = taskRepository.findById(id);
            if (taskOpt.isEmpty()) {
                return new APIResponse("Task not found", true, HttpStatus.NOT_FOUND);
            }
            return new APIResponse("Task retrieved successfully", convertToResponse(taskOpt.get()), false, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error retrieving task", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse create(TaskRequestDTO dto) {
        try {
            Optional<Pet> petOpt = petRepository.findById(dto.getPetId());
            if (petOpt.isEmpty()) {
                return new APIResponse("Pet not found", true, HttpStatus.NOT_FOUND);
            }
            Task task = new Task();
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setStatus(dto.getStatus());
            task.setPet(petOpt.get());

            taskRepository.save(task);
            return new APIResponse("Task created successfully", convertToResponse(task), false, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error creating task", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse delete(Long id) {
        try {
            if (!taskRepository.existsById(id)) {
                return new APIResponse("Task not found", true, HttpStatus.NOT_FOUND);
            }
            taskRepository.deleteById(id);
            return new APIResponse("Task deleted successfully", false, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error deleting task", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse updateTask(Long id, TaskRequestDTO dto) {
        try {
            Optional<Task> existingTask = taskRepository.findById(id);
            if (existingTask.isEmpty()) {
                return new APIResponse("Task not found", true, HttpStatus.NOT_FOUND);
            }

            Task task = existingTask.get();
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setStatus(dto.getStatus());
            taskRepository.save(task);

            return new APIResponse("Task updated successfully", convertToResponse(task), false, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Error updating task", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private TaskResponseDTO convertToResponse(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPetId(task.getPet().getId());
        return dto;
    }
}