package fr.raksrinana.rsndiscord.command.base;

import fr.raksrinana.rsndiscord.command.api.ISimpleCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleCommand extends ExecutableCommand implements ISimpleCommand{
	@Override
	@NotNull
	public CommandData getSlashCommand(){
		return new CommandData(getId(), getShortDescription())
				.setDefaultEnabled(getDefaultPermission())
				.addOptions(getOptions());
	}
}
