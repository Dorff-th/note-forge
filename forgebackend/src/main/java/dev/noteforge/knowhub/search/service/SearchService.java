package dev.noteforge.knowhub.search.service;

import dev.noteforge.knowhub.common.dto.PageRequestDTO;
import dev.noteforge.knowhub.common.dto.PageResponseDTO;
import dev.noteforge.knowhub.search.dto.SearchFilterDTO;
import dev.noteforge.knowhub.search.dto.SearchResultDTO;
import dev.noteforge.knowhub.search.mapper.SearchMapper;
import dev.noteforge.knowhub.search.util.SearchPostMerger;
import dev.noteforge.knowhub.search.util.SummaryProcessor;
import dev.noteforge.knowhub.search.util.SummaryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchMapper searchMapper;

    public List<SearchResultDTO> search(String keyword) {
        List<SearchResultDTO> rawList = searchMapper.searchPostsByKeyword(keyword);

        List<SearchResultDTO> withSummary = rawList.stream()
                .map(dto -> SummaryProcessor.applySummaryAndMatch(dto, keyword))
                .collect(Collectors.toList());

        return SearchPostMerger.deduplicateByPostId(withSummary);

    }

    //위 메소드 페이징 적용
    public PageResponseDTO<SearchResultDTO> searchWithPaging(String keyword, PageRequestDTO pageRequestDTO) {
        List<SearchResultDTO> rawList = searchMapper.searchPostsByKeywordWithPaging(keyword, pageRequestDTO);

        List<SearchResultDTO> withSummary = rawList.stream()
                .map(dto -> SummaryProcessor.applySummaryAndMatch(dto, keyword))
                .collect(Collectors.toList());

        for(SearchResultDTO dto : withSummary) {
            dto.setHighlightedTitle(SummaryUtil.highlightKeyword(dto.getSummary(), keyword));
        }

        int totalCount = searchMapper.searchPostsByKeywordCount(keyword);

        return new PageResponseDTO<>(pageRequestDTO, totalCount, SearchPostMerger.deduplicateByPostId(withSummary), 10);

    }

    // 상세검색
    public PageResponseDTO<SearchResultDTO> searchFilteredPostsWithPaging(SearchFilterDTO searchFilterDTO, PageRequestDTO pageRequestDTO) {

        List<SearchResultDTO> rawList = searchMapper.searchFilteredPostsWithPaging(searchFilterDTO, pageRequestDTO);

        String keyword = searchFilterDTO.getKeyword();

        List<SearchResultDTO> withSummary = rawList.stream()
                .map(dto -> SummaryProcessor.applySummaryAndMatch(dto, keyword))
                .collect(Collectors.toList());

        for(SearchResultDTO dto : withSummary) {
            dto.setHighlightedTitle(SummaryUtil.highlightKeyword(dto.getSummary(), keyword));
        }

        int totalCount = searchMapper.searchFilteredPostsCount(searchFilterDTO);

        return new PageResponseDTO<>(pageRequestDTO, totalCount, SearchPostMerger.deduplicateByPostId(withSummary), 10);

    }

    //테스트 하고 문제 없으면 위에 있는 메서드들은 미사용 처리 예정
    public PageResponseDTO<SearchResultDTO> searchPostsUnified(SearchFilterDTO searchFilterDTO, PageRequestDTO pageRequestDTO) {

        List<SearchResultDTO> rawList = searchMapper.searchPostsUnified(searchFilterDTO, pageRequestDTO);

        String keyword = searchFilterDTO.getKeyword();

        List<SearchResultDTO> withSummary = rawList.stream()
                .map(dto -> SummaryProcessor.applySummaryAndMatch(dto, keyword))
                .collect(Collectors.toList());

        for(SearchResultDTO dto : withSummary) {
            dto.setHighlightedTitle(SummaryUtil.highlightKeyword(dto.getSummary(), keyword));
        }

        int totalCount = searchMapper.searchPostsUnifiedCount(searchFilterDTO);

        return new PageResponseDTO<>(pageRequestDTO, totalCount, SearchPostMerger.deduplicateByPostId(withSummary), 10);

    }

    //검색 조건을 유지하기 위한 쿼리스트링 빌드
    public String buildQueryString(SearchFilterDTO dto) {
        StringBuilder sb = new StringBuilder();

        if (dto.getKeyword() != null && !dto.getKeyword().isBlank()) {
            sb.append("&keyword=").append(URLEncoder.encode(dto.getKeyword(), StandardCharsets.UTF_8));
        }
        if (dto.getCategoryId() != null && dto.getCategoryId() > 0) {
            sb.append("&categoryId=").append(dto.getCategoryId());
        }
        if (Boolean.TRUE.equals(dto.getTitleChecked())) sb.append("&titleChecked=true");
        if (Boolean.TRUE.equals(dto.getContentChecked())) sb.append("&contentChecked=true");
        if (Boolean.TRUE.equals(dto.getCommentChecked())) sb.append("&commentChecked=true");
        if (dto.getStartDate() != null) sb.append("&startDate=").append(dto.getStartDate());
        if (dto.getEndDate() != null) sb.append("&endDate=").append(dto.getEndDate());

        return sb.toString();
    }


}
