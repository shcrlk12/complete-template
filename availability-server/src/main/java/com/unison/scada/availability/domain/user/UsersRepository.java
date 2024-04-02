package com.unison.scada.availability.domain.user;

import com.unison.scada.availability.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, String> {
}
