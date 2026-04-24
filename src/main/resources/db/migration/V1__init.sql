CREATE TABLE foods (
    id BIGSERIAL PRIMARY KEY,
    food_cd VARCHAR(255) NOT NULL UNIQUE,
    group_name VARCHAR(255),
    food_name VARCHAR(255) NOT NULL,
    research_year INTEGER,
    maker_name VARCHAR(255),
    ref_name VARCHAR(255),
    serving_size VARCHAR(255),
    calorie DOUBLE PRECISION DEFAULT 0.0,
    carbohydrate DOUBLE PRECISION DEFAULT 0.0,
    protein DOUBLE PRECISION DEFAULT 0.0,
    fat DOUBLE PRECISION DEFAULT 0.0,
    sugars DOUBLE PRECISION DEFAULT 0.0,
    sodium DOUBLE PRECISION DEFAULT 0.0,
    cholesterol DOUBLE PRECISION DEFAULT 0.0,
    saturated_fatty_acids DOUBLE PRECISION DEFAULT 0.0,
    trans_fat DOUBLE PRECISION DEFAULT 0.0
);

CREATE INDEX idx_food_name ON foods(food_name);
CREATE INDEX idx_food_cd ON foods(food_cd);
