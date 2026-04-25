package com.example.nutritional.api.global.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

@Schema(description = "페이지네이션 응답 래퍼")
data class PageResponse<T>(
    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    val page: Int,
    @Schema(description = "페이지 크기", example = "20")
    val size: Int,
    @Schema(description = "전체 요소 개수", example = "100")
    val totalElements: Long,
    @Schema(description = "전체 페이지 수", example = "5")
    val totalPages: Int,
    @Schema(description = "다음 페이지 존재 여부", example = "true")
    val hasNext: Boolean,
    @Schema(description = "데이터 목록")
    val content: List<T>
) {
    constructor(pageData: Page<T>) : this(
        page = pageData.number,
        size = pageData.size,
        totalElements = pageData.totalElements,
        totalPages = pageData.totalPages,
        hasNext = pageData.hasNext(),
        content = pageData.content
    )
}
