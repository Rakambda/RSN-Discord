package fr.raksrinana.rsndiscord.api.anilist.data.media;

import com.fasterxml.jackson.annotation.*;
import fr.raksrinana.rsndiscord.api.anilist.data.FuzzyDate;
import fr.raksrinana.rsndiscord.api.anilist.data.IAniListObject;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static fr.raksrinana.rsndiscord.utils.LangUtils.translate;
import static java.util.Optional.ofNullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(value = {
		@JsonSubTypes.Type(value = AnimeMedia.class, name = "ANIME"),
		@JsonSubTypes.Type(value = MangaMedia.class, name = "MANGA")
})
@Getter
public abstract class Media implements IAniListObject{
	public static final String QUERY = """
			media {
			    id
			    season
			    type
			    format
			    status
			    episodes
			    chapters
			    volumes
			    genres
			    synonyms
			    isAdult
			    siteUrl
			    source(version: 3)
			    %s
			    %s
			    %s
			    %s
			    %s
			}""".formatted(
			MediaTitle.QUERY,
			FuzzyDate.getQuery("startDate"),
			FuzzyDate.getQuery("endDate"),
			MediaCoverImage.QUERY,
			MediaRank.QUERY
	);
	
	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private final MediaType type;
	
	@JsonProperty("startDate")
	private FuzzyDate startDate = new FuzzyDate();
	@JsonProperty("endDate")
	private FuzzyDate endDate = new FuzzyDate();
	@JsonProperty("genres")
	private Set<String> genres = new HashSet<>();
	@JsonProperty("synonyms")
	private Set<String> synonyms = new HashSet<>();
	@JsonProperty("title")
	private MediaTitle title;
	@JsonProperty("season")
	private MediaSeason season;
	@JsonProperty("format")
	private MediaFormat format;
	@JsonProperty("status")
	private MediaStatus status;
	@JsonProperty("siteUrl")
	private URL url;
	@JsonProperty("coverImage")
	private MediaCoverImage coverImage;
	@JsonProperty("source")
	private MediaSource source;
	@JsonProperty("rankings")
	private Set<MediaRank> rankings = new HashSet<>();
	@JsonProperty("isAdult")
	private boolean isAdult;
	@JsonProperty("id")
	private int id;
	
	protected Media(@NotNull MediaType type){
		this.type = type;
	}
	
	@Override
	public void fillEmbed(@NotNull Guild guild, @NotNull EmbedBuilder builder){
		builder.setDescription(getTitle().getRomaji());
		if(getType().isShouldDisplay()){
			builder.addField(translate(guild, "anilist.type"), getType().toString(), true);
		}
		builder.addField(translate(guild, "anilist.format"), ofNullable(getFormat()).map(Enum::toString).orElse("-"), true)
				.addField(translate(guild, "anilist.status"), ofNullable(getStatus()).map(Enum::toString).orElse("-"), true);
		if(isAdult()){
			builder.addField(translate(guild, "anilist.adult"), "", true);
		}
		
		fillAdditionalEmbed(guild, builder);
		
		getStartDate().asDate().ifPresent(startDate -> builder.addField(translate(guild, "anilist.started"), startDate.format(DF), true));
		getEndDate().asDate().ifPresent(startDate -> builder.addField(translate(guild, "anilist.ended"), startDate.format(DF), true));
		Optional.ofNullable(rankings).stream().flatMap(Collection::stream).forEach(ranking -> ranking.fillEmbed(guild, builder));
		
		if(!genres.isEmpty()){
			builder.addField(translate(guild, "anilist.genres"), String.join(", ", getGenres()), true);
		}
		if(!synonyms.isEmpty()){
			builder.addField(translate(guild, "anilist.synonyms"), String.join(", ", getSynonyms()), true);
		}
		Optional.ofNullable(getSource()).ifPresent(source -> builder.addField(translate(guild, "anilist.source"), source.toString(), true));
		builder.setThumbnail(getCoverImage().getLarge().toString())
				.setFooter("ID: " + getId());
	}
	
	protected abstract void fillAdditionalEmbed(@NotNull Guild guild, @NotNull EmbedBuilder builder);
	
	@NotNull
	public abstract String getProgressType(boolean contains);
	
	@Override
	public int hashCode(){
		return getId();
	}
	
	@Override
	public boolean equals(Object obj){
		return obj instanceof Media && Objects.equals(((Media) obj).getId(), getId());
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public int compareTo(@NotNull IAniListObject o){
		return Integer.compare(getId(), o.getId());
	}
	
	@Nullable
	public abstract Integer getItemCount();
	
	@NotNull
	public static String getQueryWithId(){
		return """
				media(id: $mediaId) {
					id
					season
					type
					format
					status
					episodes
					chapters
					volumes
					genres
					isAdult
					siteUrl
					%s
					%s
					%s
					%s
				}""".formatted(
				MediaTitle.QUERY,
				FuzzyDate.getQuery("startDate"),
				FuzzyDate.getQuery("endDate"),
				MediaCoverImage.QUERY
		);
	}
}
