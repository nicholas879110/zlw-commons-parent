package com.zlw.commons.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class JsonDateDeserializer extends JsonDeserializer<Date> {

    private static final  SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public Date deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String fieldData= parser.getText();
        try {
            return sdf.parse(fieldData);
        } catch (ParseException e) {
           throw new RuntimeException("反序列化日期出错",e);
        }
    }
}
