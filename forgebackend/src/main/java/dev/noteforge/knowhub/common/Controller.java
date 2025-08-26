package dev.noteforge.knowhub.common;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/error/403")
    public String error403() {
        return "error/403"; // templates/error/403.html 렌더링됨
    }

    // 선택: 기타 오류 페이지도 추가 가능
    @GetMapping("/error/404")
    public String error404() {
        return "error/404";
    }

    @GetMapping("/error/500")
    public String error500() {
        return "error/500";
    }
}
