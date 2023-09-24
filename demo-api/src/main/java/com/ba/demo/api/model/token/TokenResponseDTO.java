package com.ba.demo.api.model.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDTO {

    private String token;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TokenDto{");
        sb.append(", token='").append(token).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
