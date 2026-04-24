package com.example.nutritional.api.food.application

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class FoodResponse(
    @Schema(description = "ID")
    val id: Long,
    @Schema(description = "식품코드")
    val foodCd: String,
    @Schema(description = "식품군")
    val groupName: String?,
    @Schema(description = "식품이름")
    val foodName: String,
    @Schema(description = "조사년도")
    val researchYear: Int?,
    @Schema(description = "지역/제조사")
    val makerName: String?,
    @Schema(description = "자료출처")
    val refName: String?,
    @Schema(description = "1회 제공량")
    val servingSize: String?,
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
    val saturatedFattyAcids: Double?,
    @Schema(description = "트랜스지방(g)")
    val transFat: Double?
)

data class FoodCreateRequest(
    @field:NotBlank(message = "식품 코드는 필수입니다.")
    val foodCd: String,
    val groupName: String?,
    @field:NotBlank(message = "식품 이름은 필수입니다.")
    val foodName: String,
    val researchYear: Int?,
    val makerName: String?,
    val refName: String?,
    val servingSize: String?,
    val calorie: Double?,
    val carbohydrate: Double?,
    val protein: Double?,
    val fat: Double?,
    val sugars: Double?,
    val sodium: Double?,
    val cholesterol: Double?,
    val saturatedFattyAcids: Double?,
    val transFat: Double?
)

data class FoodUpdateRequest(
    @field:NotBlank(message = "식품 이름은 필수입니다.")
    val foodName: String,
    val groupName: String?,
    val researchYear: Int?,
    val makerName: String?,
    val refName: String?,
    val servingSize: String?,
    val calorie: Double?,
    val carbohydrate: Double?,
    val protein: Double?,
    val fat: Double?,
    val sugars: Double?,
    val sodium: Double?,
    val cholesterol: Double?,
    val saturatedFattyAcids: Double?,
    val transFat: Double?
)
