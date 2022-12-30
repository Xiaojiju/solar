package com.dire.guard.service;

import com.dire.guard.UserTemplate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserServiceMapper {

    UserTemplate selectUserByReferenceKey(String referenceKey);

}
