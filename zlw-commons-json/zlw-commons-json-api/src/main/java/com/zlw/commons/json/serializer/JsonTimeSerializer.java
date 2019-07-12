package com.zlw.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonTimeSerializer extends JsonSerializer<Date> {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    @Override
    public void serialize(Date value, JsonGenerator jgen,SerializerProvider provider) throws IOException,JsonProcessingException {

        String formattedDate = formatter.format(value);
        jgen.writeString(formattedDate);
    }
}
