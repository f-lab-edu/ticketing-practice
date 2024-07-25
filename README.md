# ticketing-practice
공연 예매 연습 웹 애플리케이션

## 기간 / 인원
- 2024.06 ~ 2024.09 / 1인

## 사용 기술
- Java 17, Spring Boot, JPA, MySQL

## 핵심 기능
- 공연 좌석 예매, 예매 취소, 예매 목록 보기

## 경험 및 성과
- Spring Boot와 JPA를 사용하여 RESTful API를 설계하고 Spring REST Docs로 문서화하며 테스트 주도 개발(TDD)을 실천
- MockMvc, Mockito, JUnit을 사용하여 Spring MVC 컨트롤러와 서비스 계층의 단위 및 통합 테스트를 자동화하여 API 동작을 검증하고 문서화
- JaCoCo를 사용하여 테스트 커버리지 80% 이상을 달성하여 코드 품질과 안정성을 보장
- Spring Validation을 활용해 데이터 유효성 검사 및 예외 처리를 구현하여 API 안정성을 강화
- 데이터베이스 작업에서 예외 처리와 트랜잭션 관리를 통해 데이터 무결성을 유지하고, Spring Security로 보안을 강화
- ExceptionHandler로 글로벌 예외 처리를 구현하여 각종 예외 상황에 대해 일관된 응답과 API 가용성을 개선

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

### #5 자코코(JaCoCo)를 활용한 테스트 커버리지 측정, User 외 엔티티 기능 구현 및 테스트 코드 작성
- 자코코(JaCoCo)를 활용한 테스트 커버리지 측정
- 테스트 커버리지 70-80% 이상 목표
- Board, Article, ArticleComment, Artist, ArtistWishlist, Concert, ConcertComment, ConcertWishlist, Place, District, Seat, Reservation, Image 기능 구현 및 테스트 코드 작성
