package fr.mrcraftcod.gunterdiscord.commands;

import fr.mrcraftcod.gunterdiscord.commands.generic.BasicCommand;
import fr.mrcraftcod.gunterdiscord.commands.generic.CommandResult;
import fr.mrcraftcod.gunterdiscord.runners.DisplayDailyStatsScheduledRunner;
import fr.mrcraftcod.gunterdiscord.settings.configs.MembersParticipationConfig;
import fr.mrcraftcod.gunterdiscord.utils.Actions;
import fr.mrcraftcod.gunterdiscord.utils.Utilities;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 12/04/2018.
 *
 * @author Thomas Couchoud
 * @since 2018-04-12
 */
public class TempParticipationCommand extends BasicCommand{
	@Override
	public CommandResult execute(@NotNull final MessageReceivedEvent event, @NotNull final LinkedList<String> args) throws Exception{
		super.execute(event, args);
		final var localDate = LocalDate.now();
		final var ytdKey = localDate.format(DisplayDailyStatsScheduledRunner.DF);
		final var date = localDate.format(DisplayDailyStatsScheduledRunner.DFD);
		final var stats = new MembersParticipationConfig(event.getGuild()).getValue(ytdKey);
		if(Objects.nonNull(stats)){
			final var i = new AtomicInteger(1);
			final var builder = Utilities.buildEmbed(event.getAuthor(), Color.MAGENTA, "Participation of the " + date);
			stats.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).map(e -> {
				final var user = event.getJDA().getUserById(e.getKey());
				if(Objects.nonNull(user)){
					return Map.entry(user, e.getValue());
				}
				return null;
			}).filter(Objects::nonNull).limit(10).forEachOrdered(e -> builder.addField("#" + i.getAndIncrement(), e.getKey().getAsMention() + " Messages: " + e.getValue(), false));
			Actions.reply(event, builder.build());
		}
		return CommandResult.SUCCESS;
	}
	
	@Override
	public AccessLevel getAccessLevel(){
		return AccessLevel.ADMIN;
	}
	
	@Override
	public String getName(){
		return "Temporary participation";
	}
	
	@Override
	public List<String> getCommand(){
		return List.of("tempparticipation", "tp");
	}
	
	@Override
	public String getDescription(){
		return "Display the temporary ranking for the day";
	}
	
	@Override
	public int getScope(){
		return ChannelType.TEXT.getId();
	}
}
