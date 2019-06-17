package fr.mrcraftcod.gunterdiscord.commands;

import fr.mrcraftcod.gunterdiscord.commands.generic.BasicCommand;
import fr.mrcraftcod.gunterdiscord.commands.generic.CommandResult;
import fr.mrcraftcod.gunterdiscord.utils.Actions;
import fr.mrcraftcod.gunterdiscord.utils.Utilities;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import java.awt.Color;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 12/04/2018.
 *
 * @author Thomas Couchoud
 * @since 2018-04-12
 */
public class TimeCommand extends BasicCommand{
	@Override
	public CommandResult execute(final GuildMessageReceivedEvent event, @NotNull final LinkedList<String> args) throws Exception{
		super.execute(event, args);
		Actions.reply(event, Utilities.buildEmbed(event.getAuthor(), Color.GREEN, "Server time infos").addField("Time:", ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), false).addField("Ms", "" + System.currentTimeMillis(), false).build());
		return CommandResult.SUCCESS;
	}
	
	@Override
	public AccessLevel getAccessLevel(){
		return AccessLevel.MODERATOR;
	}
	
	@Override
	public String getName(){
		return "Get server time";
	}
	
	@Override
	public List<String> getCommandStrings(){
		return List.of("time");
	}
	
	@Override
	public String getDescription(){
		return "Get the current time of the server";
	}
	
	@Override
	public int getScope(){
		return ChannelType.TEXT.getId();
	}
}
