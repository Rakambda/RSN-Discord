package fr.raksrinana.rsndiscord.utils.overwatch.year2019.stage.week;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.raksrinana.rsndiscord.utils.json.ZonedDateTimeDeserializer;
import fr.raksrinana.rsndiscord.utils.overwatch.year2019.stage.match.OverwatchMatch;
import fr.raksrinana.rsndiscord.utils.overwatch.year2019.stage.week.event.OverwatchEvent;
import lombok.Getter;
import lombok.NonNull;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class OverwatchWeek implements Comparable<OverwatchWeek>{
	@JsonProperty("id")
	private int id;
	@JsonProperty("startDate")
	@JsonDeserialize(using = ZonedDateTimeDeserializer.class)
	private ZonedDateTime startDate;
	@JsonProperty("endDate")
	@JsonDeserialize(using = ZonedDateTimeDeserializer.class)
	private ZonedDateTime endDate;
	@JsonProperty("name")
	private String name;
	@JsonProperty("matches")
	private final List<OverwatchMatch> matches = new LinkedList<>();
	@JsonProperty("events")
	private List<OverwatchEvent> events;
	
	public boolean hasEnded(){
		return this.getMatches().stream().allMatch(OverwatchMatch::hasEnded);
	}
	
	public boolean hasStarted(){
		return this.getMatches().stream().anyMatch(OverwatchMatch::hasStarted);
	}
	
	@Override
	public int compareTo(@NonNull final OverwatchWeek overwatchWeek){
		return this.getStartDate().compareTo(overwatchWeek.getStartDate());
	}
	
	@Override
	public boolean equals(final Object obj){
		return obj instanceof OverwatchWeek && Objects.equals(this.getId(), ((OverwatchWeek) obj).getId());
	}
	
	public Optional<OverwatchMatch> getCurrentMatch(){
		return this.getMatches().stream().filter(w -> !w.hasEnded()).filter(OverwatchMatch::hasStarted).sorted().findFirst();
	}
	
	public Optional<OverwatchMatch> getNextMatch(){
		return this.getMatches().stream().filter(s -> !s.hasStarted()).sorted().findFirst();
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(id);
	}
	
	@Override
	public String toString(){
		return this.getName();
	}
}
