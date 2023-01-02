package com.dire.guard.mapper;

import com.dire.guard.service.UserFromJdbcImpl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserServiceMapper {

    UserFromJdbcImpl.UserDto selectUserByReferenceKey(@Param("referenceKey") String referenceKey);

}
