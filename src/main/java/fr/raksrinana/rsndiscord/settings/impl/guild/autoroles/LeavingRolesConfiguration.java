package fr.raksrinana.rsndiscord.settings.impl.guild.autoroles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.raksrinana.rsndiscord.settings.api.ICompositeConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.ofNullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class LeavingRolesConfiguration implements ICompositeConfiguration{
	@JsonProperty("leavers")
	private final Map<Long, LeaverRoles> leavers = new HashMap<>();
	
	@NotNull
	public Optional<LeaverRoles> getLeaver(@NotNull User user){
		return ofNullable(leavers.get(user.getIdLong()));
	}
	
	public void removeUser(@NotNull User user){
		leavers.remove(user.getIdLong());
	}
	
	public void addLeaver(@NotNull LeaverRoles leaverRoles){
		leavers.put(leaverRoles.getUser().getUserId(), leaverRoles);
	}
}
