package fr.raksrinana.rsndiscord.runners;

import fr.raksrinana.rsndiscord.commands.reminder.DelayReminderCommand;
import fr.raksrinana.rsndiscord.settings.Settings;
import fr.raksrinana.rsndiscord.settings.types.MessageConfiguration;
import fr.raksrinana.rsndiscord.utils.Actions;
import fr.raksrinana.rsndiscord.utils.log.Log;
import fr.raksrinana.rsndiscord.utils.reminder.ReminderUtils;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class RemindersScheduledRunner implements ScheduledRunner{
	@Getter
	private final JDA jda;
	
	public RemindersScheduledRunner(JDA jda){
		this.jda = jda;
	}
	
	@Override
	public void execute(){
		final var currentDate = LocalDateTime.now();
		for(final var guild : this.getJda().getGuilds()){
			Log.getLogger(guild).debug("Processing guild {}", guild);
			final var it = Settings.get(guild).getReminders().iterator();
			while(it.hasNext()){
				final var reminder = it.next();
				if(currentDate.isAfter(reminder.getNotifyDate())){
					for(final var handler : ReminderUtils.getHandlers()){
						if(handler.acceptTag(reminder.getTag())){
							if(handler.accept(reminder)){
								it.remove();
								break;
							}
						}
					}
				}
				else{
					final var embed = DelayReminderCommand.getEmbedFor(reminder);
					Optional.ofNullable(reminder.getReminderCountdownMessage()).flatMap(MessageConfiguration::getMessage).ifPresentOrElse(message -> Actions.editMessage(message, embed), () -> reminder.getChannel().getChannel().ifPresent(channel -> Actions.sendMessage(channel, "", embed).thenAccept(message -> reminder.setReminderCountdownMessage(new MessageConfiguration(message)))));
				}
			}
		}
	}
	
	@NonNull
	@Override
	public String getName(){
		return "reminders refresher";
	}
	
	@Override
	public long getDelay(){
		return 3;
	}
	
	@Override
	public long getPeriod(){
		return 14;
	}
	
	@NonNull
	@Override
	public TimeUnit getPeriodUnit(){
		return TimeUnit.MINUTES;
	}
}
