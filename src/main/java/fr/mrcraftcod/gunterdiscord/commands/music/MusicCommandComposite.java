package fr.mrcraftcod.gunterdiscord.commands.music;

import fr.mrcraftcod.gunterdiscord.commands.generic.CommandComposite;
import fr.mrcraftcod.gunterdiscord.settings.configs.DJRoleConfig;
import fr.mrcraftcod.gunterdiscord.utils.Utilities;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import java.util.List;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com)
 *
 * @author Thomas Couchoud
 * @since 2018-08-18
 */
public class MusicCommandComposite extends CommandComposite{
	/**
	 * Constructor.
	 */
	public MusicCommandComposite(){
		addSubCommand(new AddMusicCommand(this));
		addSubCommand(new StopMusicCommand(this));
		addSubCommand(new PauseMusicCommand(this));
		addSubCommand(new ResumeMusicCommand(this));
		addSubCommand(new NowPlayingMusicCommand(this));
		addSubCommand(new SeekMusicCommand(this));
		addSubCommand(new SkipMusicCommand(this));
		addSubCommand(new QueueMusicCommand(this));
		addSubCommand(new ShuffleMusicCommand(this));
	}
	
	@Override
	public boolean isAllowed(final Member member){
		return Utilities.isTeam(member) || Utilities.hasRole(member, new DJRoleConfig(member.getGuild()).getObject(null));
	}
	
	@Override
	public AccessLevel getAccessLevel(){
		return AccessLevel.ALL;
	}
	
	@Override
	public String getName(){
		return "Music";
	}
	
	@Override
	public List<String> getCommandStrings(){
		return List.of("music", "m");
	}
	
	@Override
	public String getDescription(){
		return "Handles music interactions";
	}
	
	@Override
	public int getScope(){
		return ChannelType.TEXT.getId();
	}
}
