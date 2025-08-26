package dev.noteforge.knowhub.admin.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminStatsDTO {
    private long memberCount;
    private long postCount;
    private long commentCount;
    private long attachmentCount;
    private long editorImageCount;
    private long categoryCount;
    private long tagCount;

    private long todayNewMembers;
    private long todayNewPosts;
    private long todayNewComments;
}
