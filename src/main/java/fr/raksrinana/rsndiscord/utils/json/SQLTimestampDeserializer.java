package fr.raksrinana.rsndiscord.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.NonNull;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SQLTimestampDeserializer extends JsonDeserializer<ZonedDateTime>{
	@Override
	public ZonedDateTime deserialize(@NonNull final JsonParser jsonParser, @NonNull final DeserializationContext deserializationContext) throws IOException{
		return ZonedDateTime.ofInstant(Instant.ofEpochSecond(jsonParser.getValueAsLong()), ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
	}
}
