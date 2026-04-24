# 식품영양성분 API (Nutritional Ingredients API)

이 애플리케이션은 식품영양성분 데이터를 관리하고 검색할 수 있는 RESTful API를 제공합니다.

## 기술 스택

- **Language**: Kotlin 1.9.23
- **Framework**: Spring Boot 3.2.4
- **Database**: PostgreSQL (Production), H2 (Test)
- **Library**: Apache POI (Excel Parsing), SpringDoc (OpenAPI/Swagger)
- **Deployment**: Docker, Docker Compose

## 시작하기

### 1. 실행

**옵션 A: Docker Compose를 이용한 실행 (권장)**
Docker가 설치되어 있어야 합니다. 애플리케이션과 데이터베이스를 한 번에 실행하며, 초기 데이터(CSV)도 자동으로 적재됩니다.

```bash
docker-compose up --build
```

**옵션 B: 로컬에서 직접 실행하기 (Gradle)**
로컬 환경에 PostgreSQL(포트 5432, 데이터베이스 `nutritional_db`, 계정 `user`/`password`)이 실행 중이어야 합니다.

```bash
./gradlew bootRun
```

### 3. API 문서 확인

애플리케이션이 실행되면 아래 주소에서 Swagger UI를 확인할 수 있습니다:
`http://localhost:8080/swagger-ui.html`

## 주요 기능 및 구현 상세

## 패키지 구조 (DDD 지향)

이 프로젝트는 도메인 중심의 설계를 위해 다음과 같은 패키지 구조를 가집니다:

- `food`
    - `domain`: 엔티티, 리포지토리 인터페이스, 검색 스펙
    - `application`: 서비스(비즈니스 로직), DTO
    - `infrastructure`: 데이터 로더(CSV/Excel), 하위 구현체
    - `presentation`: REST 컨트롤러
- `global`: 에러 핸들링, 공통 설정

### 데이터 적재 (Data Loading)

- **방법**: 애플리케이션 시작 시 `DataLoader`가 활성화되어 있으면 지정된 경로의 파일(CSV 또는 Excel)을 읽어 DB에 적재합니다.
- **CSV 파싱**: `Apache Commons CSV`를 사용하여 대용량 데이터를 스트리밍 방식으로 처리합니다.
- **Excel 파싱**: `Apache POI`를 지원합니다.
- **중복 방지**: `food_cd`를 고유 키로 사용하여 이미 존재하는 데이터는 건너뜁니다.
- **결측치 처리**:
    - 수치형 데이터(열량, 영양소 등)가 비어있는 경우 `0.0`으로 기본값을 설정합니다.
    - 필수값인 `food_name`이 없는 경우 "Unknown"으로 대체하거나 해당 행을 무시합니다.
- **매핑 기준**: Excel의 헤더 이름을 기반으로 Entity의 필드와 매핑합니다.

### 검색 API (Search API)

    - `GET /api/v1/foods`: 목록 조회 (검색/페이징)
        - **필터링**: 식품명(부분 일치), 조사년도(정확 일치), 제조사(부분 일치), 식품코드(정확 일치)를 지원합니다.
        - **페이지네이션**: Spring Data Jpa의 `Pageable`을 사용하여 대량 데이터 조회를 최적화하였습니다.
        - **성능 최적화**: 자주 검색되는 `food_name`과 `food_cd` 컬럼에 인덱스를 적용하였습니다.

### CRUD API

- RESTful 원칙에 따라 설계되었습니다:
    - `GET /api/v1/foods/{id}`: 상세 조회
    - `POST /api/v1/foods`: 생성
    - `PUT /api/v1/foods/{id}`: 수정
    - `DELETE /api/v1/foods/{id}`: 삭제

## 예외 처리 (Error Handling)

- `GlobalExceptionHandler`를 통해 일관된 오류 응답 형식을 반환합니다.
    - `404 Not Found`: 존재하지 않는 리소스 요청 시
    - `409 Conflict`: 중복된 식품코드 생성 시도 시
    - `400 Bad Request`: 유효하지 않은 요청 데이터 전달 시

## 테스트 실행

```bash
./gradlew test
```
