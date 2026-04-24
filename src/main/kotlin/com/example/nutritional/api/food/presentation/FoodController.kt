package com.example.nutritional.api.food.presentation

import com.example.nutritional.api.food.application.FoodCreateRequest
import com.example.nutritional.api.food.application.FoodResponse
import com.example.nutritional.api.food.application.FoodUpdateRequest
import com.example.nutritional.api.food.application.FoodService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/foods")
@Tag(name = "Food API", description = "Food Nutritional Ingredients API")
class FoodController(private val foodService: FoodService) {

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

    @GetMapping("/{id}")
    @Operation(summary = "식품 상세 조회")
    fun getFood(@PathVariable id: Long): FoodResponse {
        return foodService.getFood(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "식품 생성")
    fun createFood(@RequestBody @Valid request: FoodCreateRequest): FoodResponse {
        return foodService.createFood(request)
    }

    @PutMapping("/{id}")
    @Operation(summary = "식품 정보 수정")
    fun updateFood(
        @PathVariable id: Long,
        @RequestBody @Valid request: FoodUpdateRequest
    ): FoodResponse {
        return foodService.updateFood(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "식품 삭제")
    fun deleteFood(@PathVariable id: Long) {
        foodService.deleteFood(id)
    }
}
