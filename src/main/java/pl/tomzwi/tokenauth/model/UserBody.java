package pl.tomzwi.tokenauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBody {

    private String username;
    private String password;
    private String email;

}
