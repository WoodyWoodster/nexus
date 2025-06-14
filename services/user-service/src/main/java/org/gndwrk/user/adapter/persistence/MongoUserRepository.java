package org.gndwrk.user.adapter.persistence;

import org.gndwrk.user.domain.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoUserRepository extends MongoRepository<User, String> {
}
