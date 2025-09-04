package dev.noteforge.knowhub.search.dto;

import dev.noteforge.knowhub.common.dto.PageRequestDTO;
import lombok.Data;

@Data
public class SearchRequestWrapper {
    private SearchFilterDTO searchFilterDTO;
    private PageRequestDTO pageRequestDTO;
}