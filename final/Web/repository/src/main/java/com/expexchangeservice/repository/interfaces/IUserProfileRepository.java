package com.expexchangeservice.repository.interfaces;

import com.expexchangeservice.model.entities.User;
import com.expexchangeservice.model.entities.UserProfile;

import java.util.List;

public interface IUserProfileRepository extends IRepository<UserProfile> {
    UserProfile findByUser(User user);
}