package com.boot3.myrestapi.security.userinfo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class UserInfoSerializer extends JsonSerializer<UserInfo>{
@Override
public void serialize(UserInfo userInfo, JsonGenerator gen, 
SerializerProvider serializers) throws IOException {
gen.writeStartObject();
gen.writeNumberField("id", userInfo.getId());
gen.writeStringField("email", userInfo.getEmail());
gen.writeEndObject();
}
}
