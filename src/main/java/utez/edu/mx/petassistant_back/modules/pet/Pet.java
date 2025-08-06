package utez.edu.mx.petassistant_back.modules.pet;

import jakarta.persistence.*;
import utez.edu.mx.petassistant_back.modules.task.Task;
import utez.edu.mx.petassistant_back.modules.user.BeanUser;

import java.util.List;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String image; // Will store the file path or image name

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private BeanUser user;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    public Pet() {
    }

    public Pet(Long id, String name, String description, String image, BeanUser user, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.user = user;
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BeanUser getUser() {
        return user;
    }

    public void setUser(BeanUser user) {
        this.user = user;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
