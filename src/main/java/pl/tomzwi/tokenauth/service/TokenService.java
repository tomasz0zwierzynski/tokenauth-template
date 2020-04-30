package pl.tomzwi.tokenauth.service;

import pl.tomzwi.tokenauth.entity.Token;
import pl.tomzwi.tokenauth.exception.InvalidUsernamePasswordException;
import pl.tomzwi.tokenauth.exception.TokenNotFoundException;

public interface TokenService {

    Token getToken(String username, String password ) throws InvalidUsernamePasswordException;

    Token getToken(String token) throws TokenNotFoundException;

    void purge();

}
