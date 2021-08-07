package fr.raksrinana.rsndiscord.settings.impl.guild.birthday;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.raksrinana.rsndiscord.settings.api.ICompositeConfiguration;
import fr.raksrinana.rsndiscord.settings.types.ChannelConfiguration;
import fr.raksrinana.rsndiscord.settings.types.UserConfiguration;
import fr.raksrinana.rsndiscord.utils.json.converter.ISO8601LocalDateDeserializer;
import fr.raksrinana.rsndiscord.utils.json.converter.ISO8601LocalDateSerializer;
import fr.raksrinana.rsndiscord.utils.json.converter.UserConfigurationKeyDeserializer;
import fr.raksrinana.rsndiscord.utils.json.converter.UserConfigurationKeySerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.ofNullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class BirthdaysConfiguration implements ICompositeConfiguration{
	@JsonProperty("birthdays")
	@JsonSerialize(keyUsing = UserConfigurationKeySerializer.class)
	@JsonDeserialize(keyUsing = UserConfigurationKeyDeserializer.class)
	@Getter
	@Setter
	private Map<UserConfiguration, Birthday> birthdays = new HashMap<>();
	@JsonProperty("notificationChannel")
	@Setter
	private ChannelConfiguration notificationChannel;
	@Deprecated
	@JsonProperty("dates")
	@JsonSerialize(keyUsing = UserConfigurationKeySerializer.class, contentUsing = ISO8601LocalDateSerializer.class)
	@JsonDeserialize(keyUsing = UserConfigurationKeyDeserializer.class, contentUsing = ISO8601LocalDateDeserializer.class)
	private final Map<UserConfiguration, LocalDate> dates = new HashMap<>();
	
	public void setBirthday(@NotNull User user, @NotNull LocalDate date){
		birthdays.put(new UserConfiguration(user), new Birthday(date));
	}
	
	public void removeBirthday(@NotNull User user){
		birthdays.remove(new UserConfiguration(user));
	}
	
	@NotNull
	public Optional<Birthday> getBirthday(@NotNull User user){
		return ofNullable(birthdays.get(new UserConfiguration(user)));
	}
	
	@NotNull
	public Optional<ChannelConfiguration> getNotificationChannel(){
		return ofNullable(notificationChannel);
	}
}
