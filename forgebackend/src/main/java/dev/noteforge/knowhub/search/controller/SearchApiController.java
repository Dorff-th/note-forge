
package dev.noteforge.knowhub.search.controller;

import dev.noteforge.knowhub.common.dto.PageRequestDTO;
import dev.noteforge.knowhub.common.dto.PageResponseDTO;
import dev.noteforge.knowhub.category.domain.Category;
import dev.noteforge.knowhub.post.service.CategoryService;
import dev.noteforge.knowhub.search.dto.SearchFilterDTO;
import dev.noteforge.knowhub.search.dto.SearchRequestWrapper;
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
    @PostMapping("")
    public Map<String, Object> search(@RequestBody SearchRequestWrapper wrapper) {
        SearchFilterDTO searchFilterDTO = wrapper.getSearchFilterDTO();
        PageRequestDTO pageRequestDTO = wrapper.getPageRequestDTO();

        // 검색 실행
        PageResponseDTO<SearchResultDTO> result =
                searchService.searchPostsUnified(searchFilterDTO, pageRequestDTO);

        List<Category> categories = categoryService.findAllCategory();
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
