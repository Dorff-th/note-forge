package dev.noteforge.knowhub.member.controller;



import dev.noteforge.knowhub.common.util.JwtTokenProvider;
import dev.noteforge.knowhub.member.dto.LoginRequest;
import dev.noteforge.knowhub.member.dto.LoginResponse;
import dev.noteforge.knowhub.member.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        //UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        MemberDetails userDetails = (MemberDetails) authentication.getPrincipal();

        // JWT 발급
        String token = jwtTokenProvider.createToken(userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority(),
                userDetails.getNickname(),
                userDetails.getProfileImageUrl());

        return LoginResponse.builder()
                .token(token)
                .user(LoginResponse.UserInfo.builder()
                        .id(userDetails.getId())
                        .username(userDetails.getUsername())
                        .role(userDetails.getAuthorities().iterator().next().getAuthority())
                        .nickname(userDetails.getNickname())
                        .profileImageUrl(userDetails.getProfileImageUrl())
                        .build())
                .build();
    }
}


