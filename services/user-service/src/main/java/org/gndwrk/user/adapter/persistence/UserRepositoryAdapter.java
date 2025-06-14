package org.gndwrk.user.adapter.persistence;

import org.gndwrk.user.domain.model.User;
import org.gndwrk.user.port.out.UserRepositoryPort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoUserRepository extends MongoRepository<User, String> {}

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final MongoUserRepository mongoUserRepository;

    public UserRepositoryAdapter(MongoUserRepository mongoUserRepository) {
        this.mongoUserRepository = mongoUserRepository;
    }

    @Override
    public User save(User user) {
        return mongoUserRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return mongoUserRepository.findById(id);
    }
}
