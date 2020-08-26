package fr.raksrinana.rsndiscord.commands.trombinoscope;

import fr.raksrinana.rsndiscord.commands.generic.BotCommand;
import fr.raksrinana.rsndiscord.commands.generic.CommandComposite;
import fr.raksrinana.rsndiscord.utils.permission.Permission;
import fr.raksrinana.rsndiscord.utils.permission.SimplePermission;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Guild;
import java.util.List;
import static fr.raksrinana.rsndiscord.utils.LangUtils.translate;

@BotCommand
public class TrombinoscopeCommandComposite extends CommandComposite{
	/**
	 * Constructor.
	 */
	public TrombinoscopeCommandComposite(){
		super();
		this.addSubCommand(new AddCommand(this));
		this.addSubCommand(new GetCommand(this));
		this.addSubCommand(new RemoveCommand(this));
		this.addSubCommand(new StatsCommand(this));
		this.addSubCommand(new GlobalCommand(this));
	}
	
	@Override
	public @NonNull Permission getPermission(){
		return new SimplePermission("command.trombinoscope", false);
	}
	
	@NonNull
	@Override
	public String getName(@NonNull Guild guild){
		return translate(guild, "command.trombinoscope.name");
	}
	
	@NonNull
	@Override
	public List<String> getCommandStrings(){
		return List.of("trombinoscope", "trombi", "t");
	}
	
	@NonNull
	@Override
	public String getDescription(@NonNull Guild guild){
		return translate(guild, "command.trombinoscope.description");
	}
}
