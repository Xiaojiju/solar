package com.dire.core.convert;

import com.dire.core.CommonConstants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CommonConstants.Y_M_D_H_M_S);
            return LocalDateTime.parse(jsonParser.getText(), dateTimeFormatter);
        } catch (Exception e) {
            throw new RuntimeException("属性：" + jsonParser.getCurrentName() + "," + jsonParser.getText() + "需要" + CommonConstants.Y_M_D_H_M_S + "格式");
        }
    }
}
