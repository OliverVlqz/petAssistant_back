package utez.edu.mx.petassistant_back.modules.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.petassistant_back.modules.task.dto.TaskRequestDTO;
import utez.edu.mx.petassistant_back.utils.APIResponse;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<APIResponse> getAllTasks() {
        APIResponse response = taskService.findAll();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getTaskById(@PathVariable Long id) {
        APIResponse response = taskService.findById(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping
    public ResponseEntity<APIResponse> createTask(@RequestBody TaskRequestDTO dto) {
        APIResponse response = taskService.create(dto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteTask(@PathVariable Long id) {
        APIResponse response = taskService.delete(id);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
