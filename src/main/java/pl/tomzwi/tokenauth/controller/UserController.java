package pl.tomzwi.tokenauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import pl.tomzwi.tokenauth.configuration.CurrentlyLoggedUser;
import pl.tomzwi.tokenauth.configuration.ErrorEntityPreparator;
import pl.tomzwi.tokenauth.entity.Token;
import pl.tomzwi.tokenauth.entity.User;
import pl.tomzwi.tokenauth.exception.UserActivateCodeNotCorrectException;
import pl.tomzwi.tokenauth.exception.UserAlreadyExistsException;
import pl.tomzwi.tokenauth.exception.UserEmailAlreadyExistsException;
import pl.tomzwi.tokenauth.model.ActivateUserBody;
import pl.tomzwi.tokenauth.model.ErrorResponse;
import pl.tomzwi.tokenauth.model.UserBody;
import pl.tomzwi.tokenauth.service.TokenService;
import pl.tomzwi.tokenauth.service.UserService;

@RestController
@RequestMapping( "/${security.endpoint.users.prefix}" )
public class UserController {

    @Autowired
    private ErrorEntityPreparator errorEntity;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MailSender emailSender;

    @PostMapping( "/register" )
    public ResponseEntity<Object> register(@RequestBody UserBody user) {
        User registeredUser = userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo( "tomasz0zwierzynski@gmail.com" );
        message.setSubject("Kod aktywacyjny");
        message.setText( registeredUser.getGenerated() );

        emailSender.send( message );

        return new ResponseEntity<>( HttpStatus.OK );
    }

    @PostMapping( "/activate" )
    public ResponseEntity<Object> activate(@RequestBody ActivateUserBody activate ) {

        Token token = tokenService.getToken(activate.getToken());

        userService.activateUser( token.getUser().getUsername(), activate.getCode() );

        return new ResponseEntity<>( HttpStatus.OK );
    }

    @ExceptionHandler( { UserAlreadyExistsException.class, UserEmailAlreadyExistsException.class, UserActivateCodeNotCorrectException.class } )
    public ResponseEntity<ErrorResponse> handleRegisterExceptions( Exception ex ) {
        return errorEntity.prepare( HttpStatus.FORBIDDEN, ex );
    }

}
