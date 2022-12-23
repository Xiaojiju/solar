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
package com.dire.core;

import com.dire.core.context.request.DecodeRequestBodyAdvice;
import com.dire.core.convert.LocalDateConverter;
import com.dire.core.convert.LocalDateTimeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * 自动注入Configuration
 * @author 一块小饼干
 */
@Import(value = {
        LocalDateTimeConverter.class,
        LocalDateConverter.class,
        GlobalExceptionHandler.class,
        DecodeRequestBodyAdvice.class
})
@Configuration
public class CoreAutoConfiguration {
}
