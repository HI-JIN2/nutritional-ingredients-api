package com.example.nutritional.api.food.infrastructure

import com.example.nutritional.api.food.domain.Food
import com.example.nutritional.api.food.domain.FoodRepository
import org.apache.commons.csv.CSVFormat
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileReader
import java.nio.charset.Charset

@Service
class DataLoadService(private val foodRepository: FoodRepository) {
    private val logger = org.slf4j.LoggerFactory.getLogger(DataLoadService::class.java)

    @Transactional
    fun loadData(filePath: String) {
        val file = File(filePath)
        if (!file.exists()) {
            logger.warn("Data file not found at $filePath")
            return
        }

        if (filePath.endsWith(".csv", ignoreCase = true)) {
            loadFromCsv(file)
        } else {
            logger.error("Unsupported file format: $filePath. Only .csv is supported.")
        }
    }

    private fun loadFromCsv(file: File) {
        logger.info("Loading data from CSV: ${file.absolutePath}")
        val reader = FileReader(file, Charset.forName("UTF-8"))
        val csvFormat = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreHeaderCase(true)
            .setTrim(true)
            .build()

        val foods = mutableListOf<Food>()
        var count = 0
        csvFormat.parse(reader).use { parser ->
            for (record in parser) {
                val foodCd = record.get("식품코드") ?: continue
                if (foodRepository.existsByFoodCd(foodCd)) continue

                val food = Food(
                    foodCd = foodCd,
                    groupName = record.get("DB군"),
                    foodName = record.get("식품명") ?: "Unknown",
                    researchYear = record.get("연도")?.toIntOrNull(),
                    makerName = record.get("지역 / 제조사"),
                    refName = record.get("성분표출처"),
                    servingSize = record.get("1회제공량"),
                    calorie = record.get("에너지(㎉)")?.toDoubleOrNull() ?: 0.0,
                    carbohydrate = record.get("탄수화물(g)")?.toDoubleOrNull() ?: 0.0,
                    protein = record.get("단백질(g)")?.toDoubleOrNull() ?: 0.0,
                    fat = record.get("지방(g)")?.toDoubleOrNull() ?: 0.0,
                    sugars = record.get("총당류(g)")?.toDoubleOrNull() ?: 0.0,
                    sodium = record.get("나트륨(㎎)")?.toDoubleOrNull() ?: 0.0,
                    cholesterol = record.get("콜레스테롤(㎎)")?.toDoubleOrNull() ?: 0.0,
                    saturatedFattyAcids = record.get("총 포화 지방산(g)")?.toDoubleOrNull() ?: 0.0,
                    transFat = record.get("트랜스 지방산(g)")?.toDoubleOrNull() ?: 0.0
                )
                foods.add(food)
                count++

                if (foods.size >= 1000) {
                    foodRepository.saveAll(foods)
                    foods.clear()
                    logger.info("Processed $count rows...")
                }
            }
            if (foods.isNotEmpty()) {
                foodRepository.saveAll(foods)
            }
        }
        logger.info("CSV data load completed. Total rows: $count")
    }
}
