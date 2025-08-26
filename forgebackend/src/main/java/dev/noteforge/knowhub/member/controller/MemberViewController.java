package dev.noteforge.knowhub.member.controller;

import dev.noteforge.knowhub.member.dto.RegisterRequestDTO;
import dev.noteforge.knowhub.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MemberViewController {

    private  final MemberService memberService;

    //thymeleaf에서 th:data-msg 속성으로 받기위해 파라미터를 받고 이를 다시 model로 붙임
    // 만약 이렇게 안하고 <body th:data-msg="${parmm.msg}"> 이렇게 하면 보안정책에 걸림
    @GetMapping("/login")
    public String loginForm(@RequestParam(name ="msg", required = false) String msg, Model model) {
        model.addAttribute("msg", msg);
        return "member/login";
    }

    //회원가입 폼

    @GetMapping("/register")
    public String registerForm(Model model) {

        if (!model.containsAttribute("registerRequestDTO")) {
            model.addAttribute("registerRequestDTO", new RegisterRequestDTO());
        }

        return "member/register";
    }

    //회원가입 처리
    @PostMapping("/register")
    public String registerProcess(@Valid @ModelAttribute("registerRequestDTO") RegisterRequestDTO dto, BindingResult result, RedirectAttributes redirectAttributes) {

        //log.info(dto.toString());

        if(result.hasErrors()) {
            //log.info(result.toString());
            // 검증 실패 시, 오류 메시지와 입력값을 flash에 저장
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerRequestDTO", result);
            redirectAttributes.addFlashAttribute("registerRequestDTO", dto);

            return "redirect:/register";  // PRG 패턴
        }

        memberService.register(dto);

        redirectAttributes.addFlashAttribute("returnMsg", "회원가입이 완료되었습니다. 로그인해 주세요!");

        return "redirect:/login";   // 회원가입이 끝나면 다시 로그인 페이지로 돌아감
    }

    @GetMapping("/")
    public String home(@RequestParam(name ="msg", required = false) String msg, Model model) {
        model.addAttribute("msg", msg);
        return "home";
    }


}
