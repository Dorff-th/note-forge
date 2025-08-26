package dev.noteforge.knowhub.search.util;

import java.util.regex.Pattern;

public class SummaryUtil {

    //검색 키워드 중심으로 내용 요약
    public static String extractSummary(String text, String keyword, int radius) {
        if (text == null || keyword == null) return null;

        String lowerText = text.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();

        int idx = lowerText.indexOf(lowerKeyword);
        if (idx == -1) return null;

        int start = Math.max(0, idx - radius);
        int end = Math.min(text.length(), idx + keyword.length() + radius);

        StringBuilder sb = new StringBuilder();
        if (start > 0) sb.append("...");
        sb.append(text, start, end);
        if (end < text.length()) sb.append("...");

        return sb.toString();
    }

    //검색 결과에서 검색 키워드 하이라이트 표시
    public static String highlightKeyword(String text, String keyword) {
        if (text == null || keyword == null) return text;
        return text.replaceAll("(?i)(" + Pattern.quote(keyword) + ")", "<mark>$1</mark>");
    }
}

