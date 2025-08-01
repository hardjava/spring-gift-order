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
- [x] 사용자 로그인 처리
  - ID 토큰 유효성 검증
  - 발급받은 토큰으로 사용자 정보 조회
  - 서비스 회원 정보 확인 또는 가입 처리
- [x] 테스트 코드 작성

## 2️⃣ 단계 - 주문하기
### 구현 예정 기능 목록
- [x] 주문 API 구현 (`POST /api/orders`)
  - [x] 주문 엔티티 생성
  - [x] 상품 Id 및 수량을 입력받아 주문 생성 및 옵션 수량 차감 기능
  - [x] 주문 메시지 작성 가능
  - [x] 주문한 상품이 위시리스트에 있는 경우 자동 삭제 기능
  - [x] 주문 생성 시 주문 내역을 나에게 카카오톡 메시지로 전송 기능
- [x] 테스트 코드 작성

## 3️⃣ 단계 - 배포하기
### 구현 예정 기능 목록
- [x] 자동 배포 설정 (GitHub Actions -> EC2)
  - `step3` 브랜치 push 시, EC2 서버에 SSH 접속하여 다음 작업 수행
    - `application.properties` 재설정
    - `main` 브랜치 pull 및 `hardjava` 브랜치 checkout
    - Gradle 빌드 후 실행 중인 서버 종료
    - 새로 빌드한 JAR 실행
    - 로그 `output.log`로 저장
- [x] CORS 설정
  - 클라이언트와 서버가 서로 다른 Origin을 사용하는 경우 발생할 수 있는 CORS 문제를 해결하기 위해, 서버에 CORS 설정을 적용
    - 모든 경로에 대해 CORS 허용
    - 모든 Origin 허용
    - HTTP 메서드 GET, POST, PUT, DELETE 허용
    - 모든 요청 헤더 허용
