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

@RestController
@RequestMapping(ApiEndpoints.SEARCH)
@Tag(name = "Search API", description = "검색 API")
class SearchController(private val foodService: FoodService) {

    @GetMapping
    @Operation(
        summary = "식품 목록 검색",
        description = "식품명, 조사년도, 제조사, 식품코드로 검색 및 페이징. \\n\\n**[주의] 스웨거 자동완성 값 처리사항**\\n- `sort` 파라미터에 기본으로 들어간 `string` 값을 지우고 `id,desc` 또는 `foodName,asc` 등으로 입력해야 정상 작동합니다. (필요없을 시 항목 자체를 통과시키거나 삭제하세요)\\n- **사용 가능한 정렬 항목 예시**: id, foodName, makerName, researchYear 등"
    )
    fun getFoods(
        @RequestParam(value = "food_name", required = false) foodName: String?,
        @RequestParam(value = "research_year", required = false) researchYear: Int?,
        @RequestParam(value = "maker_name", required = false) makerName: String?,
        @RequestParam(value = "food_code", required = false) foodCode: String?,
        @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable
    ): PageResponse<FoodResponse> {
        val pageData = foodService.getFoods(foodName, researchYear, makerName, foodCode, pageable)
        return PageResponse(pageData)
    }
}
