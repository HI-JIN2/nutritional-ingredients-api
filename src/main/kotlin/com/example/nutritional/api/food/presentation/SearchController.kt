package com.example.nutritional.api.food.presentation

import com.example.nutritional.api.food.application.FoodResponse
import com.example.nutritional.api.food.application.FoodService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
@Tag(name = "Search API", description = "검색 API")
class SearchController(private val foodService: FoodService) {

    @GetMapping
    @Operation(
        summary = "식품 목록 검색",
        description = "식품명, 조사년도, 제조사, 식품코드로 검색 및 페이징. \\n\\n**[주의] 스웨거 자동완성 값 처리사항**\\n- `sort` 파라미터에 기본으로 들어간 `string` 값을 지우고 `id,desc` 또는 `food_name,asc` 등으로 입력해야 정상 작동합니다. (필요없을 시 항목 자체를 통과시키거나 삭제하세요)\\n- **사용 가능한 정렬 항목 예시**: id, food_name, maker_name, research_year 등"
    )
    fun getFoods(
        @RequestParam(required = false) food_name: String?,
        @RequestParam(required = false) research_year: Int?,
        @RequestParam(required = false) maker_name: String?,
        @RequestParam(required = false) food_code: String?,
        @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable
    ): Page<FoodResponse> {
        return foodService.getFoods(food_name, research_year, maker_name, food_code, pageable)
    }
}
