package fr.mrcraftcod.gunterdiscord.runners;

import fr.mrcraftcod.gunterdiscord.settings.configs.AniListChannelConfig;
import fr.mrcraftcod.gunterdiscord.settings.configs.AniListTokenConfig;
import fr.mrcraftcod.utils.http.requestssenders.post.JSONPostRequestSender;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import static fr.mrcraftcod.gunterdiscord.utils.log.Log.getLogger;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-10-08.
 *
 * @author Thomas Couchoud
 * @since 2018-10-08
 */
public class AniListScheduledRunner implements Runnable{
	public static final Logger LOGGER = LoggerFactory.getLogger(AniListScheduledRunner.class);
	private final JDA jda;
	
	public AniListScheduledRunner(final JDA jda){
		getLogger(null).info("Creating anilist runner");
		this.jda = jda;
	}
	
	@Override
	public void run(){
		LOGGER.info("Starting AniList runner");
		try{
			final var userChanges = new HashMap<User, List<String>>();
			for(final var guild : jda.getGuilds()){
				final var channel = new AniListChannelConfig(guild).getObject(null);
				if(Objects.nonNull(channel)){
					final var tokens = new AniListTokenConfig(guild).getAsMap();
					for(final var userID : tokens.keySet()){
						final var user = jda.getUserById(userID);
						if(!userChanges.containsKey(user)){
							userChanges.put(user, getChanges(user, tokens.get(userID)));
						}
					}
				}
			}
			LOGGER.info("AniList runner done");
		}
		catch(final Exception e){
			LOGGER.error("Error in AniList runner", e);
		}
	}
	
	private List<String> getChanges(final User user, final String code) throws Exception{
		LOGGER.info("Fetching user {}", user);
		final var token = getApiToken(code);
		final var headers = new HashMap<String, String>();
		headers.put("Authorization", "Bearer " + token);
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		
		LOGGER.info("{}", new JSONPostRequestSender(new URL("https://graphql.anilist.co"), headers).getRequest().asString().getBody());
		
		final var handler = new JSONPostRequestSender(new URL("https://graphql.anilist.co"), headers).getRequestHandler();
		if(handler.getStatus() == 200){
			final var jsonResult = handler.getResult();
			LOGGER.warn("AUTH RESULT: {}", jsonResult.toString());
		}
		else{
			LOGGER.error("Error getting API access, HTTP code {}", handler.getStatus());
		}
		return new ArrayList<>();
	}
	
	private String getApiToken(final String token) throws Exception{
		final var headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		
		final var body = new JSONObject();
		body.put("grant_type", "authorization_code");
		body.put("client_id", "1230");
		body.put("client_secret", System.getenv("ANILIST_SECRET"));
		body.put("redirect_uri", "https://www.mrcraftcod.fr/redirect");
		body.put("code", token);
		final var result = new JSONPostRequestSender(new URL("https://anilist.co/api/v2/oauth/token"), headers, new HashMap<>(), body.toString()).getRequestHandler();
		if(result.getStatus() != 200){
			throw new Exception("Getting token get HTTP " + result.getStatus());
		}
		return "";
	}
}
