package fr.raksrinana.rsndiscord.api.anilist.data.list;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import java.awt.Color;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public enum MediaListStatus{
	PLANNING(Color.PINK, "Planning"),
	CURRENT(Color.ORANGE, "Current"),
	COMPLETED(Color.GREEN, "Completed"),
	DROPPED(Color.RED, "Dropped"),
	PAUSED(Color.YELLOW, "Paused"),
	REPEATING(Color.BLUE, "Repeating"),
	UNKNOWN(Color.MAGENTA, "Unknown");
	
	private final Color color;
	private final String value;
	
	MediaListStatus(@NotNull Color color, @NotNull String value){
		this.color = color;
		this.value = value;
	}
	
	@JsonCreator
	@NotNull
	public static MediaListStatus getFromName(@NotNull String value){
		return MediaListStatus.valueOf(value);
	}
	
	@Override
	public String toString(){
		return value;
	}
}
