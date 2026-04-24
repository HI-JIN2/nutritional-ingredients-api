package com.example.nutritional.api.food.domain
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FoodRepository : JpaRepository<Food, Long>, JpaSpecificationExecutor<Food> {
    fun findByFoodCd(foodCd: String): Optional<Food>
    fun existsByFoodCd(foodCd: String): Boolean
}
