package org.gndwrk.user.port.in;

import org.gndwrk.user.domain.model.User;

public interface CreateUserUseCase {
  User createUser(User user);
}
