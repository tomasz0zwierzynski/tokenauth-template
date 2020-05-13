package pl.tomzwi.tokenauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tomzwi.tokenauth.configuration.CurrentlyLoggedUser;
import pl.tomzwi.tokenauth.configuration.ErrorEntityPreparator;
import pl.tomzwi.tokenauth.entity.Role;
import pl.tomzwi.tokenauth.entity.Token;
import pl.tomzwi.tokenauth.entity.User;
import pl.tomzwi.tokenauth.exception.InvalidUsernamePasswordException;
import pl.tomzwi.tokenauth.model.ErrorResponse;
import pl.tomzwi.tokenauth.model.TokenResponse;
import pl.tomzwi.tokenauth.model.UserBody;
import pl.tomzwi.tokenauth.model.UserResponse;
import pl.tomzwi.tokenauth.service.TokenService;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestController
@RequestMapping( "/${security.endpoint.token.prefix}" )
public class TokenController {

    @Autowired
    private ErrorEntityPreparator errorEntity;

    @Autowired
    private TokenService tokenService;

    @PostMapping( "/token" )
    public ResponseEntity<TokenResponse> token(@RequestParam("username") final String username, @RequestParam("password") final String password) {
        Token token = tokenService.getToken(username, password);
        TokenResponse response = new TokenResponse(
                token.getUser().getUsername(),
                token.getToken(),
                DateTimeFormatter.ISO_DATE_TIME.format(token.getExpires()),
                token.getUser().getRoles().stream().map(Role::getName).collect(Collectors.toList())
                );
        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @GetMapping( "/current" )
    public ResponseEntity<UserResponse> current(@CurrentlyLoggedUser User user) {
        UserResponse userBody = new UserResponse(user.getUsername(), user.getEmail(), user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        return new ResponseEntity<>( userBody, HttpStatus.OK );
    }

    @ExceptionHandler( { InvalidUsernamePasswordException.class } )
    public ResponseEntity<ErrorResponse> handleInvalidUsernamePasswordException( Exception ex ) {
        return errorEntity.prepare( HttpStatus.BAD_REQUEST, ex );
    }

}
