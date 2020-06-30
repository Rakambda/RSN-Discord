package fr.raksrinana.rsndiscord.utils.trakt.model.users.history;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.raksrinana.rsndiscord.utils.json.ISO8601LocalDateDeserializer;
import fr.raksrinana.rsndiscord.utils.json.ISO8601ZonedDateTimeDeserializer;
import fr.raksrinana.rsndiscord.utils.json.URLDeserializer;
import fr.raksrinana.rsndiscord.utils.themoviedb.model.MovieDetails;
import fr.raksrinana.rsndiscord.utils.trakt.TraktObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.dv8tion.jda.api.EmbedBuilder;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import static fr.raksrinana.rsndiscord.utils.LangUtils.translate;
import static fr.raksrinana.rsndiscord.utils.trakt.model.users.history.UserHistory.DATE_FORMAT;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
public class Movie implements TraktObject{
	@JsonProperty("title")
	private String title;
	@JsonProperty("year")
	private int year;
	@JsonProperty("ids")
	private MediaIds ids;
	@JsonProperty("tagline")
	private String tagline;
	@JsonProperty("overview")
	private String overview;
	@JsonProperty("released")
	@JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
	private LocalDate released;
	@JsonProperty("runtime")
	private int runtime;
	@JsonProperty("country")
	private String country;
	@JsonProperty("trailer")
	@JsonDeserialize(using = URLDeserializer.class)
	private URL trailer;
	@JsonProperty("homepage")
	@JsonDeserialize(using = URLDeserializer.class)
	private URL homepage;
	@JsonProperty("status")
	private String status;
	@JsonProperty("rating")
	private double rating;
	@JsonProperty("votes")
	private int votes;
	@JsonProperty("comment_count")
	private int commentCount;
	@JsonProperty("updated_at")
	@JsonDeserialize(using = ISO8601ZonedDateTimeDeserializer.class)
	private ZonedDateTime updatedAt;
	@JsonProperty("language")
	private String language;
	@JsonProperty("genres")
	private Set<String> genres;
	
	@Override
	public void fillEmbed(@NonNull Locale locale, @NonNull EmbedBuilder builder){
		fillEmbed(locale, builder, null);
	}
	
	public void fillEmbed(@NonNull Locale locale, @NonNull EmbedBuilder builder, MovieDetails movieDetails){
		builder.addField(translate(locale, "trakt.title"), getTitle(), true);
		builder.addField(translate(locale, "trakt.year"), Integer.toString(this.getYear()), true);
		builder.addField(translate(locale, "trakt.status"), this.getStatus(), true);
		builder.addField(translate(locale, "trakt.aired"), this.getReleased().format(DATE_FORMAT), true);
		builder.addField(translate(locale, "trakt.genres"), String.join(", ", this.getGenres()), true);
		builder.addField(translate(locale, "trakt.overview"), this.getOverview(), false);
	}
	
	@Override
	public URL getUrl(){
		return this.getTrailer();
	}
	
	@Override
	public int compareTo(@NonNull TraktObject o){
		if(o instanceof Movie){
			return getTitle().compareTo(((Movie) o).getTitle());
		}
		return 0;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(getIds());
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(o == null || getClass() != o.getClass()){
			return false;
		}
		Movie movie = (Movie) o;
		return Objects.equals(getIds(), movie.getIds());
	}
}
