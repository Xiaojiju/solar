/*
 * Copyright 2022 一块小饼干(莫杨)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dire.guard;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dire.core.Judge;
import com.dire.datasource.BaseModel;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户关联信息
 * @author 一块小饼干
 * @since 1.0.0
 */
@TableName("solar_user_reference")
public class SolarUserReference extends BaseModel<SolarUserReference> implements Serializable {

    @TableId
    private String id;
    private String identifier;
    private String referenceKey;
    private Judge validated;
    private String afterReferenceKey;
    private LocalDateTime expiredTime;
    private LocalDateTime validatedTime;
    private Judge loginAccess;
    private Judge thirdPart;
    private Integer alive;
    private String secretId;
    private String uId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getReferenceKey() {
        return referenceKey;
    }

    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
    }

    public Judge getValidated() {
        return validated;
    }

    public void setValidated(Judge validated) {
        this.validated = validated;
    }

    public String getAfterReferenceKey() {
        return afterReferenceKey;
    }

    public void setAfterReferenceKey(String afterReferenceKey) {
        this.afterReferenceKey = afterReferenceKey;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public LocalDateTime getValidatedTime() {
        return validatedTime;
    }

    public void setValidatedTime(LocalDateTime validatedTime) {
        this.validatedTime = validatedTime;
    }

    public Judge getLoginAccess() {
        return loginAccess;
    }

    public void setLoginAccess(Judge loginAccess) {
        this.loginAccess = loginAccess;
    }

    public Judge getThirdPart() {
        return thirdPart;
    }

    public void setThirdPart(Judge thirdPart) {
        this.thirdPart = thirdPart;
    }

    public Integer getAlive() {
        return alive;
    }

    public void setAlive(Integer alive) {
        this.alive = alive;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
