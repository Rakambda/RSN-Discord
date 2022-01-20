package fr.raksrinana.rsndiscord.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import kong.unirest.GenericType;
import kong.unirest.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Type;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.PropertyAccessor.CREATOR;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.fasterxml.jackson.annotation.PropertyAccessor.GETTER;
import static com.fasterxml.jackson.annotation.PropertyAccessor.SETTER;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_TRAILING_COMMA;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS;

public class JacksonObjectMapper implements ObjectMapper{
	private final com.fasterxml.jackson.databind.ObjectMapper mapper;
	
	public JacksonObjectMapper(){
		mapper = JsonMapper.builder()
				.enable(ALLOW_TRAILING_COMMA)
				.enable(ACCEPT_CASE_INSENSITIVE_ENUMS)
				.enable(ALLOW_COMMENTS)
				.visibility(FIELD, ANY)
				.visibility(GETTER, NONE)
				.visibility(SETTER, NONE)
				.visibility(CREATOR, NONE)
				.serializationInclusion(NON_NULL)
				.build();
	}
	
	@Override
	public <T> T readValue(String value, Class<T> valueType){
		try{
			return mapper.readValue(value, valueType);
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public <T> T readValue(String value, GenericType<T> genericType){
		try{
			return mapper.readValue(value, new TypeReference<>(){
				@Override
				public Type getType(){
					return genericType.getType();
				}
			});
		}
		catch(IOException var4){
			throw new RuntimeException(var4);
		}
	}
	
	@Override
	public String writeValue(Object value){
		try{
			return mapper.writeValueAsString(value);
		}
		catch(JsonProcessingException e){
			throw new RuntimeException(e);
		}
	}
}
