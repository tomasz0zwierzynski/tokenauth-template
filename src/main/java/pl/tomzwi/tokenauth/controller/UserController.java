package pl.tomzwi.tokenauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import pl.tomzwi.tokenauth.configuration.CurrentlyLoggedUser;
import pl.tomzwi.tokenauth.configuration.ErrorEntityPreparator;
import pl.tomzwi.tokenauth.entity.Role;
import pl.tomzwi.tokenauth.entity.User;
import pl.tomzwi.tokenauth.exception.UserActivateCodeNotCorrectException;
import pl.tomzwi.tokenauth.exception.UserAlreadyActivatedException;
import pl.tomzwi.tokenauth.exception.UserAlreadyExistsException;
import pl.tomzwi.tokenauth.exception.UserEmailAlreadyExistsException;
import pl.tomzwi.tokenauth.model.ErrorResponse;
import pl.tomzwi.tokenauth.model.UserBody;
import pl.tomzwi.tokenauth.model.UserResponse;
import pl.tomzwi.tokenauth.service.FreemarkerService;
import pl.tomzwi.tokenauth.service.TokenService;
import pl.tomzwi.tokenauth.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping( "/${security.endpoint.users.prefix}" )
public class UserController {

    @Value("${security.email.verification}")
    private boolean emailVerification;

    @Autowired
    private ErrorEntityPreparator errorEntity;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MailSender emailSender;

    @Autowired
    private FreemarkerService freemarkerService;

    @PostMapping( "/register" )
    public ResponseEntity<Object> register(@RequestBody @Valid UserBody user) {
        User registeredUser = userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail());

        if ( emailVerification ) {
            // send verification email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Kod aktywacyjny");

            String text = freemarkerService.generateActivationMail(user.getUsername(), registeredUser.getGenerated());
            message.setText(text);

            emailSender.send(message);
        } else {
            userService.activateUser( registeredUser.getUsername() );
        }

        return new ResponseEntity<>( HttpStatus.OK );
    }

    @PostMapping( "/activate" )
    public ResponseEntity<Object> activate(@RequestParam("code") String code, @CurrentlyLoggedUser User user ) {
        if ( emailVerification ) {
            userService.activateUser(user.getUsername(), code);
        } else {
            return new ResponseEntity<>( HttpStatus.FORBIDDEN );
        }
        return new ResponseEntity<>( HttpStatus.OK );
    }

    @GetMapping( "/roles" )
    public ResponseEntity<UserResponse> getRoles(@RequestParam("username") String username ) {
        User user = userService.getByUsername(username);

        UserResponse response = new UserResponse( user.getUsername(), user.getEmail(), user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @PostMapping( "/roles" )
    public ResponseEntity<UserResponse> addRoles(@RequestParam("username") String username, @RequestParam("roles") String roles ) {
        List<String> requestedRoles = Arrays.asList( roles.split("\\s*,\\s*"));

        User user = userService.addRoles(username, requestedRoles);

        UserResponse response = new UserResponse( user.getUsername(), user.getEmail(), user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @DeleteMapping( "/roles" )
    public ResponseEntity<UserResponse> removeRoles(@RequestParam("username") String username, @RequestParam("roles") String roles ) {
        List<String> requestedRoles = Arrays.asList( roles.split("\\s*,\\s*"));

        User user = userService.removeRoles(username, requestedRoles);

        UserResponse response = new UserResponse( user.getUsername(), user.getEmail(), user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @ExceptionHandler( { UserAlreadyExistsException.class, UserEmailAlreadyExistsException.class, UserActivateCodeNotCorrectException.class, UserAlreadyActivatedException.class } )
    public ResponseEntity<ErrorResponse> handleRegisterExceptions( Exception ex ) {
        return errorEntity.prepare( HttpStatus.FORBIDDEN, ex );
    }

}
