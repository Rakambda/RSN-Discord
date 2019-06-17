package fr.mrcraftcod.gunterdiscord.utils.log;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Objects;

public class Log{
	private static final HashMap<Guild, Logger> LOGGERS = new HashMap<>();
	private static JDA jda;
	
	public static Logger getLogger(final Guild g){
		return LOGGERS.computeIfAbsent(g, g2 -> {
			if(Objects.nonNull(g2)){
				return LoggerFactory.getLogger(g2.getName());
			}
			return LoggerFactory.getLogger("No Guild");
		});
	}
	
	public static Logger getLogger(final long guildId){
		return getLogger(Log.jda.getGuildById(guildId));
	}
	
	public static void setJDA(final JDA jda){
		Log.jda = jda;
	}
}