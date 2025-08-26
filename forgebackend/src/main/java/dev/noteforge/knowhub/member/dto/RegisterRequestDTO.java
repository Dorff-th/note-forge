package dev.noteforge.knowhub.member.dto;

import dev.noteforge.knowhub.common.validation.PasswordMatch;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 회원가입 요청 DTO
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatch
public class RegisterRequestDTO {

    @NotBlank(message = "{NotBlank.registerRequestDTO.nickname}")
    @Size(min = 2, max = 20, message = "{Size.registerRequestDTO.nickname}")
    private String nickname;

    @NotBlank(message = "{NotBlank.registerRequestDTO.email}")
    @Email(message = "{Email.registerRequestDTO.email}")
    private String email;           // 물론 별도의 이메일 필드가 존재하므로 필요함.

    @NotBlank(message = "{NotBlank.registerRequestDTO.password}")
    @Size(min = 4, message = "{sizePassword}")
    private String password;

    @NotBlank(message = "{NotBlank.registerRequestDTO.confirmPassword}")
    private String confirmPassword;

    @AssertTrue(message = "{email.checked}")
    private boolean emailChecked;       // email 중복사용 체크 여부

    @AssertTrue(message = "{nickname.checked}")
    private boolean nicknameChecked;  // nickname 중복사용 체크 여부

}
