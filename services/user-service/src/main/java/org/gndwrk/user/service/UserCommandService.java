package org.gndwrk.user.service;

import org.gndwrk.user.domain.model.User;
import org.gndwrk.user.port.in.CreateUserUseCase;
import org.gndwrk.user.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class UserCommandService implements CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UserCommandService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User createUser(User user) {
        return userRepositoryPort.save(user);
    }
}
