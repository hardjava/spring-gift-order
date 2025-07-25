# spring-gift-order

## 0️⃣ 단계 - 기본 코드 준비
- 상품 고도화 코드를 옮겨온다

## 1️⃣ 단계 - 카카오 로그인
### 구현 예정 기능 목록
- [x] 카카오 API를 사용하기 위한 애플리케이션 등록 및 설정
- [x] 인가 코드 발급을 위한 카카오 로그인 URL 생성
- [x] 액세스 토큰 요청 기능
  - `RestTemplate`을 이용한 POST 요청
  - 인가 코드를 통한 엑세스 토큰 발급
  - 예외 처리를 위한 `RestTemplateResponseErrorHandler` 구현
- [ ] 테스트 코드 작성
