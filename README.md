# ticketing-practice

공연 예매 연습 웹 애플리케이션

#1 Maven으로 프로젝트 세팅, 회원가입, 로그인 구현

#3 Gradle로 수정, Spring REST Docs 세팅, User의 API와 기능 구현

회원가입
POST /api/users
모든 회원 검색
GET /api/users
회원 1명 검색
GET /api/users/{user_id}
회원 정보 수정
PUT /api/users/{user_id}
회원 탈퇴
DELETE /api/users/{user_id}
회원 1명이 작성한 게시글 목록
GET /api/users/{user_id}/articles
회원 1명이 작성한 게시글 댓글 목록
GET /api/users/{user_id}/article-comments
회원 1명이 작성한 콘서트 댓글 목록
GET /api/users/{user_id}/concert-comments
회원 1명의 콘서트 찜 목록
GET /api/users/{user_id}/concert-wishlists
회원 1명의 아티스트 찜 목록
GET /api/users/{user_id}/artist-wishlists
회원 1명이 예매한 공연 목록
GET /api/users/{user_id}/reservations
회원 1명이 예매한 공연 1개 취소
DELETE /api/users/{user_id}/reservations/{reservation_id}
