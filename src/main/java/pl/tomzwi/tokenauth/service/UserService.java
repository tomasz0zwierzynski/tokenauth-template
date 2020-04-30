package pl.tomzwi.tokenauth.service;

import pl.tomzwi.tokenauth.entity.User;
import pl.tomzwi.tokenauth.exception.UserAlreadyExistsException;
import pl.tomzwi.tokenauth.exception.UserEmailAlreadyExistsException;
import pl.tomzwi.tokenauth.exception.UserNotFoundException;

public interface UserService {

    User getByUsername( String username ) throws UserNotFoundException;

    boolean isUsernamePasswordCorrect( String username, String password );

    User registerUser( String username, String password, String email ) throws UserAlreadyExistsException, UserEmailAlreadyExistsException;

}
