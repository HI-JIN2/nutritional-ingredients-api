package com.example.nutritional.api.food.presentation

import com.example.nutritional.api.food.service.dto.FoodCreateRequest
import com.example.nutritional.api.food.service.FoodService
import com.example.nutritional.api.global.common.ApiEndpoints
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.example.nutritional.api.NutritionalApiApplication

@SpringBootTest(classes = [NutritionalApiApplication::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FoodApiIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    @Test
    fun `식품을 생성하고 검색 조건을 통해 조회할 수 있어야 한다`() {
        val request = FoodCreateRequest(
            foodCd = "TEST001",
            groupName = "Test Group",
            foodName = "Test Food",
            researchYear = 2023,
            makerName = "Test Maker",
            refName = "Test Ref",
            servingSize = "100g",
            calorie = 100.0,
            carbohydrate = 10.0,
            protein = 5.0,
            fat = 2.0,
            sugars = 1.0,
            sodium = 100.0,
            cholesterol = 0.0,
            saturatedFattyAcids = 0.0,
            transFat = 0.0
        )

        mockMvc.perform(
            post(ApiEndpoints.FOODS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.food_name").value("Test Food"))

        mockMvc.perform(
            get(ApiEndpoints.SEARCH)
                .param("food_name", "Test")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].food_cd").value("TEST001"))
    }

    @Test
    fun `존재하지 않는 식품을 조회하면 404를 반환해야 한다`() {
        mockMvc.perform(get("${ApiEndpoints.FOODS}/99999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `중복된 식품 코드로 생성 시도 시 409를 반환해야 한다`() {
        val request = FoodCreateRequest(
            foodCd = "DUP_001",
            foodName = "Original Food",
            groupName = "Group",
            researchYear = 2023,
            makerName = "Maker",
            refName = "Ref",
            servingSize = "100g",
            calorie = 100.0,
            carbohydrate = 10.0,
            protein = 5.0,
            fat = 2.0,
            sugars = 1.0,
            sodium = 100.0,
            cholesterol = 0.0,
            saturatedFattyAcids = 0.0,
            transFat = 0.0
        )

        // 처음 생성 성공
        mockMvc.perform(
            post(ApiEndpoints.FOODS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated)

        // 두번째 생성 시도 (중복)
        mockMvc.perform(
            post(ApiEndpoints.FOODS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isConflict)
    }

    @Test
    fun `잘못된 요청값으로 생성 시도 시 400을 반환해야 한다`() {
        // 필수값인 foodName이 비어있는 잘못된 요청
        val request = FoodCreateRequest(
            foodCd = "BAD_001",
            foodName = "", 
            groupName = "Group",
            researchYear = 2023,
            makerName = "Maker",
            refName = "Ref",
            servingSize = "100g",
            calorie = 100.0,
            carbohydrate = 10.0,
            protein = 5.0,
            fat = 2.0,
            sugars = 1.0,
            sodium = 100.0,
            cholesterol = 0.0,
            saturatedFattyAcids = 0.0,
            transFat = 0.0
        )

        mockMvc.perform(
            post(ApiEndpoints.FOODS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `존재하지 않는 대상을 수정 시도하면 404를 반환해야 한다`() {
        val request = com.example.nutritional.api.food.service.dto.FoodUpdateRequest(
            foodName = "Updated Food",
            groupName = "Group",
            researchYear = 2024,
            makerName = "Maker",
            refName = "Ref",
            servingSize = "100g",
            calorie = 100.0,
            carbohydrate = 10.0,
            protein = 5.0,
            fat = 2.0,
            sugars = 1.0,
            sodium = 100.0,
            cholesterol = 0.0,
            saturatedFattyAcids = 0.0,
            transFat = 0.0
        )

        mockMvc.perform(
            put("${ApiEndpoints.FOODS}/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNotFound)
    }

    @Test
    fun `존재하지 않는 대상을 삭제 시도하면 404를 반환해야 한다`() {
        mockMvc.perform(delete("${ApiEndpoints.FOODS}/99999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `쿼리 파라미터 타입이 맞지 않으면 400을 반환해야 한다`() {
        mockMvc.perform(
            get(ApiEndpoints.SEARCH)
                .param("research_year", "not_a_number")
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("research_year 파라미터의 타입이 잘못되었습니다. (기대 타입: Int)"))
    }

    @Test
    fun `잘못된 형식의 JSON을 보내면 400을 반환해야 한다`() {
        mockMvc.perform(
            post(ApiEndpoints.FOODS)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"food_cd\": \"TEST\", \"food_name\": ") // 끊긴 JSON
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("요청 본문 형식이 잘못되었습니다."))
    }

    @Test
    fun `지원하지 않는 HTTP 메서드로 요청하면 405를 반환해야 한다`() {
        mockMvc.perform(
            post("${ApiEndpoints.SEARCH}") // SEARCH는 GET만 지원
        ).andExpect(status().isMethodNotAllowed)
            .andExpect(jsonPath("$.message").value("지원하지 않는 HTTP 메서드입니다: POST"))
    }
}
