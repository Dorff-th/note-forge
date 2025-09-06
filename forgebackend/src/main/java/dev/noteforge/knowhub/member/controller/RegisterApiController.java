package dev.noteforge.knowhub.member.controller;

import dev.noteforge.knowhub.common.exception.DuplicateResourceException;
import dev.noteforge.knowhub.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 회원가입에 필요한 email, nickname 중복 검사를 위한 API 호출용 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterApiController {

    final private MemberService memberService;

    @GetMapping("/checked-email")
    public ResponseEntity<?> checkedEmail(@RequestParam("email") String email) {

        if (memberService.existsByEmail(email)) {
            throw new DuplicateResourceException("Duplicate.email");
        }
        return ResponseEntity.ok(Map.of("duplicate", false));
    }

    @GetMapping("/checked-nickname")
    public ResponseEntity<?> checkedNickname(@RequestParam("nickname") String nickname) {

        if (memberService.existsByNickname(nickname)) {
            throw new DuplicateResourceException("Duplicate.nickname");
        }
        return ResponseEntity.ok(Map.of("duplicate", false));
    }

}
