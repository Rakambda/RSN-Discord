package fr.mrcraftcod.gunterdiscord.listeners;

import fr.mrcraftcod.gunterdiscord.settings.configs.ModoRolesConfig;
import fr.mrcraftcod.gunterdiscord.settings.configs.VoiceTextChannelsAssociationConfig;
import fr.mrcraftcod.gunterdiscord.settings.configs.VoiceTextChannelsConfig;
import fr.mrcraftcod.gunterdiscord.utils.Actions;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.*;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.restaction.ChannelAction;
import java.util.List;
import static fr.mrcraftcod.gunterdiscord.utils.log.Log.getLogger;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 31/07/2018.
 *
 * @author Thomas Couchoud
 * @since 2018-07-31
 */
public class VoiceTextChannelsListener extends ListenerAdapter{
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event){
		super.onGuildVoiceJoin(event);
		try{
			joinVoice(event, event.getChannelJoined());
		}
		catch(Exception e){
			getLogger(event.getGuild()).error("", e);
		}
	}
	
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event){
		super.onGuildVoiceMove(event);
		try{
			leaveVoice(event);
			joinVoice(event, event.getChannelJoined());
		}
		catch(Exception e){
			getLogger(event.getGuild()).error("", e);
		}
	}
	
	/**
	 * Handle things when a user leaves a channel.
	 *
	 * @param event The event.
	 */
	private static void leaveVoice(GuildVoiceUpdateEvent event){
		VoiceTextChannelsAssociationConfig config = new VoiceTextChannelsAssociationConfig(event.getGuild());
		Long channelID = config.getValue(event.getChannelLeft().getIdLong());
		if(channelID != null){
			TextChannel channel = event.getGuild().getTextChannelById(channelID);
			if(channel != null){
				channel.putPermissionOverride(event.getMember()).setDeny(Permission.MESSAGE_READ).queue();
				if(event.getChannelLeft().getMembers().isEmpty()){
					channel.delete().reason("No more users in the vocal").queue();
					config.deleteKey(event.getChannelLeft().getIdLong());
					getLogger(event.getGuild()).info("Text channel {} deleted", channel);
				}
			}
		}
	}
	
	/**
	 * Handle things when a user joins a channel.
	 *
	 * @param event The event.
	 */
	private static void joinVoice(GenericGuildVoiceEvent event, VoiceChannel voiceChannel){
		if(new VoiceTextChannelsConfig(event.getGuild()).contains(voiceChannel.getIdLong())){
			VoiceTextChannelsAssociationConfig config = new VoiceTextChannelsAssociationConfig(event.getGuild());
			TextChannel channel = null;
			Long channelID = config.getValue(voiceChannel.getIdLong());
			if(channelID != null){
				channel = event.getGuild().getTextChannelById(channelID);
			}
			
			if(channel == null){
				ChannelAction creator = event.getGuild().getController().createTextChannel(voiceChannel.getName());
				creator.setParent(voiceChannel.getParent());
				creator.setTopic("Channel pour les utilisateurs du vocal du même nom");
				creator.addPermissionOverride(event.getGuild().getPublicRole(), List.of(), List.of(Permission.MESSAGE_READ));
				for(Role role : new ModoRolesConfig(event.getGuild()).getAsList()){
					creator.addPermissionOverride(role, List.of(Permission.MESSAGE_READ), List.of());
				}
				channel = (TextChannel) creator.complete();
				config.addValue(voiceChannel.getIdLong(), channel.getIdLong());
				getLogger(event.getGuild()).info("Text channel {} created", channel);
			}
			
			channel.putPermissionOverride(event.getMember()).setAllow(Permission.MESSAGE_READ).queue();
			Actions.sendMessage(channel, "%s a rejoint le channel", event.getMember().getAsMention());
		}
	}
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
		super.onGuildVoiceLeave(event);
		try{
			leaveVoice(event);
		}
		catch(Exception e){
			getLogger(event.getGuild()).error("", e);
		}
	}
}

