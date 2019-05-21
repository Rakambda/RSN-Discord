package fr.mrcraftcod.gunterdiscord.utils.anilist.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.util.Optional;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-11.
 *
 * @author Thomas Couchoud
 * @since 2018-10-11
 */
@SuppressWarnings("WeakerAccess")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AniListAnimeMedia extends AniListMedia{
	@JsonProperty("episodes")
	private Integer episodes;
	
	public AniListAnimeMedia(){
		super(AniListMediaType.ANIME);
	}
	
	@Override
	public void fillEmbed(final EmbedBuilder builder){
		super.fillEmbed(builder);
		Optional.ofNullable(getEpisodes()).map(Object::toString).ifPresent(val -> builder.addField("Episodes", val, true));
		Optional.ofNullable(getSeason()).map(Enum::toString).ifPresent(val -> builder.addField("Season", val, true));
	}
	
	@Override
	public String getProgressType(final boolean contains){
		return "watched episode";
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public Integer getItemCount(){
		return getEpisodes();
	}
	
	public Integer getEpisodes(){
		return this.episodes;
	}
}
