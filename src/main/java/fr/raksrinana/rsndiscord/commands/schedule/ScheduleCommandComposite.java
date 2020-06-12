package fr.raksrinana.rsndiscord.commands.schedule;

import fr.raksrinana.rsndiscord.commands.generic.BotCommand;
import fr.raksrinana.rsndiscord.commands.generic.CommandComposite;
import fr.raksrinana.rsndiscord.commands.schedule.delete.DeleteCommandComposite;
import lombok.NonNull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fr.raksrinana.rsndiscord.utils.LangUtils.translate;

@BotCommand
public class ScheduleCommandComposite extends CommandComposite{
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'!'HH:mm z");
	private static final Pattern PERIOD_PATTERN = Pattern.compile("([0-9]+)([mhd])");
	
	public ScheduleCommandComposite(){
		this.addSubCommand(new MessageScheduleCommand(this));
		this.addSubCommand(new DeleteCommandComposite(this));
	}
	
	public static Optional<ZonedDateTime> getReminderDate(@NonNull String string){
		try{
			final var duration = parsePeriod(string);
			if(!duration.isZero()){
				return Optional.of(ZonedDateTime.now().plus(duration));
			}
			return Optional.of(ZonedDateTime.parse(string, DATE_FORMATTER));
		}
		catch(DateTimeParseException ignored){
		}
		return Optional.empty();
	}
	
	private static Duration parsePeriod(@NonNull String period){
		period = period.toLowerCase(Locale.ENGLISH);
		Matcher matcher = PERIOD_PATTERN.matcher(period);
		Duration duration = Duration.ZERO;
		while(matcher.find()){
			int amount = Integer.parseInt(matcher.group(1));
			String type = matcher.group(2);
			switch(type){
				case "m" -> duration = duration.plus(Duration.ofMinutes(amount));
				case "h" -> duration = duration.plus(Duration.ofHours(amount));
				case "d" -> duration = duration.plus(Duration.ofDays(amount));
			}
		}
		return duration;
	}
	
	@Override
	public void addHelp(@NonNull Guild guild, @NonNull EmbedBuilder builder){
		super.addHelp(guild, builder);
		builder.addField("delay", translate(guild, "command.schedule.help.delay"), false);
	}
	
	@NonNull
	@Override
	public String getName(@NonNull  Guild guild){
		return translate(guild, "command.schedule.name");
	}
	
	@NonNull
	@Override
	public List<String> getCommandStrings(){
		return List.of("schedule");
	}
	
	@NonNull
	@Override
	public String getDescription(@NonNull Guild guild){
		return translate(guild, "command.schedule.description");
	}
}
