package ist.challenge.anis_farhan.controller;

import ist.challenge.anis_farhan.model.User;
import ist.challenge.anis_farhan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Username sudah terpakai", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        try {
            if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
                return new ResponseEntity<>("Username dan / atau password kosong", HttpStatus.BAD_REQUEST);
            }

            User userLogin = userRepository.findByUsername(user.getUsername());
            if (userLogin.getPassword().equals(user.getPassword())) {
                return new ResponseEntity<>("Sukses Login",HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/listusers")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@RequestBody User newUser, @PathVariable long id) {
        User oldUser = userRepository.findById(id).
                orElseThrow(() -> new RuntimeException("User not found for this id: " + id));

        try {
            oldUser.setUsername(newUser.getUsername());
            userRepository.save(oldUser);

            if (newUser.getPassword().equals(oldUser.getPassword())) {
                return new ResponseEntity<>("Password tidak boleh sama dengan password sebelumnya", HttpStatus.BAD_REQUEST);
            }

            oldUser.setPassword(newUser.getPassword());
            userRepository.save(oldUser);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Username sudah terpakai", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        try {
            User user = userRepository.findById(id).
                    orElseThrow(() -> new RuntimeException("User not found for this id: " + id));
            userRepository.delete(user);
            return new ResponseEntity<>("User berhasil dihapus", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User gagal dihapus", HttpStatus.BAD_REQUEST);
        }
    }

}
