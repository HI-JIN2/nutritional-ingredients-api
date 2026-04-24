package com.example.nutritional.api.global.error

class FoodNotFoundException(id: Long) : RuntimeException("해당 ID의 식품을 찾을 수 없습니다: $id")
class DuplicateFoodCodeException(code: String) : RuntimeException("이미 존재하는 식품 코드입니다: $code")
