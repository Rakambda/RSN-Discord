package fr.raksrinana.rsndiscord.utils.anilist.notifications;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.raksrinana.rsndiscord.utils.anilist.AniListUtils;
import fr.raksrinana.rsndiscord.utils.anilist.media.Media;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.awt.Color;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import static fr.raksrinana.rsndiscord.utils.LangUtils.translate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("RELATED_MEDIA_ADDITION")
@Getter
public class RelatedMediaNotification extends Notification{
	@Getter
	private static final String QUERY = "RelatedMediaAdditionNotification {\n" + "id\n" + "type\n" + "createdAt\n" + Media.getQUERY() + "\n}";
	@JsonProperty("media")
	private Media media;
	
	public RelatedMediaNotification(){
		super(NotificationType.RELATED_MEDIA_ADDITION);
	}
	
	@Override
	public void fillEmbed(@NonNull Locale locale, @NonNull final EmbedBuilder builder){
		builder.setTimestamp(this.getDate());
		builder.setColor(Color.PINK);
		builder.setTitle(translate(locale, "anilist.related"), this.getMedia().getUrl().toString());
		builder.addBlankField(false);
		builder.addField(translate(locale, "anilist.media"), "", false);
		this.getMedia().fillEmbed(locale, builder);
	}
	
	@Override
	@NonNull
	public URL getUrl(){
		return Optional.of(this.getMedia()).map(Media::getUrl).orElse(AniListUtils.FALLBACK_URL);
	}
	
	@Override
	public int hashCode(){
		return this.getId();
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
