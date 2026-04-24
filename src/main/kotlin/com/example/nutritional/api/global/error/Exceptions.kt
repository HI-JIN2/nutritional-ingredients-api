package com.example.nutritional.api.global.error

class FoodNotFoundException(id: Long) : RuntimeException("Food not found with id: $id")
class DuplicateFoodCodeException(code: String) : RuntimeException("Duplicate food code: $code")
