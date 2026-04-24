package com.example.nutritional.api.food.presentation

import com.example.nutritional.api.food.application.FoodCreateRequest
import com.example.nutritional.api.food.application.FoodService
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FoodApiIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    @Test
    fun `should create and retrieve food`() {
        val request = FoodCreateRequest(
            food_cd = "TEST001",
            group_name = "Test Group",
            food_name = "Test Food",
            research_year = 2023,
            maker_name = "Test Maker",
            ref_name = "Test Ref",
            serving_size = "100g",
            calorie = 100.0,
            carbohydrate = 10.0,
            protein = 5.0,
            fat = 2.0,
            sugars = 1.0,
            sodium = 100.0,
            cholesterol = 0.0,
            saturated_fatty_acids = 0.0,
            trans_fat = 0.0
        )

        mockMvc.perform(
            post("/api/v1/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.food_name").value("Test Food"))

        mockMvc.perform(
            get("/api/v1/search")
                .param("food_name", "Test")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].food_cd").value("TEST001"))
    }

    @Test
    fun `should return 404 for non-existent food`() {
        mockMvc.perform(get("/api/v1/foods/99999"))
            .andExpect(status().isNotFound)
    }
}
