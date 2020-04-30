package pl.tomzwi.tokenauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tomzwi.tokenauth.configuration.ErrorEntityPreparator;
import pl.tomzwi.tokenauth.exception.UserAlreadyExistsException;
import pl.tomzwi.tokenauth.exception.UserEmailAlreadyExistsException;
import pl.tomzwi.tokenauth.model.ErrorResponse;
import pl.tomzwi.tokenauth.model.UserBody;
import pl.tomzwi.tokenauth.service.UserService;

@RestController
@RequestMapping( "/${security.endpoint.users.prefix}" )
public class UserController {

    @Autowired
    private ErrorEntityPreparator errorEntity;

    @Autowired
    private UserService userService;

    @PostMapping( "/register" )
    public ResponseEntity<Object> register(@RequestBody UserBody user) {
        userService.registerUser( user.getUsername(), user.getPassword(), user.getEmail() );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    @ExceptionHandler( { UserAlreadyExistsException.class, UserEmailAlreadyExistsException.class } )
    public ResponseEntity<ErrorResponse> handleRegisterExceptions( Exception ex ) {
        return errorEntity.prepare( HttpStatus.FORBIDDEN, ex );
    }

}
