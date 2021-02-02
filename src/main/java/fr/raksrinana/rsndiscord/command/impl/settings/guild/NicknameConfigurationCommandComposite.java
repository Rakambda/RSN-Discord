package fr.raksrinana.rsndiscord.command.impl.settings.guild;

import fr.raksrinana.rsndiscord.command.Command;
import fr.raksrinana.rsndiscord.command.CommandComposite;
import fr.raksrinana.rsndiscord.command.impl.settings.guild.nickname.ChangeDelayConfigurationCommand;
import fr.raksrinana.rsndiscord.permission.IPermission;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import static fr.raksrinana.rsndiscord.permission.PermissionUtils.ALLOW;

public class NicknameConfigurationCommandComposite extends CommandComposite{
	public NicknameConfigurationCommandComposite(Command parent){
		super(parent);
		addSubCommand(new ChangeDelayConfigurationCommand(this));
	}
	
	@Override
	public @NotNull IPermission getPermission(){
		return ALLOW;
	}
	
	@NotNull
	@Override
	public String getName(@NotNull Guild guild){
		return "Nickname";
	}
	
	@NotNull
	@Override
	public @NotNull List<String> getCommandStrings(){
		return List.of("nickname");
	}
	
	@NotNull
	@Override
	public String getDescription(@NotNull Guild guild){
		return "Nickname configurations";
	}
}
