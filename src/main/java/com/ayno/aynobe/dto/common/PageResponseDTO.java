package com.ayno.aynobe.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "PageResponse")
public class PageResponseDTO<T> {
    private List<T> content;
    private int page;           // 0-base
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
}
