package dev.noteforge.knowhub.search.util;

import java.util.regex.Pattern;

public class SummaryUtil {

    /**
     * 본문에서 키워드 주변의 요약 텍스트를 추출한다.
     * - 마크다운 문법 제거
     * - HTML 태그 제거
     * - 이미지 링크 제거
     * 결과적으로 순수 텍스트만 남긴다.
     *
     * @param text    원본문
     * @param keyword 검색 키워드
     * @param radius  키워드 앞뒤로 포함할 문자 수
     * @return 요약 텍스트 (없으면 null)
     */
    public static String extractSummary(String text, String keyword, int radius) {
        if (text == null || keyword == null) return null;

        // 1. 전처리: 마크다운/HTML/이미지 제거
        String cleaned = text
                // 마크다운 이미지 제거: ![alt](url)
                .replaceAll("!\\[[^\\]]*\\]\\([^)]*\\)", "")
                // HTML 이미지 태그 제거
                .replaceAll("<img[^>]*>", "")
                // 모든 HTML 태그 제거
                .replaceAll("<[^>]+>", "")
                // 마크다운 굵게 (**bold** or __bold__)
                .replaceAll("(\\*\\*|__)(.*?)\\1", "$2")
                // 마크다운 기울임 (*italic* or _italic_)
                .replaceAll("(\\*|_)(.*?)\\1", "$2")
                // 마크다운 취소선 (~~strike~~)
                .replaceAll("~~(.*?)~~", "$1")
                // 마크다운 헤더 (#, ##, ### 등)
                .replaceAll("(?m)^#{1,6}\\s*", "")
                // 마크다운 인용문 (> )
                .replaceAll("(?m)^>\\s?", "")
                // 마크다운 리스트 (-, *, 1.)
                .replaceAll("(?m)^([-*+]\\s+|\\d+\\.\\s+)", "")
                // 인라인 코드 (`code`)
                .replaceAll("`([^`]*)`", "$1")
                // 링크 [text](url) → text
                .replaceAll("\\[([^\\]]+)\\]\\([^)]*\\)", "$1")
                // 잔여 특수문자 정리
                .replaceAll("[`>*~]", "")
                // 공백 정리
                .replaceAll("\\s+", " ")
                .trim();

        if (cleaned.isEmpty()) return null;

        // 2. 키워드 위치 탐색
        String lowerText = cleaned.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();

        int idx = lowerText.indexOf(lowerKeyword);
        if (idx == -1) return null;

        // 3. 앞뒤 radius 만큼 잘라내기
        int start = Math.max(0, idx - radius);
        int end = Math.min(cleaned.length(), idx + keyword.length() + radius);

        StringBuilder sb = new StringBuilder();
        if (start > 0) sb.append("...");
        sb.append(cleaned, start, end);
        if (end < cleaned.length()) sb.append("...");

        return sb.toString();
    }


    //검색 결과에서 검색 키워드 하이라이트 표시
    public static String highlightKeyword(String text, String keyword) {
        if (text == null || keyword == null) return text;
        return text.replaceAll("(?i)(" + Pattern.quote(keyword) + ")", "<mark>$1</mark>");
    }
}

