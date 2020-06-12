package fr.raksrinana.rsndiscord.commands.config.guild;

import fr.raksrinana.rsndiscord.commands.config.guild.nickname.ChangeDelayConfigurationCommand;
import fr.raksrinana.rsndiscord.commands.generic.Command;
import fr.raksrinana.rsndiscord.commands.generic.CommandComposite;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Guild;
import java.util.List;

public class NicknameConfigurationCommandComposite extends CommandComposite{
	public NicknameConfigurationCommandComposite(final Command parent){
		super(parent);
		this.addSubCommand(new ChangeDelayConfigurationCommand(this));
	}
	
	@NonNull
	@Override
	public AccessLevel getAccessLevel(){
		return AccessLevel.ADMIN;
	}
	
	@NonNull
	@Override
	public String getName(@NonNull Guild guild){
		return "Nickname";
	}
	
	@NonNull
	@Override
	public List<String> getCommandStrings(){
		return List.of("nickname");
	}
	
	@NonNull
	@Override
	public String getDescription(@NonNull Guild guild){
		return "Nickname configurations";
	}
}
