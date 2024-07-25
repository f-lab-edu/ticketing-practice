package com.ticketingberry.domain.repository.common;

import java.util.List;

import com.ticketingberry.domain.entity.User;

public interface UserRelatedRepository<T> {
	List<T> findByUser(User user);
}
