package dev.noteforge.knowhub.common;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 로그인 페이지에서 로그인 성공/실패에 대한 메시지를 반환하는 Advice
 */
@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute
    public void globalAttributes(@RequestParam(name ="msg", required = false) String msg, Model model) {

        if(msg != null) {

            String returnMsg = switch (msg) {
                case "loginSuccess" -> "로그인에 성공하였습니다.";
                case "logoutSuccess" -> "로그아웃에 성공하였습니다.";
                case "error" -> "error";
                default -> "기본 msg!";
            };

            model.addAttribute("returnMsg", returnMsg);
        }

    }
}
