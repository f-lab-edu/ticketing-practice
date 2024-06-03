# ticketing-practice

## 프로젝트
공연 티켓 예매 연습 웹 애플리케이션

## 요구사항 (일부)
- 예매가 아직 열리지 않은 공연들을 메인 페이지에 보여준다.
- 예매 날짜가 가까운 공연부터 우선 순위로 보여준다.
- 장소가 같으면 모두 동일한 트래픽을 처리한다.
- 사용자는 좋아하는 아티스트와 공연을 찜할 수 있고, 찜해놓은 아티스트와 공연 목록을 모두 볼 수 있다.
- 공연 제목과 아티스트로 검색을 할 수 있다.
- 아티스트 이름으로 검색하면 해당 아티스트가 출연하는 공연들의 목록을 모두 보여준다.
- 장소로 검색하면 해당 장소에서 진행되는 공연들의 목록을 모두 보여준다.
- 해당 공연을 찜해놓은 회원들의 목록을 모두 보여준다.
- 해당 아티스트를 찜해놓은 회원들의 목록을 모두 보여준다.
- 사용자는 게시판과 댓글을 작성할 수 있다. 댓글은 공연과 게시판 밑에 작성할 수 있다.
- 찜해놓은 아티스트의 공연 예정 정보가 새로 나오면 사용자의 이메일과 휴대폰으로 공연 정보를 보내준다.
- 사용자 프로필, 공연, 게시판에 사진을 삽입할 수 있다.
- 관리자는 개발자 혼자이고, 회원가입을 새로 하는 모든 사람들은 일반 사용자이다.

![티켓팅 연습 웹 ERD](https://github.com/f-lab-edu/ticketing-practice/assets/39743375/e2b358ac-4f8e-45e2-9c65-c9723e57755b)

Table user {
  id integer [primary key]
  img_id integer
  name varchar
  username varchar
  password varchar
  email varchar
  phone varchar
  birth varchar
  gender varchar
  role varchar
  created_date datetime
}

Table artist {
  id integer [primary key]
  user_id integer
  img_id integer
  name varchar
}

Table concert {
  id integer [primary key]
  place_id integer
  artist_id integer
  user_id integer
  img_id integer
  title varchar
  content varchar
  view integer
  opened_ticket_date datetime
  performed_date datetime
  created_date datetime
}

Table place {
  id integer [primary key]
  name varchar
}

Table board {
  id integer [primary key]
  user_id integer
  img_id integer
  title varchar
  content varchar
  view integer
  created_date datetime
  modified_date datetime
}

Table reply {
  id integer [primary key]
  user_id integer
  concert_id integer
  board_id integer
  created_date datetime
  modified_date datetime
}

Table img {
  id integer [primary key]
  name varchar
  data longblob
  compressed_data longblob
}


Ref: concert.user_id <> user.id  // many-to-many
 
Ref: board.user_id > user.id    // many-to-one

Ref: reply.user_id > user.id    // many-to-one

Ref: concert.place_id > place.id  // many-to-one

Ref: reply.concert_id > concert.id  // many-to-one

Ref: reply.board_id > board.id    // many-to-one

Ref: artist.user_id <> user.id    // many-to-many

Ref: concert.artist_id <> artist.id  // many-to-many

Ref: user.img_id - img.id      // one-to-one

Ref: artist.img_id - img.id    // one-to-one

Ref: concert.img_id - img.id   // one-to-one

Ref: board.img_id - img.id     // one-to-one
