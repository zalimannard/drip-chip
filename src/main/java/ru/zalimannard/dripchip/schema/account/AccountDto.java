package ru.zalimannard.dripchip.schema.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

@Value
@Builder
public class AccountDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    Integer id;

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

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    String password;

    @JsonProperty("role")
    @NotNull
    AccountRole role;

}
