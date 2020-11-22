package fr.raksrinana.rsndiscord.modules.anilist.data.list;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.raksrinana.rsndiscord.modules.anilist.data.FuzzyDate;
import fr.raksrinana.rsndiscord.modules.anilist.data.IAniListDatedObject;
import fr.raksrinana.rsndiscord.modules.anilist.data.IAniListObject;
import fr.raksrinana.rsndiscord.modules.anilist.data.media.IMedia;
import fr.raksrinana.rsndiscord.modules.anilist.data.media.MangaMedia;
import fr.raksrinana.rsndiscord.utils.json.SQLTimestampDeserializer;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import static fr.raksrinana.rsndiscord.utils.LangUtils.translate;
import static java.lang.Boolean.TRUE;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class MediaList implements IAniListDatedObject{
	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	@Getter
	private static final String QUERY = """
			mediaList(userId: $userID) {
			    id
			    private
			    progress
			    progressVolumes
			    priority
			    customLists
			    score(format: POINT_100)
			    status
			    updatedAt
			    createdAt
			    repeat
			    notes
			    %s
			    %s
			    %s
			}""".formatted(
			FuzzyDate.getQuery("startedAt"),
			FuzzyDate.getQuery("completedAt"),
			IMedia.getQUERY()
	);
	@JsonProperty("status")
	private final MediaListStatus status = MediaListStatus.UNKNOWN;
	@JsonProperty("private")
	private final boolean privateItem = false;
	@JsonProperty("startedAt")
	private final FuzzyDate startedAt = new FuzzyDate();
	@JsonProperty("completedAt")
	private final FuzzyDate completedAt = new FuzzyDate();
	@JsonProperty("customLists")
	private final Map<String, Boolean> customLists = new HashMap<>();
	@JsonProperty("id")
	private int id;
	@JsonProperty("media")
	private IMedia media;
	@JsonProperty("priority")
	private Integer priority;
	@JsonProperty("progress")
	private Integer progress;
	@JsonProperty("progressVolumes")
	private Integer progressVolumes;
	@JsonProperty("createdAt")
	@JsonDeserialize(using = SQLTimestampDeserializer.class)
	private ZonedDateTime createdAt;
	@JsonProperty("updatedAt")
	@JsonDeserialize(using = SQLTimestampDeserializer.class)
	private ZonedDateTime updatedAt;
	@JsonProperty("score")
	private Integer score;
	@JsonProperty("repeat")
	private Integer repeat;
	@JsonProperty("notes")
	private String notes;
	
	@Override
	public void fillEmbed(@NonNull Guild guild, @NonNull final EmbedBuilder builder){
		builder.setTimestamp(getDate())
				.setColor(getStatus().getColor())
				.setTitle(translate(guild, "anilist.list-info"), getMedia().getUrl().toString())
				.addField(translate(guild, "anilist.list-status"), this.getStatus().toString(), true);
		if(nonNull(getScore())){
			builder.addField(translate(guild, "anilist.list-score"), this.getScore() + "/100", true);
		}
		if(Objects.equals(isPrivateItem(), TRUE)){
			builder.addField(translate(guild, "anilist.list-private"), "Yes", true);
		}
		
		getStartedAt().asDate().ifPresent(date -> builder.addField(translate(guild, "anilist.list-started"), date.format(DF), true));
		getCompletedAt().asDate().ifPresent(date -> {
			builder.addField(translate(guild, "anilist.list-complete"), date.format(DF), true);
			getStartedAt().durationTo(date).ifPresent(duration -> builder.addField(translate(guild, "anilist.list-time"), String.format("%d days", duration.get(DAYS)), true));
		});
		
		builder.addField(translate(guild, "anilist.list-progress"), getProgress() + "/" + ofNullable(getMedia().getItemCount()).map(Object::toString).orElse("?"), true);
		if(nonNull(getProgressVolumes()) && getMedia() instanceof MangaMedia){
			builder.addField(translate(guild, "anilist.list-volumes"), getProgressVolumes() + "/" + ofNullable(((MangaMedia) getMedia()).getVolumes()).map(Object::toString).orElse("?"), true);
		}
		if(nonNull(getRepeat()) && getRepeat() > 0){
			builder.addField(translate(guild, "anilist.list-repeat"), Integer.toString(this.getRepeat()), true);
		}
		
		final var lists = this.customLists.entrySet().stream()
				.filter(k -> nonNull(k.getValue()) && k.getValue())
				.map(Map.Entry::getKey)
				.collect(joining(", "));
		if(!lists.isBlank()){
			builder.addField(translate(guild, "anilist.list-custom"), lists, true);
		}
		if(nonNull(getNotes()) && !getNotes().isBlank()){
			builder.addField(translate(guild, "anilist.list-notes"), getNotes(), false);
		}
		builder.addBlankField(false);
		getMedia().fillEmbed(guild, builder);
	}
	
	@Override
	@NonNull
	public ZonedDateTime getDate(){
		return this.getUpdatedAt();
	}
	
	@Override
	@NonNull
	public URL getUrl(){
		return getMedia().getUrl();
	}
	
	@Override
	public int hashCode(){
		return this.getId();
	}
	
	@Override
	public boolean equals(final Object obj){
		return obj instanceof MediaList && Objects.equals(((MediaList) obj).getId(), getId());
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public int compareTo(@NonNull final IAniListObject o){
		if(o instanceof IAniListDatedObject){
			return getDate().compareTo(((IAniListDatedObject) o).getDate());
		}
		return Integer.compare(getId(), o.getId());
	}
}
