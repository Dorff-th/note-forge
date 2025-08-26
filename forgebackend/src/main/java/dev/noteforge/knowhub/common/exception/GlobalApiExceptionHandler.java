package dev.noteforge.knowhub.common.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * API 호출시 발생하는 예외 핸들러
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalApiExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicate(DuplicateResourceException ex, Locale locale) {
        System.out.println(">>> locale = " + locale);
        System.out.println(">>> message key = " + ex.getMessageKey());

        String message = null;
        try {
            message = messageSource.getMessage(ex.getMessageKey(), null, locale);
        } catch (NoSuchMessageException e) {
            message = "정의되지 않은 메시지입니다.";
        }

        // Map.of는 null 허용 안함 → Map.ofEntries 사용하거나 LinkedHashMap 등으로 처리
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("error", message);
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "");  // 여기도 null-safe
        body.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);

    }
}
