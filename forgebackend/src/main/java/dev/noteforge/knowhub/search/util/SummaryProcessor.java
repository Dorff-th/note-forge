package dev.noteforge.knowhub.search.util;

import dev.noteforge.knowhub.search.dto.SearchResultDTO;

public class SummaryProcessor {

    private static final int SUMMARY_RADIUS = 30;

    public static SearchResultDTO applySummaryAndMatch(SearchResultDTO dto, String keyword) {

        if (contains(dto.getTitle(), keyword)) {
            dto.setMatchedField("POST_TITLE");
            dto.setSummary(SummaryUtil.extractSummary(dto.getTitle(), keyword, SUMMARY_RADIUS));
        } else if (contains(dto.getContent(), keyword)) {
            dto.setMatchedField("POST_CONTENT");
            dto.setSummary(SummaryUtil.extractSummary(dto.getContent(), keyword, SUMMARY_RADIUS));
        } else if (contains(dto.getCommentContent(), keyword)) {
            dto.setMatchedField("COMMENT_CONTENT");
            dto.setSummary(SummaryUtil.extractSummary(dto.getCommentContent(), keyword, SUMMARY_RADIUS));
        } else if (contains(dto.getWriterName(), keyword)) {
            dto.setMatchedField("POST_WRITER");
            dto.setSummary(dto.getWriterName());
        } else if (contains(dto.getCommentWriter(), keyword)) {
            dto.setMatchedField("COMMENT_WRITER");
            dto.setSummary(dto.getCommentWriter());
        } else if (contains(dto.getCategoryName(), keyword)) {
            dto.setMatchedField("CATEGORY_NAME");
            dto.setSummary(dto.getCategoryName());
        }
        return dto;
    }

    private static boolean contains(String source, String keyword) {
        return source != null && keyword != null &&
                source.toLowerCase().contains(keyword.toLowerCase());
    }
}
