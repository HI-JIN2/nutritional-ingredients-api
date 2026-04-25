package com.example.nutritional.api.food.service

import com.example.nutritional.api.food.domain.Food
import com.example.nutritional.api.food.domain.FoodRepository
import com.example.nutritional.api.food.domain.FoodSpecifications
import com.example.nutritional.api.global.error.DuplicateFoodCodeException
import com.example.nutritional.api.global.error.FoodNotFoundException
import com.example.nutritional.api.food.service.dto.FoodCreateRequest
import com.example.nutritional.api.food.service.dto.FoodResponse
import com.example.nutritional.api.food.service.dto.FoodUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FoodService(private val foodRepository: FoodRepository) {

    fun getFoods(
        foodName: String?,
        researchYear: Int?,
        makerName: String?,
        foodCode: String?,
        pageable: Pageable
    ): Page<FoodResponse> {
        val spec = FoodSpecifications.withFilter(foodName, researchYear, makerName, foodCode)
        return foodRepository.findAll(spec, pageable).map { it.toResponse() }
    }

    fun getFood(id: Long): FoodResponse {
        return foodRepository.findById(id).orElseThrow { FoodNotFoundException(id) }.toResponse()
    }

    @Transactional
    fun createFood(request: FoodCreateRequest): FoodResponse {
        if (foodRepository.existsByFoodCd(request.foodCd)) {
            throw DuplicateFoodCodeException(request.foodCd)
        }
        val food = Food(
            foodCd = request.foodCd,
            groupName = request.groupName,
            foodName = request.foodName,
            researchYear = request.researchYear,
            makerName = request.makerName,
            refName = request.refName,
            servingSize = request.servingSize,
            calorie = request.calorie,
            carbohydrate = request.carbohydrate,
            protein = request.protein,
            fat = request.fat,
            sugars = request.sugars,
            sodium = request.sodium,
            cholesterol = request.cholesterol,
            saturatedFattyAcids = request.saturatedFattyAcids,
            transFat = request.transFat
        )
        return foodRepository.save(food).toResponse()
    }

    @Transactional
    fun updateFood(id: Long, request: FoodUpdateRequest): FoodResponse {
        val food = foodRepository.findById(id).orElseThrow { FoodNotFoundException(id) }
        food.update(
            foodName = request.foodName,
            groupName = request.groupName,
            researchYear = request.researchYear,
            makerName = request.makerName,
            refName = request.refName,
            servingSize = request.servingSize,
            calorie = request.calorie,
            carbohydrate = request.carbohydrate,
            protein = request.protein,
            fat = request.fat,
            sugars = request.sugars,
            sodium = request.sodium,
            cholesterol = request.cholesterol,
            saturatedFattyAcids = request.saturatedFattyAcids,
            transFat = request.transFat
        )
        return food.toResponse()
    }

    @Transactional
    fun deleteFood(id: Long) {
        if (!foodRepository.existsById(id)) {
            throw FoodNotFoundException(id)
        }
        foodRepository.deleteById(id)
    }

    private fun Food.toResponse() = FoodResponse(
        id = id!!,
        foodCd = foodCd,
        groupName = groupName,
        foodName = foodName,
        researchYear = researchYear,
        makerName = makerName,
        refName = refName,
        servingSize = servingSize,
        calorie = calorie,
        carbohydrate = carbohydrate,
        protein = protein,
        fat = fat,
        sugars = sugars,
        sodium = sodium,
        cholesterol = cholesterol,
        saturatedFattyAcids = saturatedFattyAcids,
        transFat = transFat
    )
}
