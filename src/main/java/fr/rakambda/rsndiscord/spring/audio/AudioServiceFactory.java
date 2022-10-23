package fr.rakambda.rsndiscord.spring.audio;

import fr.rakambda.rsndiscord.spring.storage.repository.AudioRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AudioServiceFactory{
	private final AudioRepository audioRepository;
	private final Map<Long, AudioService> services;
	
	public AudioServiceFactory(AudioRepository audioRepository){
		this.audioRepository = audioRepository;
		
		services = new ConcurrentHashMap<>();
	}
	
	@NotNull
	public AudioService get(@NotNull Guild guild){
		return services.computeIfAbsent(guild.getIdLong(), id -> buildService(guild));
	}
	
	@NotNull
	private AudioService buildService(@NotNull Guild guild){
		var guildEntity = audioRepository.findByGuild_Id(guild.getIdLong()).orElseThrow();
		var volume = Math.min(100, Math.max(0, guildEntity.getVolume()));
		
		return new AudioService(guild, volume);
	}
	
	@NotNull
	public Collection<AudioService> getAll(){
		return services.values();
	}
}
