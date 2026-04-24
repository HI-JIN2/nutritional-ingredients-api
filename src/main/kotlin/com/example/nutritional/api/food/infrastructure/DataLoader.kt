package com.example.nutritional.api.food.infrastructure

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataLoader(
    private val dataLoadService: DataLoadService,
    @Value("\${app.data.load:false}") private val shouldLoad: Boolean,
    @Value("\${app.data.file-path:src/main/resources/data/통합_식품영양성분DB_음식_20230715.csv}") private val filePath: String
) : CommandLineRunner {
    private val logger = org.slf4j.LoggerFactory.getLogger(DataLoader::class.java)

    override fun run(vararg args: String?) {
        if (shouldLoad) {
            logger.info("Starting data load from $filePath...")
            dataLoadService.loadData(filePath)
            logger.info("Data loader finished.")
        }
    }
}
