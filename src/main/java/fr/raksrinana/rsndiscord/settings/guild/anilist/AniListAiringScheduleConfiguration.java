package fr.raksrinana.rsndiscord.settings.guild.anilist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.raksrinana.rsndiscord.api.anilist.data.airing.AiringSchedule;
import fr.raksrinana.rsndiscord.settings.guild.schedule.ScheduleConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.Map;
import static fr.raksrinana.rsndiscord.reaction.handler.AniListReleaseScheduleHandler.MEDIA_ID_KEY;
import static fr.raksrinana.rsndiscord.schedule.ScheduleTag.ANILIST_AIRING_SCHEDULE;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class AniListAiringScheduleConfiguration extends ScheduleConfiguration{
	public AniListAiringScheduleConfiguration(@NotNull User user, @NotNull TextChannel channel, @NotNull ZonedDateTime scheduleDate, @NotNull AiringSchedule schedule){
		super(user, channel, scheduleDate, MessageFormat.format("Episode {0} is airing", schedule.getEpisode()),
				ANILIST_AIRING_SCHEDULE, Map.of(MEDIA_ID_KEY, Integer.toString(schedule.getMedia().getId())));
	}
}
