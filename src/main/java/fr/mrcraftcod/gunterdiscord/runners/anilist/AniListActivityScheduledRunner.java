package fr.mrcraftcod.gunterdiscord.runners.anilist;

import fr.mrcraftcod.gunterdiscord.runners.ScheduledRunner;
import fr.mrcraftcod.gunterdiscord.settings.NewSettings;
import fr.mrcraftcod.gunterdiscord.settings.types.ChannelConfiguration;
import fr.mrcraftcod.gunterdiscord.settings.types.UserDateConfiguration;
import fr.mrcraftcod.gunterdiscord.utils.anilist.AniListUtils;
import fr.mrcraftcod.gunterdiscord.utils.anilist.activity.list.AniListListActivity;
import fr.mrcraftcod.gunterdiscord.utils.anilist.queries.AniListListActivityPagedQuery;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static fr.mrcraftcod.gunterdiscord.utils.log.Log.getLogger;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-08.
 *
 * @author Thomas Couchoud
 * @since 2018-10-08
 */
public class AniListActivityScheduledRunner implements AniListRunner<AniListListActivity, AniListListActivityPagedQuery>, ScheduledRunner{
	private final JDA jda;
	
	public AniListActivityScheduledRunner(@Nonnull final JDA jda){
		getLogger(null).info("Creating AniList list change runner");
		this.jda = jda;
	}
	
	@Override
	public long getPeriod(){
		return 1;
	}
	
	@Nonnull
	@Override
	public TimeUnit getPeriodUnit(){
		return TimeUnit.HOURS;
	}
	
	@Override
	public long getDelay(){
		return 0;
	}
	
	@Override
	public void run(){
		this.runQueryOnEveryUserAndDefaultChannels();
	}
	
	@Nonnull
	@Override
	public String getRunnerName(){
		return "list activity";
	}
	
	@Nonnull
	@Override
	public JDA getJDA(){
		return this.jda;
	}
	
	@Override
	public List<TextChannel> getChannels(){
		return this.getJDA().getGuilds().stream().map(g -> NewSettings.getConfiguration(g).getAniListConfiguration().getMediaChangeChannel().map(ChannelConfiguration::getChannel).filter(Optional::isPresent).map(Optional::get).orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
	}
	
	@Override
	public boolean keepOnlyNew(){
		return true;
	}
	
	@Nonnull
	@Override
	public String getFetcherID(){
		return "listActivity";
	}
	
	@Nonnull
	@Override
	public AniListListActivityPagedQuery initQuery(@Nonnull final Member member){
		return new AniListListActivityPagedQuery(AniListUtils.getUserId(member).orElseThrow(), NewSettings.getConfiguration(member.getGuild()).getAniListConfiguration().getLastAccess(this.getFetcherID()).stream().filter(a -> Objects.equals(a.getUserId(), member.getUser().getIdLong())).map(UserDateConfiguration::getDate).findAny().orElse(AniListUtils.getDefaultDate(member)));
	}
}
