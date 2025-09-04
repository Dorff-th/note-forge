package dev.noteforge.knowhub.search.controller;

import dev.noteforge.knowhub.common.dto.PageRequestDTO;
import dev.noteforge.knowhub.common.dto.PageResponseDTO;
import dev.noteforge.knowhub.category.domain.Category;
import dev.noteforge.knowhub.post.service.CategoryService;
import dev.noteforge.knowhub.search.dto.SearchFilterDTO;
import dev.noteforge.knowhub.search.dto.SearchResultDTO;
import dev.noteforge.knowhub.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchApiController {

    private final SearchService searchService;
    private final CategoryService categoryService;

    /**
     * 통합검색 API
     * 예: GET /api/search?keyword=삽질&page=1&size=10&categoryId=1
     */
    @GetMapping("")
    public Map<String, Object> search(
            @ModelAttribute SearchFilterDTO searchFilterDTO,
            @ModelAttribute PageRequestDTO pageRequestDTO
    ) {
        // 기본 검색결과
        PageResponseDTO<SearchResultDTO> result =
                searchService.searchPostsUnified(searchFilterDTO, pageRequestDTO);

        // 카테고리 목록
        List<Category> categories = categoryService.findAllCategory();

        // 쿼리스트링 (프론트에서 페이지 이동 시 활용 가능)
        String queryString = searchService.buildQueryString(searchFilterDTO);

        return Map.of(
                "result", result,
                "categories", categories,
                "searchFilter", searchFilterDTO,
                "queryString", queryString,
                "currentPage", pageRequestDTO.getPage()
        );
    }
}
