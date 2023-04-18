package ru.zalimannard.dripchip.schema.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

@Value
@Builder(toBuilder = true)
public class AccountResponseDto {

    @JsonProperty("id")
    Integer id;

    @JsonProperty("firstName")
    String firstName;

    @JsonProperty("lastName")
    String lastName;

    @JsonProperty("email")
    String email;

    @JsonProperty("role")
    AccountRole role;

}
