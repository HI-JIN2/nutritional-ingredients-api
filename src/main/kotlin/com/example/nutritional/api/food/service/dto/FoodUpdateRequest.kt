package com.example.nutritional.api.food.service.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "식품 정보 수정 요청 DTO")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class FoodUpdateRequest(
    @Schema(description = "식품이름", example = "수정된 꿩불고기")
    @field:NotBlank(message = "식품 이름은 필수입니다.")
    val foodName: String,
    @Schema(description = "식품군", example = "조리식품")
    val groupName: String?,
    @Schema(description = "조사년도", example = "2024")
    val researchYear: Int?,
    @Schema(description = "지역/제조사", example = "충주시")
    val makerName: String?,
    @Schema(description = "자료출처", example = "2024년 영양자료집")
    val refName: String?,
    @Schema(description = "1회 제공량", example = "600")
    val servingSize: String?,
    @Schema(description = "열량(kcal)", example = "400.0")
    val calorie: Double?,
    @Schema(description = "탄수화물(g)", example = "40.0")
    val carbohydrate: Double?,
    @Schema(description = "단백질(g)", example = "35.0")
    val protein: Double?,
    @Schema(description = "지방(g)", example = "10.0")
    val fat: Double?,
    @Schema(description = "총당류(g)", example = "15.0")
    val sugars: Double?,
    @Schema(description = "나트륨(mg)", example = "1300.0")
    val sodium: Double?,
    @Schema(description = "콜레스테롤(mg)", example = "110.0")
    val cholesterol: Double?,
    @Schema(description = "포화지방산(g)", example = "2.0")
    val saturatedFattyAcids: Double?,
    @Schema(description = "트랜스지방(g)", example = "0.2")
    val transFat: Double?
)
