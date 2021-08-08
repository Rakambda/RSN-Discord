package fr.raksrinana.rsndiscord.command.impl;

import fr.raksrinana.rsndiscord.command.BotSlashCommand;
import fr.raksrinana.rsndiscord.command.base.group.SubCommandsGroupCommand;
import fr.raksrinana.rsndiscord.command.impl.music.*;
import org.jetbrains.annotations.NotNull;

@BotSlashCommand
public class MusicGroupCommand extends SubCommandsGroupCommand{
	public MusicGroupCommand(){
		addSubcommand(new AddCommand());
		addSubcommand(new MoveCommand());
		addSubcommand(new NowPlayingCommand());
		addSubcommand(new PauseCommand());
		addSubcommand(new QueueCommand());
		addSubcommand(new ResumeCommand());
		addSubcommand(new SeekCommand());
		addSubcommand(new ShuffleCommand());
		addSubcommand(new SkipCommand());
		addSubcommand(new StopCommand());
		addSubcommand(new VolumeCommand());
	}
	
	@Override
	@NotNull
	public String getId(){
		return "music";
	}
	
	@Override
	@NotNull
	public String getShortDescription(){
		return "Manages music";
	}
}
