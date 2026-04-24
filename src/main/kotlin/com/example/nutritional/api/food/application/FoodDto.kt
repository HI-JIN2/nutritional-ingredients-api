package com.example.nutritional.api.food.application

import io.swagger.v3.oas.annotations.media.Schema

data class FoodResponse(
    @Schema(description = "ID")
    val id: Long,
    @Schema(description = "식품코드")
    val food_cd: String,
    @Schema(description = "식품군")
    val group_name: String?,
    @Schema(description = "식품이름")
    val food_name: String,
    @Schema(description = "조사년도")
    val research_year: Int?,
    @Schema(description = "지역/제조사")
    val maker_name: String?,
    @Schema(description = "자료출처")
    val ref_name: String?,
    @Schema(description = "1회 제공량")
    val serving_size: String?,
    @Schema(description = "열량(kcal)")
    val calorie: Double?,
    @Schema(description = "탄수화물(g)")
    val carbohydrate: Double?,
    @Schema(description = "단백질(g)")
    val protein: Double?,
    @Schema(description = "지방(g)")
    val fat: Double?,
    @Schema(description = "총당류(g)")
    val sugars: Double?,
    @Schema(description = "나트륨(mg)")
    val sodium: Double?,
    @Schema(description = "콜레스테롤(mg)")
    val cholesterol: Double?,
    @Schema(description = "포화지방산(g)")
    val saturated_fatty_acids: Double?,
    @Schema(description = "트랜스지방(g)")
    val trans_fat: Double?
)

data class FoodCreateRequest(
    val food_cd: String,
    val group_name: String?,
    val food_name: String,
    val research_year: Int?,
    val maker_name: String?,
    val ref_name: String?,
    val serving_size: String?,
    val calorie: Double?,
    val carbohydrate: Double?,
    val protein: Double?,
    val fat: Double?,
    val sugars: Double?,
    val sodium: Double?,
    val cholesterol: Double?,
    val saturated_fatty_acids: Double?,
    val trans_fat: Double?
)

data class FoodUpdateRequest(
    val food_name: String,
    val group_name: String?,
    val research_year: Int?,
    val maker_name: String?,
    val ref_name: String?,
    val serving_size: String?,
    val calorie: Double?,
    val carbohydrate: Double?,
    val protein: Double?,
    val fat: Double?,
    val sugars: Double?,
    val sodium: Double?,
    val cholesterol: Double?,
    val saturated_fatty_acids: Double?,
    val trans_fat: Double?
)
