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
    private String grdId;
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$")
    private String pwd;
    @NotBlank
    private String userName;
    @NotNull
    @Past
    private LocalDateTime birth;
    @NotBlank
    private String gndr;
    @NotBlank
    private String telNo;
    private String userStatCode;
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
    private String zpcd;
    private String mainAddr;
    private String detailAddr;
    private String rcntPayTypeCode;
    private LocalDateTime signupDate;
    private LocalDateTime wdrwDate;
    private LocalDateTime rcntLoginDate;
    private LocalDateTime regDate;
    private String regId;
    private LocalDateTime upDate;
    private String upId;

    // password 저장시 자동 암호화
    public UserDto encodePassword(PasswordEncoder passwordEncoder) {
        this.pwd = passwordEncoder.encode(this.pwd);
        return this;
    }

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
}
