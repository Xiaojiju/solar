package com.dire.guard.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dire.guard.SolarSecret;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SecretMapper extends BaseMapper<SolarSecret> {

}
