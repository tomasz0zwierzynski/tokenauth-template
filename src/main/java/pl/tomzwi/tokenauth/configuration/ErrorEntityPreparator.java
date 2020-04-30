package pl.tomzwi.tokenauth.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.tomzwi.tokenauth.model.ErrorResponse;

@Component
public class ErrorEntityPreparator {

    public ResponseEntity<ErrorResponse> prepare(HttpStatus httpStatus, Exception ex ) {
        ErrorResponse error = new ErrorResponse();

        error.setException( ex.getClass().getSimpleName() );
        error.setMessage( ex.getMessage() );
        error.setStatus( String.valueOf( httpStatus.value() ) );

        return new ResponseEntity<>( error, httpStatus );
    }

}
