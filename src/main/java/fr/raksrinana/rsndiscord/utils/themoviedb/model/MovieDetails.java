package fr.raksrinana.rsndiscord.utils.themoviedb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.raksrinana.rsndiscord.utils.json.ISO8601DateDeserializer;
import fr.raksrinana.rsndiscord.utils.json.URLDeserializer;
import fr.raksrinana.rsndiscord.utils.themoviedb.TMDBUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class MovieDetails{
	@JsonProperty("adult")
	private boolean adult;
	@JsonProperty("backdrop_path")
	private String backdropPath;
	@JsonProperty("belongs_to_collection")
	private Collection belongs_to_collection;
	@JsonProperty("budget")
	private int budget;
	@JsonProperty("genres")
	private Set<Genre> genres;
	@JsonProperty("homepage")
	@JsonDeserialize(using = URLDeserializer.class)
	private URL homepage;
	@JsonProperty("id")
	private int id;
	@JsonProperty("imdb_id")
	private String imdbId;
	@JsonProperty("original_language")
	private String originalLanguage;
	@JsonProperty("original_title")
	private String originalTitle;
	@JsonProperty("overview")
	private String overview;
	@JsonProperty("popularity")
	private long popularity;
	@JsonProperty("poster_path")
	private String posterPath;
	@JsonProperty("production_companies")
	private Set<ProductionCompany> productionCompanies;
	@JsonProperty("production_countries")
	private Set<ProductionCountry> productionCountries;
	@JsonProperty("release_date")
	@JsonDeserialize(using = ISO8601DateDeserializer.class)
	private LocalDate releaseDate;
	@JsonProperty("revenue")
	private int revenue;
	@JsonProperty("runtime")
	private int runtime;
	@JsonProperty("spoken_languages")
	private Set<Language> spokenLanguages;
	@JsonProperty("status")
	private String status;
	@JsonProperty("tagline")
	private String tagline;
	@JsonProperty("title")
	private String title;
	@JsonProperty("video")
	private boolean video;
	@JsonProperty("vote_average")
	private double voteAverage;
	@JsonProperty("vote_count")
	private int voteCount;
	
	@Override
	public int hashCode(){
		return Objects.hash(getId());
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(o == null || getClass() != o.getClass()){
			return false;
		}
		MovieDetails that = (MovieDetails) o;
		return getId() == that.getId();
	}
	
	public Optional<URL> getPosterURL(){
		return Optional.ofNullable(getPosterPath()).map(path -> TMDBUtils.getImageURL(path, "original"));
	}
}
