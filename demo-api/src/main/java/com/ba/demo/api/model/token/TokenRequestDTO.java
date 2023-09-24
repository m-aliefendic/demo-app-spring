package com.ba.demo.api.model.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.flywaydb.core.internal.util.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequestDTO {
    @NotNull
    private String email;
    @NotNull
    @Size(min = 8)
    private String password;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TokenDto{");
        sb.append("username='").append(email).append('\'');
        sb.append(", password='").append(StringUtils.left(password,1)).append("***\'");
        sb.append('}');
        return sb.toString();
    }
}
