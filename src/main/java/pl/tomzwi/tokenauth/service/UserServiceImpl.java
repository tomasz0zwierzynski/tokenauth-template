package pl.tomzwi.tokenauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tomzwi.tokenauth.entity.Role;
import pl.tomzwi.tokenauth.entity.User;
import pl.tomzwi.tokenauth.exception.UserAlreadyExistsException;
import pl.tomzwi.tokenauth.exception.UserEmailAlreadyExistsException;
import pl.tomzwi.tokenauth.exception.UserNotFoundException;
import pl.tomzwi.tokenauth.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Value("${security.default.role}")
    private String defaultRole;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException("User not found") );
    }

    @Override
    public boolean isUsernamePasswordCorrect(String username, String password) {
        Optional<User> user = userRepository.findByUsername( username );

        return user.filter(value -> passwordEncoder.matches(password, value.getPassword())).isPresent();
    }

    @Override
    public User registerUser(String username, String password, String email) throws UserAlreadyExistsException, UserEmailAlreadyExistsException {
        Optional<User> alreadyCreatedUser = userRepository.findByUsername(username);
        if ( alreadyCreatedUser.isPresent() ) {
            throw new UserAlreadyExistsException("Given user already registered");
        }

        Optional<User> emailAlreadyPresent = userRepository.findByEmail(email);
        if ( emailAlreadyPresent.isPresent() ) {
            throw new UserEmailAlreadyExistsException("Email already in use");
        }

        Role defaultRoleObject = roleService.getRoleByName( defaultRole );

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles( Collections.singletonList(defaultRoleObject) );

        userRepository.save( user );

        return user;
    }
}
