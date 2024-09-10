package com.cm39.cm39.user.domain;

import com.cm39.cm39.validator.NotFutureDate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements UserDetails {
    @NotBlank
    @Email
    @Size(max = 50)
    private String userId;
    @Size(max = 50)
    private String grdId;
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$")
    @Size(max = 500)
    private String pwd;
    @Size(max = 1000)
    private String refreshToken;
    @NotBlank
    @Size(max = 50)
    private String userName;
    @NotNull
    @Past
    private LocalDateTime birth;
    @NotBlank
    @Size(max = 1)
    private String gndr;
    @NotBlank
    @Size(max = 50)
    private String telNo;
    @Size(max = 50)
    private String userStatCode;
    @Size(max = 50)
    private String snsTypeCode;
    @NotNull
    @PastOrPresent
    private LocalDateTime mntTrmsAgrDate; // 필수약관
    @NotNull
    @PastOrPresent
    private LocalDateTime prsnlInfoAgrDate; // 개인정보수집약관
    @NotNull
    @PastOrPresent
    private LocalDateTime adInfoRcvAgrDate; // 광고수신동의약관
    @Size(max = 30)
    private String zpcd;
    @Size(max = 100)
    private String mainAddr;
    @Size(max = 100)
    private String detailAddr;
    @Size(max = 50)
    private String rcntPayTypeCode;
    private LocalDateTime signupDate;
    private LocalDateTime wdrwDate;
    private LocalDateTime rcntLoginDate;
    private LocalDateTime regDate;
    @Size(max = 50)
    private String regId;
    private LocalDateTime upDate;
    @Size(max = 50)
    private String upId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    // jwt 토큰
    public void destroyRefreshToken() {
        this.refreshToken = null;
    }
}
