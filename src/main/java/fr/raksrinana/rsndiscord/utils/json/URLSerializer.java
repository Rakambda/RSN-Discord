package fr.raksrinana.rsndiscord.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.NonNull;
import java.io.IOException;
import java.net.URL;

public class URLSerializer extends JsonSerializer<URL>{
	@Override
	public void serialize(@NonNull final URL url, @NonNull final JsonGenerator jsonGenerator, final @NonNull SerializerProvider serializerProvider) throws IOException{
		jsonGenerator.writeString(url.toString());
	}
}
