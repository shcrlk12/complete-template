package com.unison.scada.availability.global.config.security;

import com.unison.scada.availability.api.user.UsersRepository;
import com.unison.scada.availability.api.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> optionalUsers = usersRepository.findById(username);

        Users users = optionalUsers.orElseThrow(() -> new UsernameNotFoundException(String.format("not found user [%s]", username)));

        users.setLastLoginTime(LocalDateTime.now());
        usersRepository.save(users);

        return new UserDetailImpl(users);
    }
}
