package fr.mrcraftcod.gunterdiscord.settings.newConfigs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.mrcraftcod.gunterdiscord.Main;
import fr.mrcraftcod.gunterdiscord.utils.json.SQLTimestampDeserializer;
import fr.mrcraftcod.gunterdiscord.utils.json.SQLTimestampSerializer;
import net.dv8tion.jda.api.entities.User;
import javax.annotation.Nonnull;
import java.util.Date;
import java.util.Optional;

/**
 * Created by mrcraftcod (MrCraftCod - zerderr@gmail.com) on 2019-06-23.
 *
 * @author Thomas Couchoud
 * @since 2019-06-23
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDateConfiguration implements NewConfiguration{
	@JsonProperty("userId")
	private long userId;
	@JsonProperty
	@JsonDeserialize(using = SQLTimestampDeserializer.class)
	@JsonSerialize(using = SQLTimestampSerializer.class)
	private Date date;
	
	@Nonnull
	public Date getDate(){
		return this.date;
	}
	
	@Nonnull
	public Optional<User> getUser(){
		return Optional.ofNullable(Main.getJDA().getUserById(this.getUserId()));
	}
	
	public long getUserId(){
		return this.userId;
	}
}
