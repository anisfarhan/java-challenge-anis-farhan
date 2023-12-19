package ist.challenge.anis_farhan.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", length = 25, unique = true)
    private String username;

    @Column(name = "password", length = 25)
    private String password;
}
