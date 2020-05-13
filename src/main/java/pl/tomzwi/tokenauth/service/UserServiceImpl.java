package pl.tomzwi.tokenauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tomzwi.tokenauth.entity.Role;
import pl.tomzwi.tokenauth.entity.User;
import pl.tomzwi.tokenauth.exception.*;
import pl.tomzwi.tokenauth.repository.UserRepository;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Value("${security.default.role}")
    private String defaultRole;

    @Value("${security.inactive.role}")
    private String inactiveRole;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Random random = new Random();

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

        Role inactiveRoleObject = roleService.getRoleByName( inactiveRole );

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        List<Role> roles = new ArrayList<>();
        roles.add( inactiveRoleObject );

        user.setRoles(roles);
        user.setGenerated( String.valueOf(random.nextInt(9999 - 1000) + 1000 ) );

        userRepository.save( user );
        userRepository.flush();

        return user;
    }

    @Override
    public User activateUser(String username, String code) throws UserActivateCodeNotCorrectException {
        User user = userRepository.findByUsername(username).orElseThrow( () -> new UserNotFoundException("User not found") );

        if ( user.getActive() ) {
            throw new UserAlreadyActivatedException("User activated!");
        }

        if ( !code.equals(user.getGenerated() != null ? user.getGenerated() : "") ) {
            throw new UserActivateCodeNotCorrectException("Activation code does not match");
        }

        user.setActive( true );
        user.setGenerated( "" );

        Role defaultRoleObject = roleService.getRoleByName( defaultRole );

        List<Role> roles = new ArrayList<>();

        roles.add( defaultRoleObject );
        user.setRoles( roles );

        userRepository.save( user );

        return user;
    }

    @Override
    public User activateUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow( () -> new UserNotFoundException("User not found") );

        if ( user.getActive() ) {
            throw new UserAlreadyActivatedException("User activated!");
        }

        user.setActive( true );
        user.setGenerated( "" );

        Role defaultRoleObject = roleService.getRoleByName( defaultRole );

        List<Role> roles = new ArrayList<>();
        roles.add( defaultRoleObject );

        user.setRoles( roles );

        userRepository.save( user );

        return user;
    }

    @Override
    public User addRoles(String username, Collection<String> roles) throws UserNotFoundException, RoleNotFoundException {
        User user = userRepository.findByUsername( username ).orElseThrow( () -> new UsernameNotFoundException("User not found"));

        List<Role> userRoles = user.getRoles();

        for ( String roleStr : roles ) {
            if ( roleStr.equals( inactiveRole ) || roleStr.equals( defaultRole ) ) continue;

            Role role = roleService.getRoleByName( roleStr );

            if ( !userRoles.contains( role ) ) {
                user.getRoles().add( role );
            }
        }

        userRepository.save( user );

        return user;
    }

    @Override
    public User removeRoles(String username, Collection<String> roles) throws UserNotFoundException, RoleNotFoundException {
        User user = userRepository.findByUsername( username ).orElseThrow( () -> new UsernameNotFoundException("User not found"));

        List<Role> userRoles = user.getRoles();

        for ( String roleStr : roles ) {
            if ( roleStr.equals( inactiveRole ) || roleStr.equals( defaultRole ) ) continue;

            Role role = roleService.getRoleByName( roleStr );

            if ( userRoles.contains( role ) ) {
                user.getRoles().remove( role );
            }
        }

        userRepository.save( user );

        return user;
    }
}
