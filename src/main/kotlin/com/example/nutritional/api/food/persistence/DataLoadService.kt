package com.example.nutritional.api.food.persistence

import com.example.nutritional.api.food.domain.Food
import com.example.nutritional.api.food.domain.FoodRepository
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

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

        if (filePath.endsWith(".xlsx", ignoreCase = true) || filePath.endsWith(".xls", ignoreCase = true)) {
            loadFromExcel(file)
        } else {
            logger.error("Unsupported file format: $filePath. Only Excel (.xlsx, .xls) is supported.")
        }
    }

    private fun loadFromExcel(file: File) {
        logger.info("Loading data from Excel: ${file.absolutePath}")
        
        // Optimize: Fetch all existing codes at once to avoid n+1 queries
        val existingFoodCodes = foodRepository.findAllFoodCodes().toHashSet()
        logger.info("Current existing food codes in DB: ${existingFoodCodes.size}")

        WorkbookFactory.create(file).use { workbook ->
            val sheet = workbook.getSheetAt(0)
            val headerRow = sheet.getRow(0) ?: return
            val columnMap = mapHeaders(headerRow)

            val foods = mutableListOf<Food>()
            var count = 0
            val startedAt = System.currentTimeMillis()

            for (i in 1..sheet.lastRowNum) {
                val row = sheet.getRow(i) ?: continue
                val foodCd = getStringValue(row, columnMap["식품코드"]) ?: continue
                
                // In-memory check instead of DB query
                if (existingFoodCodes.contains(foodCd)) continue

                val food = Food(
                    foodCd = foodCd,
                    groupName = getStringValue(row, columnMap["식품군"] ?: columnMap["DB군"]),
                    foodName = getStringValue(row, columnMap["식품이름"] ?: columnMap["식품명"]) ?: "Unknown",
                    researchYear = getIntValue(row, columnMap["조사년도"] ?: columnMap["연도"]),
                    makerName = getStringValue(row, columnMap["지역/제조사"] ?: columnMap["지역 / 제조사"]),
                    refName = getStringValue(row, columnMap["자료출처"] ?: columnMap["성분표출처"]),
                    servingSize = getStringValue(row, columnMap["1회 제공량"] ?: columnMap["1회제공량"]),
                    calorie = getDoubleValue(row, columnMap["열량(kcal)"] ?: columnMap["에너지(㎉)"]),
                    carbohydrate = getDoubleValue(row, columnMap["탄수화물(g)"]),
                    protein = getDoubleValue(row, columnMap["단백질(g)"]),
                    fat = getDoubleValue(row, columnMap["지방(g)"]),
                    sugars = getDoubleValue(row, columnMap["총당류(g)"]),
                    sodium = getDoubleValue(row, columnMap["나트륨(mg)"] ?: columnMap["나트륨(㎎)"]),
                    cholesterol = getDoubleValue(row, columnMap["콜레스테롤(mg)"] ?: columnMap["콜레스테롤(㎎)"]),
                    saturatedFattyAcids = getDoubleValue(row, columnMap["포화지방산(g)"] ?: columnMap["총 포화 지방산(g)"]),
                    transFat = getDoubleValue(row, columnMap["트랜스지방(g)"] ?: columnMap["트랜스 지방산(g)"])
                )
                foods.add(food)
                existingFoodCodes.add(foodCd) // Update in-memory set to prevent duplicate in same file
                count++

                if (foods.size >= 1000) {
                    foodRepository.saveAll(foods)
                    foods.clear()
                    logger.info("Processed $i rows, Loaded $count new items...")
                }
            }
            if (foods.isNotEmpty()) {
                foodRepository.saveAll(foods)
            }
            val endedAt = System.currentTimeMillis()
            logger.info("Excel data load completed. Total new items: $count. Time taken: ${(endedAt - startedAt) / 1000}s")
        }
    }

    private fun mapHeaders(headerRow: Row): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        for (cell in headerRow) {
            val name = cell.stringCellValue.trim()
            map[name] = cell.columnIndex
        }
        return map
    }

    private fun getStringValue(row: Row, index: Int?): String? {
        if (index == null) return null
        val cell = row.getCell(index) ?: return null
        return try {
            when (cell.cellType) {
                org.apache.poi.ss.usermodel.CellType.STRING -> cell.stringCellValue.trim()
                org.apache.poi.ss.usermodel.CellType.NUMERIC -> cell.numericCellValue.toLong().toString()
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getIntValue(row: Row, index: Int?): Int? {
        if (index == null) return null
        val cell = row.getCell(index) ?: return null
        return try {
            when (cell.cellType) {
                org.apache.poi.ss.usermodel.CellType.NUMERIC -> cell.numericCellValue.toInt()
                org.apache.poi.ss.usermodel.CellType.STRING -> cell.stringCellValue.filter { it.isDigit() }.toIntOrNull()
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getDoubleValue(row: Row, index: Int?): Double? {
        if (index == null) return 0.0
        val cell = row.getCell(index) ?: return 0.0
        return try {
            when (cell.cellType) {
                org.apache.poi.ss.usermodel.CellType.NUMERIC -> cell.numericCellValue
                org.apache.poi.ss.usermodel.CellType.STRING -> cell.stringCellValue.filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 0.0
                else -> 0.0
            }
        } catch (e: Exception) {
            0.0
        }
    }
}
