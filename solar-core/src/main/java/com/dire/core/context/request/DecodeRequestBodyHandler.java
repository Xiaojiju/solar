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
package com.dire.core.context.request;
/**
 * 请求解密定义
 * @author 一块小饼干
 */
public interface DecodeRequestBodyHandler {

    /**
     * 获取算法类型
     * @return 算法类型，默认转为大写
     */
    String getAlgorithm();

    /**
     * 解密为字符串
     * @param source 加密字符串
     * @return 解字符串
     */
    String getFormat(String source);

    /**
     * 解密为字节数组
     * @param source 加密字符串
     * @return 解密字节数组
     */
    byte[] decode(String source);
}
