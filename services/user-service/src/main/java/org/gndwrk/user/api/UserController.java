package org.gndwrk.user.api;

import org.gndwrk.user.domain.model.User;
import org.gndwrk.user.port.in.CreateUserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = createUserUseCase.createUser(user);
        return ResponseEntity.ok(created);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUser(@PathVariable String id) {
//    }
}
