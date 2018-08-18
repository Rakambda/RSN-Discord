package fr.mrcraftcod.gunterdiscord.commands.hangman;

import fr.mrcraftcod.gunterdiscord.commands.generic.BasicCommand;
import fr.mrcraftcod.gunterdiscord.commands.generic.Command;
import fr.mrcraftcod.gunterdiscord.commands.generic.CommandResult;
import fr.mrcraftcod.gunterdiscord.listeners.HangmanListener;
import fr.mrcraftcod.gunterdiscord.settings.configs.HangmanRoleConfig;
import fr.mrcraftcod.gunterdiscord.utils.Actions;
import fr.mrcraftcod.gunterdiscord.utils.Utilities;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com)
 *
 * @author Thomas Couchoud
 * @since 2018-05-27
 */
public class HangmanJoinCommand extends BasicCommand{
	/**
	 * Constructor.
	 *
	 * @param parent The parent command.
	 */
	HangmanJoinCommand(final Command parent){
		super(parent);
	}
	
	@Override
	public CommandResult execute(@NotNull final MessageReceivedEvent event, @NotNull final LinkedList<String> args) throws Exception{
		super.execute(event, args);
		final var role = new HangmanRoleConfig(event.getGuild()).getObject();
		if(role == null){
			Actions.reply(event, Utilities.buildEmbed(event.getAuthor(), Color.RED, "Erreur").addField("Raison", "Le role du pendu n'est pas configuré", false).build());
		}
		else if(!Utilities.hasRole(event.getMember(), role)){
			final var game = HangmanListener.getGame(event.getGuild());
			if(game.isPresent()){
				Actions.giveRole(event.getGuild(), event.getAuthor(), role);
				game.get().onPlayerJoin(event.getMember());
			}
			else{
				return CommandResult.FAILED;
			}
		}
		return CommandResult.SUCCESS;
	}
	
	@Override
	public AccessLevel getAccessLevel(){
		return AccessLevel.ALL;
	}
	
	@Override
	public String getName(){
		return "Rejoindre pendu";
	}
	
	@Override
	public List<String> getCommand(){
		return List.of("join", "j");
	}
	
	@Override
	public String getDescription(){
		return "Rejoins une partie de pendu";
	}
	
	@Override
	public int getScope(){
		return ChannelType.TEXT.getId();
	}
}
