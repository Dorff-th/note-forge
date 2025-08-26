package dev.noteforge.knowhub.common.exception;

/**
 * 회원가입시 이메일이나 닉네임 중복발생 했을 시 정의하는 커스텀 Exception
 */
public class DuplicateResourceException extends RuntimeException {
    private final String messageKey;
    public DuplicateResourceException(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
