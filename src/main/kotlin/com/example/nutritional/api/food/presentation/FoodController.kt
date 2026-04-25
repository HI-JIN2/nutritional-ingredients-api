package com.example.nutritional.api.food.presentation

import com.example.nutritional.api.food.service.FoodCreateRequest
import com.example.nutritional.api.food.service.FoodResponse
import com.example.nutritional.api.food.service.FoodUpdateRequest
import com.example.nutritional.api.food.service.FoodService
import com.example.nutritional.api.global.common.ApiEndpoints
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ApiEndpoints.FOODS)
@Tag(name = "Food CRUD API", description = "Food Nutritional Ingredients CRUD API")
class FoodController(private val foodService: FoodService) {

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
