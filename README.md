#  spring-product-api

스프링 부트를 활용한 **위시리스트(WishList)** 관리 REST API 프로젝트입니다.
<br><br>
---
## 추가 구현 기능(07.21)
- 생성한 클래스: ProductOptionResponseDto, ProductOptionRequestDto, ProductOption(Entity), OptionService, OptionRepository
- 옵션 기능 추가: product_id를 외래키로 받아서 해당 물품에 해당하는 옵션을 저장하고 불러올 수 있도록 구현. 옵션이 최소한 1개는 존재해야 한다는
요구사항에 맞게 product_option테이블에 초기 데이터를 insert해줘서 진행함.
- quantity를 지정된 숫자만큼 뺄 수 있는 기능을 만들고, 이는 ProductOption 엔티티에 구현을 함.(수량에 대한 예외처리도 엔티티에서 진행.)
- 중복된 옵션이 존재하면 안되기 때문에 JPA의 쿼리 메소드 기능을 이용해 product와 optionId를 받아서 중복처리르 하는 기능을 추가하였음.
- 문자 패턴과 길이 수에 관한 제약조건은 ProductOptionRequestDto에 구현하였음.
- 테스트 코드 구현

## 구현해보고 싶은 기능
- product에 option을 List로 넣어서 product를 get하게 되면 옵션들까지 같이 보이게끔 기능 구현.
- option 자체를 컨트롤러로 분리해서 CRUD를 구성(현재는 ProductController에 save와 get만 구현.)

---

###  상품 목록 조회

- **URL**: `GET /products`
- **설명**: 등록된 모든 상품 목록을 조회합니다.
---

###  상품 단건 조회

- **URL**: `GET /products/{id}`
- **설명**: ID에 해당하는 상품 정보를 조회합니다.
---

###  상품 추가

- **URL**: `POST /products`
- **설명**: 새로운 상품을 등록합니다.
- **요청 바디 예시**:
```json
{
  "name": "초코 케이크",
  "price": 5000,
  "imageUrl": "https://example.com/choco.jpg"
}
```
###  상품 삭제

- **URL**: `DELETE /products/{id}`
- **설명**: 지정한 ID의 상품을 삭제합니다.
---

## 관리자 페이지(Thymeleaf 기반)

### 상품 목록 (홈 화면)

- **URL**: GET /product-page
- **설명**: 관리자용 상품 리스트 페이지(HTML 기반)
---

### 상품 등록 폼

- **URL**: GET /product-page/new   
- **설명**: 새로운 상품을 등록하는 폼 페이지
---

### 상품 수정 폼

- **URL**: GET /product-page/{id}  
- **설명**: 기존 상품 정보를 수정하는 폼 페이지
---

### 상품 삭제 요청

- **URL**: POST /product-page/{id}/delete   
- **설명**: HTML 페이지에서 상품 삭제 요청을 전송합니다

### 기술 스택
Java 21

Spring Boot 3.5.3

Spring Web (REST API)

Spring JPA

Thymeleaf (관리자 페이지용)

H2 Database (in-memory)

JUnit5 (E2E 테스트 코드 작성)

Jwt(Spring Security 사용 X): refreshToken accessToken을 이용한 회원 로그인
-> 미션의 난이도를 고려해 RefreshToken은 명시만 해놓되 이용은 최대한 자제.(멘토님 조언)
