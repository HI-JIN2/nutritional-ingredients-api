package com.example.nutritional.api.food.application

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class FoodResponse(
    @Schema(description = "ID", example = "1")
    val id: Long,
    @Schema(description = "식품코드", example = "D000006")
    val foodCd: String,
    @Schema(description = "식품군", example = "음식")
    val groupName: String?,
    @Schema(description = "식품이름", example = "꿩불고기")
    val foodName: String,
    @Schema(description = "조사년도", example = "2019")
    val researchYear: Int?,
    @Schema(description = "지역/제조사", example = "충주")
    val makerName: String?,
    @Schema(description = "자료출처", example = "외식영양성분자료집 통합본(2012-2017년)")
    val refName: String?,
    @Schema(description = "1회 제공량", example = "500")
    val servingSize: String?,
    @Schema(description = "열량(kcal)", example = "368.8")
    val calorie: Double?,
    @Schema(description = "탄수화물(g)", example = "39.7")
    val carbohydrate: Double?,
    @Schema(description = "단백질(g)", example = "33.5")
    val protein: Double?,
    @Schema(description = "지방(g)", example = "8.5")
    val fat: Double?,
    @Schema(description = "총당류(g)", example = "16.9")
    val sugars: Double?,
    @Schema(description = "나트륨(mg)", example = "1264.31")
    val sodium: Double?,
    @Schema(description = "콜레스테롤(mg)", example = "106.18")
    val cholesterol: Double?,
    @Schema(description = "포화지방산(g)", example = "1.9")
    val saturatedFattyAcids: Double?,
    @Schema(description = "트랜스지방(g)", example = "0.1")
    val transFat: Double?
)

@Schema(description = "식품 등록 요청 DTO")
data class FoodCreateRequest(
    @Schema(description = "식품코드", example = "D000006")
    @field:NotBlank(message = "식품 코드는 필수입니다.")
    val foodCd: String,
    @Schema(description = "식품군", example = "음식")
    val groupName: String?,
    @Schema(description = "식품이름", example = "꿩불고기")
    @field:NotBlank(message = "식품 이름은 필수입니다.")
    val foodName: String,
    @Schema(description = "조사년도", example = "2019")
    val researchYear: Int?,
    @Schema(description = "지역/제조사", example = "충주")
    val makerName: String?,
    @Schema(description = "자료출처", example = "외식영양성분자료집 통합본(2012-2017년)")
    val refName: String?,
    @Schema(description = "1회 제공량", example = "500")
    val servingSize: String?,
    @Schema(description = "열량(kcal)", example = "368.8")
    val calorie: Double?,
    @Schema(description = "탄수화물(g)", example = "39.7")
    val carbohydrate: Double?,
    @Schema(description = "단백질(g)", example = "33.5")
    val protein: Double?,
    @Schema(description = "지방(g)", example = "8.5")
    val fat: Double?,
    @Schema(description = "총당류(g)", example = "16.9")
    val sugars: Double?,
    @Schema(description = "나트륨(mg)", example = "1264.31")
    val sodium: Double?,
    @Schema(description = "콜레스테롤(mg)", example = "106.18")
    val cholesterol: Double?,
    @Schema(description = "포화지방산(g)", example = "1.9")
    val saturatedFattyAcids: Double?,
    @Schema(description = "트랜스지방(g)", example = "0.1")
    val transFat: Double?
)

@Schema(description = "식품 정보 수정 요청 DTO")
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
