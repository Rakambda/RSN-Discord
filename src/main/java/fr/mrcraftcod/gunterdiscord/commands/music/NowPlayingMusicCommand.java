package fr.mrcraftcod.gunterdiscord.commands.music;

import fr.mrcraftcod.gunterdiscord.commands.generic.BasicCommand;
import fr.mrcraftcod.gunterdiscord.commands.generic.Command;
import fr.mrcraftcod.gunterdiscord.commands.generic.CommandResult;
import fr.mrcraftcod.gunterdiscord.utils.Actions;
import fr.mrcraftcod.gunterdiscord.utils.Utilities;
import fr.mrcraftcod.gunterdiscord.utils.player.GunterAudioManager;
import fr.mrcraftcod.gunterdiscord.utils.player.trackfields.ReplayTrackUserField;
import fr.mrcraftcod.gunterdiscord.utils.player.trackfields.RequesterTrackUserField;
import fr.mrcraftcod.gunterdiscord.utils.player.trackfields.TrackUserFields;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import java.awt.Color;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com)
 *
 * @author Thomas Couchoud
 * @since 2018-06-16
 */
public class NowPlayingMusicCommand extends BasicCommand{
	/**
	 * Constructor.
	 *
	 * @param parent The parent command.
	 */
	NowPlayingMusicCommand(final Command parent){
		super(parent);
	}
	
	@Override
	public void addHelp(@NotNull final Guild guild, @NotNull final EmbedBuilder builder){
		super.addHelp(guild, builder);
	}
	
	@Override
	public CommandResult execute(@NotNull final MessageReceivedEvent event, @NotNull final LinkedList<String> args) throws Exception{
		super.execute(event, args);
		final var builder = Utilities.buildEmbed(event.getAuthor(), Color.CYAN, "Currently playing");
		GunterAudioManager.currentTrack(event.getGuild()).ifPresentOrElse(track -> {
			builder.setTitle("Currently playing", track.getInfo().uri);
			final var userData = track.getUserData(TrackUserFields.class);
			builder.setDescription(track.getInfo().title);
			builder.addField("Requester", userData.get(new RequesterTrackUserField()).map(User::getAsMention).orElse("Unknown"), true);
			builder.addField("Repeating", userData.get(new ReplayTrackUserField()).map(Object::toString).orElse("False"), true);
			builder.addField("Position", String.format("%s %s / %s", buildBar(track.getPosition(), track.getDuration()), getDuration(track.getPosition()), getDuration(track.getDuration())), true);
		}, () -> {
			builder.setColor(Color.RED);
			builder.setDescription("No music are currently playing");
		});
		
		Actions.reply(event, builder.build());
		return CommandResult.SUCCESS;
	}
	
	private String buildBar(double current, final double total){
		final var charCount = 10;
		if(current > total){
			current = total;
		}
		final var sb = new StringBuilder();
		IntStream.range(0, charCount).forEach(i -> sb.append('='));
		final var replaceIndex = (int) ((current / total) * charCount);
		sb.replace(replaceIndex, replaceIndex + 1, "O");
		return sb.toString();
	}
	
	/**
	 * Get the duration in a readable format.
	 *
	 * @param time The duration in milliseconds.
	 *
	 * @return A readable version of this duration.
	 */
	static String getDuration(final long time){
		final var duration = Duration.ofMillis(time);
		if(duration.toHoursPart() > 0){
			return String.format("%02d:%02d:%02d", duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
		}
		return String.format("%02d:%02d", duration.toMinutesPart(), duration.toSecondsPart());
	}
	
	@Override
	public AccessLevel getAccessLevel(){
		return AccessLevel.ALL;
	}
	
	@Override
	public String getName(){
		return "Now playing";
	}
	
	@Override
	public List<String> getCommand(){
		//noinspection SpellCheckingInspection
		return List.of("nowplaying", "np");
	}
	
	@Override
	public String getDescription(){
		return "Get information about the current music";
	}
	
	@Override
	public int getScope(){
		return ChannelType.TEXT.getId();
	}
}
