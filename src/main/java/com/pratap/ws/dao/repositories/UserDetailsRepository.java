package com.pratap.ws.dao.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pratap.ws.dao.entities.UserDetailsEntity;
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long> {

	Optional<UserDetailsEntity> findByUserId(String userId);
	
	Optional<UserDetailsEntity> findByEmail(String email);
	
}
