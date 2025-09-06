package dev.noteforge.knowhub.member.controller;

import dev.noteforge.knowhub.member.dto.MemberProfileResponse;
import dev.noteforge.knowhub.member.security.MemberDetails;
import dev.noteforge.knowhub.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/me")
    public MemberProfileResponse getMyProfile(
            @AuthenticationPrincipal(expression = "username") String username
    ) {
        return memberService.getMyProfile(username);
    }
}
