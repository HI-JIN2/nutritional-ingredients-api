package com.example.nutritional.api.food.domain
import org.springframework.data.jpa.domain.Specification

class FoodSpecifications {
    companion object {
        fun withFilter(
            foodName: String?,
            researchYear: Int?,
            makerName: String?,
            foodCode: String?
        ): Specification<Food> {
            return Specification { root, query, cb ->
                val predicates = mutableListOf<jakarta.persistence.criteria.Predicate>()

                foodName?.trim()?.takeIf { it.isNotEmpty() }?.let {
                    val normalized = it.lowercase()
                    predicates.add(cb.like(cb.lower(root.get("foodName")), "%$normalized%"))
                }
                researchYear?.let {
                    predicates.add(cb.equal(root.get<Int>("researchYear"), it))
                }
                makerName?.trim()?.takeIf { it.isNotEmpty() }?.let {
                    val normalized = it.lowercase()
                    predicates.add(cb.like(cb.lower(root.get("makerName")), "%$normalized%"))
                }
                foodCode?.trim()?.takeIf { it.isNotEmpty() }?.let {
                    predicates.add(cb.equal(root.get<String>("foodCd"), it))
                }

                cb.and(*predicates.toTypedArray())
            }
        }
    }
}
