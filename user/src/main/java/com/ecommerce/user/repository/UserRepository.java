package com.ecommerce.user.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.user.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
