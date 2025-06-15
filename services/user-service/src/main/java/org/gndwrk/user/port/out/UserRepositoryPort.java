package org.gndwrk.user.port.out;

import java.util.Optional;
import org.gndwrk.user.domain.model.User;

public interface UserRepositoryPort {
  User save(User user);

  Optional<User> findById(String id);
}
