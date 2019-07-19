package fr.mrcraftcod.gunterdiscord.commands.config.guild;

import fr.mrcraftcod.gunterdiscord.commands.config.helpers.RoleConfigurationCommand;
import fr.mrcraftcod.gunterdiscord.commands.generic.Command;
import fr.mrcraftcod.gunterdiscord.settings.NewSettings;
import fr.mrcraftcod.gunterdiscord.settings.types.RoleConfiguration;
import net.dv8tion.jda.api.entities.Guild;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class LeaverRoleConfigurationCommand extends RoleConfigurationCommand{
	public LeaverRoleConfigurationCommand(@Nullable Command parent){
		super(parent);
	}
	
	@Nonnull
	@Override
	protected Optional<RoleConfiguration> getConfig(@Nonnull Guild guild){
		return NewSettings.getConfiguration(guild).getLeaverRole();
	}
	
	@Override
	protected void setConfig(@Nonnull Guild guild, @Nonnull RoleConfiguration value){
		NewSettings.getConfiguration(guild).setLeaverRole(value);
	}
	
	@Override
	protected void removeConfig(@Nonnull Guild guild){
		NewSettings.getConfiguration(guild).setLeaverRole(null);
	}
	
	@Nonnull
	@Override
	public String getName(){
		return "Leaver Role";
	}
	
	@Nonnull
	@Override
	public List<String> getCommandStrings(){
		return List.of("leaverRole");
	}
}
