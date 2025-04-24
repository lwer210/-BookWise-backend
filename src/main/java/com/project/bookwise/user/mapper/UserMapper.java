package com.project.bookwise.user.mapper;

import com.project.bookwise.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    int insertUser(User user);

    User getUserInfo(Long id);

    boolean existsUserInfo(String userId);
}
