package fr.mrcraftcod.gunterdiscord.commands.anilist.fetch;

import fr.mrcraftcod.gunterdiscord.commands.generic.Command;
import fr.mrcraftcod.gunterdiscord.commands.generic.CommandComposite;
import net.dv8tion.jda.api.entities.ChannelType;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-08.
 *
 * @author Thomas Couchoud
 * @since 2018-10-08
 */
public class AniListFetchCommandComposite extends CommandComposite{
	/**
	 * Constructor.
	 *
	 * @param parent The parent command.
	 */
	public AniListFetchCommandComposite(@NotNull final Command parent){
		super(parent);
		addSubCommand(new AniListFetchActivityCommand(this));
		addSubCommand(new AniListFetchNotificationCommand(this));
		addSubCommand(new AniListFetchMediaUserListCommand(this));
		addSubCommand(new AniListFetchMediaUserListDifferencesCommand(this));
	}
	
	@Override
	public AccessLevel getAccessLevel(){
		return AccessLevel.MODERATOR;
	}
	
	@Override
	public String getName(){
		return "AniList fetcher";
	}
	
	@Override
	public List<String> getCommandStrings(){
		return List.of("fetch", "f");
	}
	
	@Override
	public String getDescription(){
		return "Fetch data from AniList";
	}
	
	@Override
	public int getScope(){
		return ChannelType.TEXT.getId();
	}
}
