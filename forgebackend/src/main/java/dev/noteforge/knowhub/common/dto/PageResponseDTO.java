package dev.noteforge.knowhub.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResponseDTO<T> {

    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;

    private List<T> dtoList;

    private int currentPage;

    public PageResponseDTO(PageRequestDTO requestDTO, long totalElements, List<T> dtoList, int pageCountToShow) {
        this.page = requestDTO.getPage();
        this.size = requestDTO.getSize();
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.dtoList = dtoList;

        this.currentPage = requestDTO.getPage();  // 1-based

        // 페이지 블럭 사이즈
        int blockSize = 10;

        // 페이지 블럭 계산
        //int tempEnd = (int) (Math.ceil(this.page / (double) pageCountToShow) * pageCountToShow);
        int tempEnd = (int)(Math.ceil(currentPage / (double) blockSize)) * blockSize;
        this.startPage = tempEnd - (blockSize - 1);
        this.endPage = Math.min(tempEnd, totalPages);

        this.prev = this.startPage > 1;
        this.next = this.endPage < totalPages;
    }
}

