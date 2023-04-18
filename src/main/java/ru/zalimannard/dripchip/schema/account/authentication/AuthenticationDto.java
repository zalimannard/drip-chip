package ru.zalimannard.dripchip.schema.account.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class AuthenticationDto {

    @JsonProperty("firstName")
    @NotBlank
    String firstName;

    @JsonProperty("lastName")
    @NotBlank
    String lastName;

    @JsonProperty("email")
    @NotBlank
    @Email
    String email;

    @JsonProperty("password")
    @NotBlank
    String password;

}
