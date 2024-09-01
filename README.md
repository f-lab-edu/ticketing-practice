# ticketing-practice
공연 예매 연습 웹 애플리케이션

## 기간 / 인원
- 2024.06 ~ 2024.09 / 1인

## 사용 기술
- Java 17, Spring Boot, JPA, MySQL

## 핵심 기능
- 공연 좌석 예매, 예매 취소, 예매 목록 보기

## 경험 및 성과
- SOLID 원칙을 적용해 코드의 유연성, 재사용성, 유지보수성을 높여 시스템 확장이 용이하도록 설계
- Spring Boot와 JPA를 사용하여 RESTful API를 설계하고 Spring REST Docs로 문서화
- MockMvc, Mockito, JUnit을 사용하여 Spring MVC 컨트롤러와 서비스 계층의 단위 및 통합 테스트를 자동화하여 API 동작을 검증하고 문서화
- JaCoCo를 사용하여 테스트 커버리지 80% 이상을 달성하여 코드 품질과 안정성을 보장
- Spring Validation을 활용해 데이터 유효성 검사 및 예외 처리를 구현하여 API 안정성을 강화
- 데이터베이스 작업에서 예외 처리와 트랜잭션 관리를 통해 데이터 무결성을 유지하고, Spring Security로 보안을 강화
- 객체 그래프가 순환 형태라면 Proxy 객체로 인해 발생하는 Entity 직렬화 문제를 DTO로 Response하도록 변경하여 해결. 또한, DTO로 Response하면 Entity의 중요한 필드 노출을 숨길 수 있어 보안을 더욱 강화

## ERD 
![티켓팅 연습 웹 ERD](https://github.com/user-attachments/assets/7817fa2b-f31d-4095-9bd0-83e852542dce)

## API 명세서
https://chisel-sleet-47c.notion.site/3e9211b2596e4e02bd79cc41c83443aa?v=e87b5508f3954f2095fc7791fcd837fa&pvs=4

## Pull Requests

### #1 Maven으로 프로젝트 세팅, 회원가입, 로그인 구현
- ERD 수정 후 엔티티 생성
- 회원가입 처리
- 암호화 처리
- 로그인, 로그아웃 처리

### #3 Gradle로 수정, Spring REST Docs 세팅, User의 API와 기능 구현, User의 테스트 코드 작성
- 회원가입 => POST /api/users
- 모든 회원 검색 => GET /api/users
- 회원 1명 검색 => GET /api/users/{user_id}
- 회원 정보 수정 => PUT /api/users/{user_id}
- 회원 탈퇴 => DELETE /api/users/{user_id}
- 회원 1명이 작성한 게시글 목록 => GET /api/users/{user_id}/articles
- 회원 1명이 작성한 게시글 댓글 목록 => GET /api/users/{user_id}/article-comments
- 회원 1명이 작성한 콘서트 댓글 목록 => GET /api/users/{user_id}/concert-comments
- 회원 1명의 콘서트 찜 목록 => GET /api/users/{user_id}/concert-wishlists
- 회원 1명의 아티스트 찜 목록 => GET /api/users/{user_id}/artist-wishlists
- 회원 1명이 예매한 공연 목록 => GET /api/users/{user_id}/reservations
- 회원 1명이 예매한 공연 1개 취소 => DELETE /api/users/{user_id}/reservations/{reservation_id}

### #5 자코코(JaCoCo)를 활용한 테스트 커버리지 측정
- User의 기능 모두 테스트 커버리지 측정
- 테스트 커버리지 클래스 1개 80%, 나머지는 100% 달성
![테스트 커버리지 100%](https://github.com/user-attachments/assets/3a767376-2130-4e34-96e4-2386e1acad33)

### #7 공연 예매 관련 Entity 기능 구현
- 공연 예매 관련 Entity 목록: 
- [X] Place
- [X] Concert (Place 참조)
- [X] District (Concert 참조)
- [X] Seat (District 참조)
- [X] Reservation (User, Seat 참조)
- Controller의 Response를 Entity에서 Dto로 변경

### #9 하나의 DTO를 요청DTO과 응답DTO 용도로 책임 분산, 공연 예매 관련 기능의 Test Code 작성
- 공연 예매 관련 기능 모두 테스트 커버리지 측정
- 테스트 커버리지 클래스 1개 제외하고 나머지는 100% 달성
![image](https://github.com/user-attachments/assets/f59550d6-a9c1-483b-90af-8cd07399a13d)

- 공연 예매 관련 Service 목록: 
- [x] PlaceService
- [x] ConcertService
- [x] DistrictService
- [x] SeatService
- [x] TicketService
- 공연 예매 관련 Controller 목록: 
- [x] PlaceController
- [x] ConcertController
- [x] DistrictController
- [x] SeatController
- [x] TicketController
- 하나의 XXXDto를 요청(XXXRequest), 응답(XXXResponse)로 책임 분산
- DTO에 Entity 생성 정적 팩터리 메서드를 선언하고, Entity에는 정적 팩터리 메서드 제거
- Reservation에서 Ticket으로 domain 이름 변경

### #11 locust을 활용한 부하테스트 진행, 세부사항 추가 수정
- locust를 활용한 부하테스트 진행
![total_requests_per_second_1725198595 295](https://github.com/user-attachments/assets/9f7f8943-0aa5-469e-b571-7d4dd3a21c46)
- Concert 조회수(hits) 기능 누락됐으므로 추가
- createdAt, updatedAt도 응답 DTO에 추가하기
- User의 birth 변수명을 birthAt으로 변경하고, LocalDate 타입으로 변경
- User의 gender를 enum 타입으로 변경
