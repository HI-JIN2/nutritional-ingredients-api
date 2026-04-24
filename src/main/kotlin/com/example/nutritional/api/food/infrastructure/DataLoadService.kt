package com.example.nutritional.api.food.infrastructure

import com.example.nutritional.api.food.domain.Food
import com.example.nutritional.api.food.domain.FoodRepository
import org.apache.commons.csv.CSVFormat
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
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
        } else if (filePath.endsWith(".xlsx", ignoreCase = true) || filePath.endsWith(".xls", ignoreCase = true)) {
            loadFromExcel(file)
        } else {
            logger.error("Unsupported file format: $filePath")
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

    private fun loadFromExcel(file: File) {
        logger.info("Loading data from Excel: ${file.absolutePath}")
        WorkbookFactory.create(file).use { workbook ->
            val sheet = workbook.getSheetAt(0)
            val headerRow = sheet.getRow(0) ?: return
            val columnMap = mapHeaders(headerRow)

            val foods = mutableListOf<Food>()
            var count = 0
            for (i in 1..sheet.lastRowNum) {
                val row = sheet.getRow(i) ?: continue
                val foodCd = getStringValue(row, columnMap["식품코드"]) ?: continue
                
                if (foodRepository.existsByFoodCd(foodCd)) continue

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
                count++

                if (foods.size >= 1000) {
                    foodRepository.saveAll(foods)
                    foods.clear()
                    if (count % 10000 == 0) {
                        logger.info("Processed $count rows...")
                    }
                }
            }
            if (foods.isNotEmpty()) {
                foodRepository.saveAll(foods)
            }
            logger.info("Excel data load completed. Total rows: $count")
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
            cell.stringCellValue.trim()
        } catch (e: Exception) {
            cell.numericCellValue.toLong().toString()
        }
    }

    private fun getIntValue(row: Row, index: Int?): Int? {
        if (index == null) return null
        val cell = row.getCell(index) ?: return null
        return try {
            cell.numericCellValue.toInt()
        } catch (e: Exception) {
            cell.stringCellValue.filter { it.isDigit() }.toIntOrNull()
        }
    }

    private fun getDoubleValue(row: Row, index: Int?): Double? {
        if (index == null) return 0.0
        val cell = row.getCell(index) ?: return 0.0
        return try {
            cell.numericCellValue
        } catch (e: Exception) {
            cell.stringCellValue.filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 0.0
        }
    }
}
