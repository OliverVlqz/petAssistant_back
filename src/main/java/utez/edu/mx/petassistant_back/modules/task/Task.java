package utez.edu.mx.petassistant_back.modules.task;

import jakarta.persistence.*;
import utez.edu.mx.petassistant_back.modules.pet.Pet;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String status; // e.g., "Pending", "Completed"

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    public Task() {
    }

    public Task(Long id, String title, String description, String status, Pet pet) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.pet = pet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
