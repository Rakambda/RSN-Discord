package fr.raksrinana.rsndiscord.settings.impl.guild.reaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.raksrinana.rsndiscord.reaction.ReactionTag;
import fr.raksrinana.rsndiscord.settings.api.IAtomicConfiguration;
import fr.raksrinana.rsndiscord.settings.types.MessageConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class WaitingReactionMessageConfiguration implements IAtomicConfiguration, Comparable<WaitingReactionMessageConfiguration>{
	@JsonProperty("message")
	private MessageConfiguration message;
	@JsonProperty("tag")
	private ReactionTag tag;
	@JsonProperty("data")
	private Map<String, String> data = new HashMap<>();
	
	public WaitingReactionMessageConfiguration(@NotNull Message message, @NotNull ReactionTag tag){
		this(message, tag, null);
	}
	
	public WaitingReactionMessageConfiguration(@NotNull Message message, @NotNull ReactionTag tag, @Nullable Map<String, String> data){
		this(new MessageConfiguration(message), tag, data);
	}
	
	public WaitingReactionMessageConfiguration(@NotNull MessageConfiguration message, @NotNull ReactionTag tag, @Nullable Map<String, String> data){
		this.message = message;
		this.tag = tag;
		this.data = data;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(getMessage(), getTag());
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(o == null || getClass() != o.getClass()){
			return false;
		}
		WaitingReactionMessageConfiguration that = (WaitingReactionMessageConfiguration) o;
		return getMessage().equals(that.getMessage()) && getTag() == that.getTag();
	}
	
	@Override
	public boolean shouldBeRemoved(){
		return getMessage().shouldBeRemoved();
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("message", message)
				.append("tag", tag)
				.append("data", data)
				.toString();
	}
	
	@Override
	public int compareTo(@NotNull WaitingReactionMessageConfiguration o){
		return Long.compare(getMessage().getMessageId(), o.getMessage().getMessageId());
	}
}
