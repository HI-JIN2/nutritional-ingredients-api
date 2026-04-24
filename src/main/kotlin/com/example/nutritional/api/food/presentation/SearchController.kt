package com.example.nutritional.api.food.presentation

import com.example.nutritional.api.food.application.FoodResponse
import com.example.nutritional.api.food.application.FoodService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
@Tag(name = "Search API", description = "검색 API")
class SearchController(private val foodService: FoodService) {

    @GetMapping
    @Operation(summary = "식품 목록 검색", description = "식품명, 조사년도, 제조사, 식품코드로 검색 및 페이징 지원")
    fun getFoods(
        @RequestParam(required = false) food_name: String?,
        @RequestParam(required = false) research_year: Int?,
        @RequestParam(required = false) maker_name: String?,
        @RequestParam(required = false) food_code: String?,
        pageable: Pageable
    ): Page<FoodResponse> {
        return foodService.getFoods(food_name, research_year, maker_name, food_code, pageable)
    }
}
