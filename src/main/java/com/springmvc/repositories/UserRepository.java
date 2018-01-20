package com.springmvc.repositories;

import com.springmvc.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer> {
	
	User findByUsername(String userName);

}
