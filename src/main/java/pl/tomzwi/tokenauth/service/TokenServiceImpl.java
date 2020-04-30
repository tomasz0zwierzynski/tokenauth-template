package pl.tomzwi.tokenauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tomzwi.tokenauth.entity.Token;
import pl.tomzwi.tokenauth.entity.User;
import pl.tomzwi.tokenauth.exception.InvalidUsernamePasswordException;
import pl.tomzwi.tokenauth.exception.TokenNotFoundException;
import pl.tomzwi.tokenauth.exception.UserNotFoundException;
import pl.tomzwi.tokenauth.repository.TokenRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    UserService userService;

    @Autowired
    TokenRepository tokenRepository;

    @Value("${token.validity.time.minutes}")
    private Integer tokenValidityMinutes;

    @Override
    public Token getToken(String username, String password) throws InvalidUsernamePasswordException {
        User user = null;

        try {
            user = userService.getByUsername(username);
        } catch ( UserNotFoundException ex ) {
            throw new InvalidUsernamePasswordException("Invalid username or password");
        }

        if ( userService.isUsernamePasswordCorrect( username, password ) ) {

            LocalDateTime now = LocalDateTime.now();

            List<Token> validTokens = user.getTokens().stream()
                    .filter( token -> token.getExpires().isAfter(now) )
                    .collect(Collectors.toList());
            if ( validTokens.isEmpty() ) {
                // proper token do not exist
                Token token = new Token();
                token.setToken(UUID.randomUUID().toString());
                token.setUser(user);
                token.setExpires(LocalDateTime.now().plusMinutes(tokenValidityMinutes));

                tokenRepository.save(token);
                return token;
            } else {
                // proper token exists
                return validTokens.stream().max(Comparator.comparing(Token::getExpires)).orElseThrow(() -> new InvalidUsernamePasswordException("Invalid username or password")
                );
            }
        } else {
            throw new InvalidUsernamePasswordException("Invalid username or password");
        }

    }

    @Override
    public Token getToken(String token) throws TokenNotFoundException {
        return tokenRepository.findByToken(token).orElseThrow(() -> new TokenNotFoundException("Token not found") );
    }

    @Override
    public void purge() {
        LocalDateTime now = LocalDateTime.now();
        List<Token> expiredBeforeDate = tokenRepository.findExpiredBeforeDate(now); // TODO: można to wykonać jako jedno zapytanie
        for ( Token token : expiredBeforeDate ) {
            tokenRepository.delete( token );
        }
    }
}
