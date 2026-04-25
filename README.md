# 식품영양성분 API (Nutritional Ingredients API)

이 애플리케이션은 식품영양성분 데이터를 관리하고 검색할 수 있는 RESTful API를 제공합니다.

## 기술 스택

- **Language**: Kotlin 1.9.23
- **Framework**: Spring Boot 3.2.4
- **Database**: PostgreSQL (Production), H2 (Test)
- **DB Migration**: Flyway
- **Library**: Apache POI (Excel Parsing), SpringDoc (OpenAPI/Swagger)
- **Deployment**: Docker, Docker Compose

## 시작하기

### 1. 실행

**옵션 A: Docker Compose를 이용한 실행 (권장)**  
Docker가 설치되어 있어야 합니다. 애플리케이션과 데이터베이스를 한 번에 실행하며, 초기 데이터(CSV)도 자동으로 적재됩니다.

도커 데스크톱을 켠 상태에서 프로젝트 폴더로 이동 후 아래 명령어 입력

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


## 패키지 구조 (DDD 지향)

이 프로젝트는 도메인 중심의 설계를 위해 다음과 같은 패키지 구조를 가집니다:

- `food`
    - `domain`: 엔티티, 리포지토리 인터페이스, 검색 스펙
    - `application`: 서비스(비즈니스 로직), DTO
    - `infrastructure`: 데이터 로더(CSV/Excel), 하위 구현체
    - `presentation`: REST 컨트롤러
- `global`: 에러 핸들링, 공통 설정

## 주요 기능 및 구현 상세

### 데이터 적재 

- **방법**: 애플리케이션 시작 시 `DataLoader`가 활성화되어 있으면 지정된 경로의 파일(Excel)을 읽어 DB에 적재합니다.
- **Excel 파싱**: `Apache POI`를 사용하여 대용량 엑셀 데이터를 처리합니다. (.xlsx, .xls 지원)
- **중복 방지**: `food_cd`를 고유 키로 사용하여 이미 존재하는 데이터는 건너뜁니다.
- **결측치 처리**:
    - 수치형 데이터(열량, 영양소 등)가 비어있는 경우 `0.0`으로 기본값을 설정합니다.
    - 필수값인 `food_name`이 없는 경우 "Unknown"으로 대체하거나 해당 행을 무시합니다.
- **매핑 기준**: Excel의 헤더 이름을 기반으로 Entity의 필드와 매핑합니다.

#### 데이터베이스 마이그레이션 (Flyway)

이 프로젝트는 데이터베이스 스키마 버전 관리를 위해 **Flyway**를 사용합니다.

- **위치**: `src/main/resources/db/migration`
- **파일명 규칙**: `V{Version}__{Description}.sql` (예: `V1__init.sql`)
- **자동 적용**: 애플리케이션 시작 시 미적용된 마이그레이션 파일이 자동으로 실행됩니다.
- **설정**: `spring.jpa.hibernate.ddl-auto: validate` 설정을 통해 Hibernate가 스키마를 직접 수정하지 않고, Flyway가 생성한 스키마를 검증하도록 구성되어 있습니다.

### CRUD API

- RESTful 원칙에 따라 설계되었습니다:
    - `GET /api/v1/foods/{id}`: 상세 조회
    - `POST /api/v1/foods`: 생성
    - `PUT /api/v1/foods/{id}`: 수정
    - `DELETE /api/v1/foods/{id}`: 삭제

### 검색 API 
- `GET /api/v1/search`: 목록 조회 (검색/페이징)

#### 검색 API 세부 구현
사용자 편의성과 데이터의 특성을 고려하여 다음과 같은 검색 전략을 적용하였습니다:

1. **부분 일치**
   - **대상 필드**: 식품명(`foodName`), 제조사(`makerName`)
   - **이유**: 사용자가 전체 명칭을 정확히 모르는 경우가 많으므로 `CONTAINS` 방식의 검색을 지원합니다. (예: "우유" 검색 시 "저지방 우유", "초코우유" 등 포함)

2. **정확 일치**
   - **대상 필드**: 식품코드(`foodCode`), 조사년도(`researchYear`)
   - **이유**: 고유 식별자나 특정 연도 데이터는 정확한 값을 기준으로 필터링하는 것이 효율적입니다.

3. **대소문자 무관**
   - 영문이 포함된 검색어의 경우 영문 대소문자를 구분하지 않고 검색합니다. 내부적으로 모든 검색어와 대상 데이터를 소문자로 변환하여 비교합니다.

4. **공백 및 유효성 처리**
   - **Trim 적용**: 검색어 앞뒤의 불필요한 공백은 자동으로 제거됩니다.
   - **무시 처리**: 빈 문자열("")이나 공백만 있는 검색어가 들어올 경우 검색 조건에서 제외하여 전체 검색이 되도록 안전하게 처리합니다.

5. **성능 최적화**
   - 자주 검색되는 `food_name`과 `food_cd` 컬럼에 데이터베이스 인덱스를 적용하여 대량 데이터 조회 성능을 확보하였습니다.
   - Spring Data JPA의 `Pageable`을 사용하여 필요한 범위의 데이터만 효율적으로 조회합니다.


## 예외 처리

- `GlobalExceptionHandler`를 통해 일관된 오류 응답 형식을 반환합니다.
    - `404 Not Found`: 존재하지 않는 리소스 요청 시
    - `409 Conflict`: 중복된 식품코드 생성 시도 시
    - `400 Bad Request`: 유효하지 않은 요청 데이터 전달 시


## 테스트 실행

```bash
./gradlew test
```
