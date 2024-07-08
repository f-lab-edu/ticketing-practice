package com.ticketingberry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ticketingberry.model.entity.Article;
import com.ticketingberry.model.entity.ArticleComment;
import com.ticketingberry.model.entity.ArtistWishlist;
import com.ticketingberry.model.entity.ConcertComment;
import com.ticketingberry.model.entity.ConcertWishlist;
import com.ticketingberry.model.entity.Reservation;
import com.ticketingberry.model.entity.User;
import com.ticketingberry.repository.ArticleCommentRepository;
import com.ticketingberry.repository.ArticleRepository;
import com.ticketingberry.repository.ArtistWishlistRepository;
import com.ticketingberry.repository.ConcertCommentRepository;
import com.ticketingberry.repository.ConcertWishlistRepository;
import com.ticketingberry.repository.ReservationRepository;
import com.ticketingberry.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor	// 'final'이 붙은 필드 중에 초기화 되지 않은 모든 필드를 인자로 설정한 생성자를 만들어 줌
public class UserService {
	private final UserRepository userRepository;
	private final ArticleRepository articleRepository;
	private final ArticleCommentRepository articleCommentRepository;
	private final ConcertCommentRepository concertCommentRepository;
	private final ConcertWishlistRepository concertWishlistRepository;
	private final ArtistWishlistRepository artistWishlistRepository;
	private final ReservationRepository reservationRepository;
	private final PasswordEncoder passwordEncoder;
	
	// 회원 가입
	public User addUser(String username, String password, String nickname, String email, 
						String phone, String birth, String gender, String role) {
		User user = User.builder()
						.username(username)
						.password(passwordEncoder.encode(password))
						.nickname(nickname)
						.email(email)
						.phone(phone)
						.birth(birth)
						.gender(gender)
						.role(role)
						.build();
		userRepository.save(user);
		return user;
	}
	
	// 모든 회원 검색
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	// userId로 회원 검색
	public User getUser(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		User user = null;
		
		if (optionalUser != null) {
			user = optionalUser.get();
		} else {
			throw new IllegalArgumentException("User not found");
		}
		
		return user;
	}
	
	// username으로 회원 검색
	public User getUser(String username) {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		User user = null;
		
		if (optionalUser != null) {
			user = optionalUser.get();
		} else {
			throw new IllegalArgumentException("User not found");
		}
		
		return user;
	}
	
	// 회원 정보 수정
	public User updateUser(User inUser) {
		Optional<User> optionalUser = userRepository.findById(inUser.getId());
		User user = null;
		
		if (optionalUser != null) {
			user = optionalUser.get();
		} else {
			throw new IllegalArgumentException("User not found");
		}
		
		user.setNickname(inUser.getNickname());
		user.setEmail(inUser.getEmail());
		user.setPhone(inUser.getPhone());
		
		User savedUser = userRepository.save(user);
		return savedUser;
	}
	
	// 회원 탈퇴
	public void deleteUser(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		User user = null;
		
		if (optionalUser != null) {
			user = optionalUser.get();
			userRepository.delete(user);
		} else {
			throw new IllegalArgumentException("user not found");
		}
	}
	
	// 회원 1명이 작성한 게시글 목록
	public List<Article> getArticles(User inUser) {
		Optional<User> optionalUser = userRepository.findById(inUser.getId());
		User user = null;
		
		if (optionalUser != null) {
			user = optionalUser.get();
		} else {
			throw new IllegalArgumentException("User not found");
		}
		
		return articleRepository.findByUser(user);
	}
	
	// 회원 1명이 작성한 게시글 댓글 목록
	public List<ArticleComment> getArticleComments(User inUser) {
		Optional<User> optionalUser = userRepository.findById(inUser.getId());
		User user = null;
		
		if (optionalUser != null) {
			user = optionalUser.get();
		} else {
			throw new IllegalArgumentException("User not found");
		}
		
		return articleCommentRepository.findByUser(user);
	}
	
	// 회원 1명이 작성한 콘서트 댓글 목록
	public List<ConcertComment> getConcertComments(User inUser) {
		Optional<User> optionalUser = userRepository.findById(inUser.getId());
		User user = null;
		
		if (optionalUser != null) {
			user = optionalUser.get();
		} else {
			throw new IllegalArgumentException("User not found");
		}
		
		return concertCommentRepository.findByUser(user);
	}
	
	// 회원 1명의 콘서트 찜 목록
	public List<ConcertWishlist> getConcertWishlists(User inUser) {
		Optional<User> optionalUser = userRepository.findById(inUser.getId());
		User user = null;
		
		if (optionalUser != null) {
			user = optionalUser.get();
		} else {
			throw new IllegalArgumentException("User not found");
		}
		
		return concertWishlistRepository.findByUser(user);
	}
	
	// 회원 1명의 아티스트 찜 목록
	public List<ArtistWishlist> getArtistWishlists(User inUser) {
		Optional<User> optionalUser = userRepository.findById(inUser.getId());
		User user = null;
		
		if (optionalUser != null) {
			user = optionalUser.get();
		} else {
			throw new IllegalArgumentException("User not found");
		}
		
		return artistWishlistRepository.findByUser(user);
	}
	
	// 회원 1명이 예매한 공연 목록
	public List<Reservation> getReservations(User inUser) {
		Optional<User> optionalUser = userRepository.findById(inUser.getId());
		User user = null;
		
		if (optionalUser != null) {
			user = optionalUser.get();
		} else {
			throw new IllegalArgumentException("User not found");
		}
		
		return reservationRepository.findByUser(user);
	}
	
	// 회원 1명이 예매한 공연 1개 취소
	public void deleteReservation(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		User user = null;
		
		if (optionalUser != null) {
			user = optionalUser.get();
			userRepository.delete(user);
		} else {
			throw new IllegalArgumentException("User not found");
		}
	}
}





