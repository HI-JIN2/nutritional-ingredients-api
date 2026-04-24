package com.example.nutritional.api.global.dto

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
) {
    constructor(pageData: Page<T>) : this(
        content = pageData.content,
        page = pageData.number,
        size = pageData.size,
        totalElements = pageData.totalElements,
        totalPages = pageData.totalPages,
        last = pageData.isLast
    )
}
