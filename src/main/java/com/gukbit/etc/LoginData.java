package com.gukbit.etc;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class LoginData {
    @NotBlank
    private String id = "";
    @NotBlank
    private String pw = "";
}
