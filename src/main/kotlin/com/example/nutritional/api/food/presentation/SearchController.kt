package com.example.nutritional.api.food.presentation

import com.example.nutritional.api.food.application.FoodResponse
import com.example.nutritional.api.food.application.FoodService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import com.example.nutritional.api.global.common.ApiEndpoints
import com.example.nutritional.api.global.dto.PageResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject

@RestController
@RequestMapping(ApiEndpoints.SEARCH)
@Tag(name = "Search API", description = "검색 API")
class SearchController(private val foodService: FoodService) {

    @GetMapping
    @Operation(
        summary = "식품 목록 검색",
        description = "식품명, 조사년도, 제조사, 식품코드로 검색 및 페이징을 지원합니다."
    )
    fun getFoods(
        @RequestParam(value = "food_name", required = false) foodName: String?,
        @RequestParam(value = "research_year", required = false) researchYear: Int?,
        @RequestParam(value = "maker_name", required = false) makerName: String?,
        @RequestParam(value = "food_code", required = false) foodCode: String?,
        @ParameterObject 
        @Parameter(name = "page", example = "0", description = "페이지 번호 (0부터 시작, 기본값: 0)")
        @Parameter(name = "size", example = "20", description = "한 페이지당 조회할 데이터 개수 (20 혹은 50 권장, 기본값: 20)")
        @Parameter(name = "sort", example = "id,asc", description = "정렬 기준. 형식: '필드명,asc|desc'. (예: id,desc 또는 foodName,asc). 가능 필드: id, foodName, makerName, researchYear 등")
        @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable
    ): PageResponse<FoodResponse> {
        val pageData = foodService.getFoods(foodName, researchYear, makerName, foodCode, pageable)
        return PageResponse(pageData)
    }
}
