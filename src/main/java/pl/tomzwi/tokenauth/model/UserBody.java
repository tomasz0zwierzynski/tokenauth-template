package pl.tomzwi.tokenauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBody {

    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    @Email
    private String email;

}
