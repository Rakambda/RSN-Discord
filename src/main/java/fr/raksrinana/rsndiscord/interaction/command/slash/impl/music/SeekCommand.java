package fr.raksrinana.rsndiscord.interaction.command.slash.impl.music;

import fr.raksrinana.rsndiscord.interaction.command.CommandResult;
import fr.raksrinana.rsndiscord.interaction.command.slash.base.group.SubSlashCommand;
import fr.raksrinana.rsndiscord.music.RSNAudioManager;
import fr.raksrinana.rsndiscord.utils.jda.JDAWrappers;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import static fr.raksrinana.rsndiscord.interaction.command.CommandResult.HANDLED;
import static fr.raksrinana.rsndiscord.utils.LangUtils.translate;
import static java.util.Objects.isNull;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

@Log4j2
public class SeekCommand extends SubSlashCommand{
	private static final Pattern TIME_PATTERN = Pattern.compile("((\\d{1,2}):)?((\\d{1,2}):)?(\\d{1,2})");
	private static final long SECOND_PER_MINUTE = 60;
	private static final long SECOND_PER_HOUR = 3600;
	private static final String TIME_OPTION_ID = "time";
	
	@Override
	@NotNull
	public String getId(){
		return "seek";
	}
	
	@Override
	@NotNull
	public String getShortDescription(){
		return "Seek time in the track";
	}
	
	@Override
	public boolean replyEphemeral(){
		return false;
	}
	
	@Override
	@NotNull
	protected Collection<? extends OptionData> getOptions(){
		return List.of(new OptionData(STRING, TIME_OPTION_ID, "Time to seek").setRequired(true));
	}
	
	@Override
	public boolean isSpecificAllowed(@NotNull Member member){
		return RSNAudioManager.isRequester(member.getGuild(), member.getUser());
	}
	
	@Override
	@NotNull
	public CommandResult executeGuild(@NotNull SlashCommandInteraction event, @NotNull Guild guild, @NotNull Member member){
		var time = SeekCommand.parseTime(guild, event.getOption(TIME_OPTION_ID).getAsString());
		
		if(time < 0){
			JDAWrappers.edit(event, translate(guild, "music.invalid-format")).submitAndDelete(5);
		}
		else{
			var message = switch(RSNAudioManager.seek(guild, time)){
				case NO_MUSIC -> "music.nothing-playing";
				case OK -> "music.seeked";
				case IMPOSSIBLE -> "music.seek-error";
			};
			JDAWrappers.edit(event, translate(guild, message, event.getUser().getAsMention())).submitAndDelete(5);
		}
		return HANDLED;
	}
	
	private static long parseTime(@NotNull Guild guild, @NotNull String time){
		var matcher = TIME_PATTERN.matcher(time);
		if(!matcher.matches()){
			return -1;
		}
		var duration = 0L;
		duration += SeekCommand.getAsLong(guild, matcher.group(2)) * SECOND_PER_HOUR;
		duration += SeekCommand.getAsLong(guild, matcher.group(4)) * SECOND_PER_MINUTE;
		duration += SeekCommand.getAsLong(guild, matcher.group(5));
		return duration * 1000;
	}
	
	private static long getAsLong(@NotNull Guild guild, @Nullable String str){
		if(isNull(str) || str.isBlank()){
			return 0;
		}
		try{
			return Long.parseLong(str);
		}
		catch(Exception e){
			log.error("Error parsing {} into long", str, e);
		}
		return 0;
	}
}
