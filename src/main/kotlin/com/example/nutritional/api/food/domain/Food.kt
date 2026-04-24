package com.example.nutritional.api.food.domain

import jakarta.persistence.*

@Entity
@Table(name = "foods", indexes = [
    Index(name = "idx_food_name", columnList = "food_name"),
    Index(name = "idx_food_cd", columnList = "food_cd")
])
class Food(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "food_cd", unique = true, nullable = false)
    val foodCd: String,

    @Column(name = "group_name")
    var groupName: String?,

    @Column(name = "food_name", nullable = false)
    var foodName: String,

    @Column(name = "research_year")
    var researchYear: Int?,

    @Column(name = "maker_name")
    var makerName: String?,

    @Column(name = "ref_name")
    var refName: String?,

    @Column(name = "serving_size")
    var servingSize: String?,

    @Column(name = "calorie")
    var calorie: Double? = 0.0,

    @Column(name = "carbohydrate")
    var carbohydrate: Double? = 0.0,

    @Column(name = "protein")
    var protein: Double? = 0.0,

    @Column(name = "fat")
    var fat: Double? = 0.0,

    @Column(name = "sugars")
    var sugars: Double? = 0.0,

    @Column(name = "sodium")
    var sodium: Double? = 0.0,

    @Column(name = "cholesterol")
    var cholesterol: Double? = 0.0,

    @Column(name = "saturated_fatty_acids")
    var saturatedFattyAcids: Double? = 0.0,

    @Column(name = "trans_fat")
    var transFat: Double? = 0.0
) {
    fun update(
        foodName: String,
        groupName: String?,
        researchYear: Int?,
        makerName: String?,
        refName: String?,
        servingSize: String?,
        calorie: Double?,
        carbohydrate: Double?,
        protein: Double?,
        fat: Double?,
        sugars: Double?,
        sodium: Double?,
        cholesterol: Double?,
        saturatedFattyAcids: Double?,
        transFat: Double?
    ) {
        this.foodName = foodName
        this.groupName = groupName
        this.researchYear = researchYear
        this.makerName = makerName
        this.refName = refName
        this.servingSize = servingSize
        this.calorie = calorie
        this.carbohydrate = carbohydrate
        this.protein = protein
        this.fat = fat
        this.sugars = sugars
        this.sodium = sodium
        this.cholesterol = cholesterol
        this.saturatedFattyAcids = saturatedFattyAcids
        this.transFat = transFat
    }
}
