package ru.zalimannard.dripchip.schema.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

@Value
@Builder(toBuilder = true)
public class AccountRequestDto {

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
    @JsonProperty("role")
    @NotNull
    AccountRole role;

}
