package fr.raksrinana.rsndiscord.api.pandascore.data.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.raksrinana.rsndiscord.api.pandascore.data.game.Game;
import fr.raksrinana.rsndiscord.api.pandascore.data.league.League;
import fr.raksrinana.rsndiscord.api.pandascore.data.opponent.Opponent;
import fr.raksrinana.rsndiscord.api.pandascore.data.opponent.WrappedOpponent;
import fr.raksrinana.rsndiscord.api.pandascore.data.serie.Serie;
import fr.raksrinana.rsndiscord.api.pandascore.data.tournament.Tournament;
import fr.raksrinana.rsndiscord.api.pandascore.data.videogame.VideoGame;
import fr.raksrinana.rsndiscord.api.pandascore.data.videogame.VideoGameVersion;
import fr.raksrinana.rsndiscord.utils.json.converter.ISO8601ZonedDateTimeDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import static fr.raksrinana.rsndiscord.utils.LangUtils.translate;
import static java.util.Optional.ofNullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class RainbowSixMatch{
	private static final ZoneId ZONE_ID = ZoneId.of("Europe/Paris");
	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	@JsonProperty("begin_at")
	@JsonDeserialize(using = ISO8601ZonedDateTimeDeserializer.class)
	@Nullable
	private ZonedDateTime startDate;
	@JsonProperty("detailed_stats")
	private boolean detailedStats;
	@JsonProperty("draw")
	private boolean draw;
	@JsonProperty("end_at")
	@JsonDeserialize(using = ISO8601ZonedDateTimeDeserializer.class)
	@Nullable
	private ZonedDateTime endDate;
	@JsonProperty("forfeit")
	private boolean forfeit;
	@JsonProperty("game_advantage")
	@Nullable
	private Integer gameAdvantageOpponentId;
	@JsonProperty("games")
	@NotNull
	private List<Game> games;
	@JsonProperty("id")
	private int id;
	@JsonProperty("league")
	@NotNull
	private League league;
	@JsonProperty("league_id")
	private int leagueId;
	@JsonProperty("live")
	@NotNull
	private MatchLive live;
	@JsonProperty("match_type")
	@NotNull
	private MatchType matchType;
	@JsonProperty("modified_at")
	@JsonDeserialize(using = ISO8601ZonedDateTimeDeserializer.class)
	@NotNull
	private ZonedDateTime modifyDate;
	@JsonProperty("name")
	@NotNull
	private String name;
	@JsonProperty("number_of_games")
	private int numberOfGames;
	@JsonProperty("opponents")
	@NotNull
	private List<WrappedOpponent<?>> opponents;
	@JsonProperty("original_scheduled_at")
	@JsonDeserialize(using = ISO8601ZonedDateTimeDeserializer.class)
	@Nullable
	private ZonedDateTime originalScheduledAt;
	@JsonProperty("rescheduled")
	private boolean rescheduled = false;
	@JsonProperty("results")
	private List<Result> results;
	@JsonProperty("scheduled_at")
	@JsonDeserialize(using = ISO8601ZonedDateTimeDeserializer.class)
	@Nullable
	private ZonedDateTime scheduledAt;
	@JsonProperty("serie")
	@NotNull
	private Serie serie;
	@JsonProperty("serie_id")
	private int serieId;
	@JsonProperty("slug")
	@Nullable
	private String slug;
	@JsonProperty("status")
	@NotNull
	private MatchStatus status;
	@JsonProperty("streams_list")
	@NotNull
	private List<Stream> streams;
	@JsonProperty("tournament")
	@NotNull
	private Tournament tournament;
	@JsonProperty("tournament_id")
	private int tournamentId;
	@JsonProperty("videogame")
	@NotNull
	private VideoGame videogame;
	@JsonProperty("videogame_version")
	@Nullable
	private VideoGameVersion videogameVersion;
	//There is no type to differenciate them
	// @JsonProperty("winner")
	// @Nullable
	// private Opponent winner;
	@JsonProperty("winner_id")
	@Nullable
	private Integer winnerId;
	
	public void fillEmbed(@NotNull Guild guild, @NotNull EmbedBuilder builder){
		var leagueUrl = getLeague().getImageUrlAsString();
		var serieTier = ofNullable(getSerie().getTier())
				.map(" (%s)"::formatted)
				.orElse("");
		var description = "%s%s - %s".formatted(getLeague().getName(), serieTier, getTournament().getName());
		var streamUrl = getStreams().stream()
				.sorted().findFirst()
				.flatMap(Stream::getUrlAsString)
				.or(() -> getLeague().getUrlAsString())
				.orElse(null);
		var winnerImage = getWinner()
				.flatMap(Opponent::getImageUrlAsString)
				.or(() -> leagueUrl)
				.orElse(null);
		var startDate = ofNullable(getStartDate())
				.or(() -> ofNullable(getScheduledAt()))
				.map(d -> d.withZoneSameInstant(ZONE_ID))
				.map(DF::format)
				.orElse("Unknown");
		var endDate = ofNullable(getEndDate())
				.map(d -> d.withZoneSameInstant(ZONE_ID))
				.map(DF::format);
		var title = getOpponents().stream()
				.map(WrappedOpponent::getOpponent)
				.map(Opponent::getCompleteName)
				.collect(Collectors.joining(" vs "));
		
		builder.setColor(getStatus().getColor())
				.setTimestamp(ZonedDateTime.now())
				.setThumbnail(winnerImage)
				.setFooter(String.valueOf(getId()), leagueUrl.orElse(null))
				.setTitle(title, streamUrl)
				.setDescription(description)
				.addField(translate(guild, "pandascore.r6.match-type"), getMatchType().getValue(), true)
				.addField(translate(guild, "pandascore.r6.started-at"), startDate, true);
		
		endDate.ifPresent(d -> builder.addField(translate(guild, "pandascore.r6.ended-at"), d, true));
		
		builder.addField(translate(guild, "pandascore.r6.score"), scoreAsString(), true);
		
		getWinner().map(Opponent::getCompleteName)
				.ifPresent(w -> builder.addField(translate(guild, "pandascore.r6.winner"), w, true));
		
		builder.addBlankField(false);
		getGames().stream()
				.sorted()
				.forEach(game -> game.fillEmbed(guild, builder, getOpponents()));
	}
	
	@NotNull
	private String scoreAsString(){
		return getResults().stream()
				.map(Result::getScore)
				.map(String::valueOf)
				.collect(Collectors.joining(" - "));
	}
	
	@NotNull
	private Optional<Opponent> getWinner(){
		return Optional.ofNullable(getWinnerId())
				.flatMap(id -> getOpponents().stream()
						.filter(o -> Objects.equals(o.getOpponent().getId(), id))
						.findFirst()
						.map(WrappedOpponent::getOpponent));
	}
}
