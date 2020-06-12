package fr.raksrinana.rsndiscord.commands.config.guild.anilist;

import fr.raksrinana.rsndiscord.commands.config.helpers.ChannelConfigurationCommand;
import fr.raksrinana.rsndiscord.commands.generic.Command;
import fr.raksrinana.rsndiscord.settings.Settings;
import fr.raksrinana.rsndiscord.settings.types.ChannelConfiguration;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Guild;
import java.util.List;
import java.util.Optional;

public class NotificationChannelConfigurationCommand extends ChannelConfigurationCommand{
	public NotificationChannelConfigurationCommand(final Command parent){
		super(parent);
	}
	
	@Override
	protected void setConfig(@NonNull final Guild guild, @NonNull final ChannelConfiguration value){
		Settings.get(guild).getAniListConfiguration().setNotificationsChannel(value);
	}
	
	@Override
	protected void removeConfig(@NonNull final Guild guild){
		Settings.get(guild).getAniListConfiguration().setNotificationsChannel(null);
	}
	
	@NonNull
	@Override
	protected Optional<ChannelConfiguration> getConfig(@NonNull final Guild guild){
		return Settings.get(guild).getAniListConfiguration().getNotificationsChannel();
	}
	
	@NonNull
	@Override
	public String getName(@NonNull Guild guild){
		return "AniList notification channel";
	}
	
	@NonNull
	@Override
	public List<String> getCommandStrings(){
		return List.of("notificationChannel");
	}
}
