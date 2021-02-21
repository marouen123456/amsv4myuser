package com.sip.amsV3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sip.amsV3.entities.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByEmail(String email);
	User findById(int id);
}
