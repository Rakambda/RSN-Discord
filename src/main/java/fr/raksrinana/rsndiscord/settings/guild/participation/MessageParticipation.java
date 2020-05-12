package fr.raksrinana.rsndiscord.settings.guild.participation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.raksrinana.rsndiscord.settings.AtomicConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.User;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import java.util.Objects;
import static java.time.ZoneOffset.UTC;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class MessageParticipation implements AtomicConfiguration{
	@JsonProperty("day")
	private LocalDate day;
	@JsonProperty("userCounts")
	private Map<Long, Long> userCounts;
	
	public MessageParticipation(LocalDate day){
		this.day = day;
	}
	
	public long incrementUser(User user){
		return this.userCounts.compute(user.getIdLong(), (key, value) -> {
			if(Objects.isNull(value)){
				value = 0L;
			}
			return value + 1;
		});
	}
	
	@Override
	public boolean shouldBeRemoved(){
		return Period.between(day, LocalDate.now(UTC)).getDays() > 15;
	}
}
