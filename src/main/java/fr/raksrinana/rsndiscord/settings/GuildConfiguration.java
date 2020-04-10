package fr.raksrinana.rsndiscord.settings;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.raksrinana.rsndiscord.settings.guild.*;
import fr.raksrinana.rsndiscord.settings.types.CategoryConfiguration;
import fr.raksrinana.rsndiscord.settings.types.ChannelConfiguration;
import fr.raksrinana.rsndiscord.settings.types.RoleConfiguration;
import fr.raksrinana.rsndiscord.settings.types.UserRoleConfiguration;
import fr.raksrinana.rsndiscord.utils.reaction.ReactionTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class GuildConfiguration implements CompositeConfiguration{
	@JsonProperty("schedules")
	@JsonAlias({"reminders"})
	private final List<ScheduleConfiguration> schedules = new ArrayList<>();
	@JsonProperty("prefix")
	@Setter
	private String prefix;
	@JsonProperty("aniList")
	@Getter
	private final AniListConfiguration aniListConfiguration = new AniListConfiguration();
	@JsonProperty("trakt")
	@Getter
	private final TraktConfiguration traktConfiguration = new TraktConfiguration();
	@JsonProperty("autoRoles")
	@Getter
	@Setter
	private Set<RoleConfiguration> autoRoles = new HashSet<>();
	@JsonProperty("moderatorRoles")
	@Getter
	@Setter
	private Set<RoleConfiguration> moderatorRoles = new HashSet<>();
	@JsonProperty("djRole")
	@Setter
	private RoleConfiguration djRole;
	@JsonProperty("warns")
	@Getter
	private final WarnsConfiguration warnsConfiguration = new WarnsConfiguration();
	@JsonProperty("ideaChannels")
	@Getter
	@Setter
	private Set<ChannelConfiguration> ideaChannels = new HashSet<>();
	@JsonProperty("npXpChannels")
	@Getter
	@Setter
	private Set<ChannelConfiguration> noXpChannels = new HashSet<>();
	@JsonProperty("participation")
	@Getter
	private final ParticipationConfig participationConfig = new ParticipationConfig();
	@JsonProperty("reportChannel")
	@Setter
	private ChannelConfiguration reportChannel;
	@JsonProperty("nickname")
	@Getter
	private final NicknameConfiguration nicknameConfiguration = new NicknameConfiguration();
	@JsonProperty("ircForward")
	@Getter
	@Deprecated
	private final boolean ircForward = false;
	@JsonProperty("quizChannel")
	@Setter
	private ChannelConfiguration quizChannel;
	@JsonProperty("questions")
	@Getter
	private final QuestionsConfiguration questionsConfiguration = new QuestionsConfiguration();
	@JsonProperty("guildId")
	@Getter
	private long guildId;
	@JsonProperty("trombinoscope")
	@Getter
	private final TrombinoscopeConfiguration trombinoscopeConfiguration = new TrombinoscopeConfiguration();
	@JsonProperty("addBackRoles")
	@Getter
	private final Set<UserRoleConfiguration> addBackRoles = new HashSet<>();
	@JsonProperty("leaverRole")
	@Setter
	private RoleConfiguration leaverRole;
	@JsonProperty("poopRole")
	@Setter
	private RoleConfiguration poopRole;
	@JsonProperty("twitchAutoConnectUsers")
	@Getter
	@Deprecated
	private final Set<String> twitchAutoConnectUsers = new HashSet<>();
	@JsonProperty("overwatchLeague")
	@Getter
	private final OverwatchLeagueConfiguration overwatchLeagueConfiguration = new OverwatchLeagueConfiguration();
	@JsonProperty("rainbow6ProLeague")
	@Getter
	private final Rainbow6ProLeagueConfiguration rainbow6ProLeagueConfiguration = new Rainbow6ProLeagueConfiguration();
	@JsonProperty("announceStartChannel")
	@Setter
	private ChannelConfiguration announceStartChannel;
	@JsonProperty("musicVolume")
	@Getter
	@Setter
	private int musicVolume = 100;
	@JsonProperty("twitchConfiguration")
	@Getter
	private final TwitchConfiguration twitchConfiguration = new TwitchConfiguration();
	@JsonProperty("christmasRole")
	@Setter
	private RoleConfiguration christmasRole;
	@JsonProperty("newYearRole")
	@Setter
	private RoleConfiguration newYearRole;
	@JsonProperty("archiveCategory")
	@Setter
	private CategoryConfiguration archiveCategory;
	@JsonProperty("messagesAwaitingReaction")
	@Setter
	private Set<WaitingReactionMessageConfiguration> messagesAwaitingReaction = new HashSet<>();
	@JsonProperty("amazonTrackings")
	@Getter
	@Setter
	private Set<AmazonTrackingConfiguration> amazonTrackings = new HashSet<>();
	@JsonProperty("reactions")
	@Getter
	@Setter
	private ReactionsConfiguration reactionsConfiguration = new ReactionsConfiguration();
	@JsonProperty("covid19Channel")
	@Setter
	private ChannelConfiguration covid19Channel;
	@JsonProperty("twitchChannel")
	@Getter
	@Deprecated
	private ChannelConfiguration twitchChannel;
	@JsonProperty("hermitcraft")
	@Getter
	private final HermitcraftConfiguration hermitcraftConfiguration = new HermitcraftConfiguration();
	
	GuildConfiguration(final long guildId){
		this.guildId = guildId;
	}
	
	public void addMessagesAwaitingReaction(@NonNull WaitingReactionMessageConfiguration reaction){
		this.messagesAwaitingReaction.add(reaction);
	}
	
	public void removeMessagesAwaitingReaction(@NonNull WaitingReactionMessageConfiguration reaction){
		this.messagesAwaitingReaction.remove(reaction);
	}
	
	public Collection<WaitingReactionMessageConfiguration> getMessagesAwaitingReaction(@NonNull ReactionTag tag){
		return new HashSet<>(this.messagesAwaitingReaction).stream().filter(reaction -> Objects.equals(reaction.getTag(), tag)).collect(Collectors.toSet());
	}
	
	public void removeSchedule(ScheduleConfiguration schedule){
		this.schedules.remove(schedule);
	}
	
	public Iterator<WaitingReactionMessageConfiguration> getMessagesAwaitingReaction(){
		return this.messagesAwaitingReaction.iterator();
	}
	
	public Optional<RoleConfiguration> getChristmasRole(){
		return Optional.ofNullable(christmasRole);
	}
	
	public Optional<RoleConfiguration> getNewYearRole(){
		return Optional.ofNullable(newYearRole);
	}
	
	public Optional<CategoryConfiguration> getArchiveCategory(){
		return Optional.ofNullable(archiveCategory);
	}
	
	public void addAddBackRole(@NonNull final UserRoleConfiguration userRoleConfiguration){
		this.addBackRoles.add(userRoleConfiguration);
	}
	
	@NonNull
	public List<RoleConfiguration> getAutoRolesAndAddBackRoles(@NonNull final Member member){
		return Stream.concat(this.getAutoRoles().stream(), this.getAddBackRoles().stream().filter(c -> Objects.equals(c.getUser().getUserId(), member.getIdLong())).map(UserRoleConfiguration::getRole)).collect(Collectors.toList());
	}
	
	public void addSchedule(@NonNull ScheduleConfiguration schedule){
		this.schedules.add(schedule);
	}
	
	public List<ScheduleConfiguration> getSchedules(){
		return new LinkedList<>(this.schedules);
	}
	
	@NonNull
	public Optional<ChannelConfiguration> getAnnounceStartChannel(){
		return Optional.ofNullable(this.announceStartChannel);
	}
	
	@NonNull
	public Optional<RoleConfiguration> getDjRole(){
		return Optional.ofNullable(this.djRole);
	}
	
	@NonNull
	public Optional<RoleConfiguration> getLeaverRole(){
		return Optional.ofNullable(this.leaverRole);
	}
	
	@NonNull
	public Optional<RoleConfiguration> getPoopRole(){
		return Optional.ofNullable(this.poopRole);
	}
	
	@NonNull
	public Optional<String> getPrefix(){
		return Optional.ofNullable(this.prefix);
	}
	
	@NonNull
	public Optional<ChannelConfiguration> getQuizChannel(){
		return Optional.ofNullable(this.quizChannel);
	}
	
	@NonNull
	public Optional<ChannelConfiguration> getReportChannel(){
		return Optional.ofNullable(this.reportChannel);
	}
	
	@NonNull
	public Optional<ChannelConfiguration> getCovid19Channel(){
		return Optional.ofNullable(this.covid19Channel);
	}
}
