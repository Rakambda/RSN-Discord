package fr.raksrinana.rsndiscord.settings.impl.guild.autoroles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.raksrinana.rsndiscord.settings.api.IAtomicConfiguration;
import fr.raksrinana.rsndiscord.settings.types.RoleConfiguration;
import fr.raksrinana.rsndiscord.settings.types.UserConfiguration;
import fr.raksrinana.rsndiscord.utils.json.converter.ZonedDateTimeDeserializer;
import fr.raksrinana.rsndiscord.utils.json.converter.ZonedDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class LeaverRoles implements IAtomicConfiguration{
	@JsonProperty("user")
	private UserConfiguration user;
	@JsonProperty("roles")
	private Set<RoleConfiguration> roles = new HashSet<>();
	@JsonProperty("leaveDate")
	@JsonDeserialize(using = ZonedDateTimeDeserializer.class)
	@JsonSerialize(using = ZonedDateTimeSerializer.class)
	private ZonedDateTime leaveDate;
	
	public LeaverRoles(@NotNull User user, @NotNull List<Role> roles){
		this.user = new UserConfiguration(user);
		this.roles = roles.stream()
				.map(RoleConfiguration::new)
				.collect(toSet());
		leaveDate = ZonedDateTime.now();
	}
	
	@Override
	public boolean shouldBeRemoved(){
		return false;
	}
}
