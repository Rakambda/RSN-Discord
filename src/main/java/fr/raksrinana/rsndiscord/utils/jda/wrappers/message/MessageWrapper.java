package fr.raksrinana.rsndiscord.utils.jda.wrappers.message;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import static fr.raksrinana.rsndiscord.schedule.ScheduleService.deleteMessageMins;

@Log4j2
public class MessageWrapper{
	private final MessageChannel channel;
	private MessageAction action;
	
	public MessageWrapper(@NotNull MessageChannel channel, @NotNull String message){
		this.channel = channel;
		action = channel.sendMessage(message);
	}
	
	public MessageWrapper(@NotNull MessageChannel channel, @NotNull MessageEmbed embed){
		this.channel = channel;
		action = channel.sendMessageEmbeds(embed);
	}
	
	public MessageWrapper(@NotNull MessageChannel channel, @NotNull Message message){
		this.channel = channel;
		action = channel.sendMessage(message);
	}
	
	@NotNull
	public MessageWrapper allowedMentions(@Nullable Collection<Message.MentionType> mentionTypes){
		action = action.allowedMentions(mentionTypes);
		return this;
	}
	
	@NotNull
	public MessageWrapper mention(@NotNull Set<? extends IMentionable> mentions){
		action = action.mention(mentions);
		return this;
	}
	
	@NotNull
	public MessageWrapper mention(@NotNull IMentionable... mentions){
		action = action.mention(mentions);
		return this;
	}
	
	@NotNull
	public MessageWrapper tts(boolean state){
		action = action.tts(state);
		return this;
	}
	
	@NotNull
	public MessageWrapper replyTo(@NotNull Message message){
		action = action.reference(message);
		return this;
	}
	
	@NotNull
	public MessageWrapper replyTo(@NotNull MessageReference messageReference){
		action = action.referenceById(messageReference.getMessageIdLong());
		return this;
	}
	
	@NotNull
	public MessageWrapper addFile(@NotNull Path path){
		action = action.addFile(path.toFile());
		return this;
	}
	
	@NotNull
	public MessageWrapper addFile(@NotNull InputStream inputStream, @NotNull String fileName){
		action = action.addFile(inputStream, fileName);
		return this;
	}
	
	@NotNull
	public MessageWrapper embed(@Nullable MessageEmbed embed){
		action = action.setEmbeds(embed);
		return this;
	}
	
	@NotNull
	public MessageWrapper addActionRow(@NotNull Collection<Component> components){
		action = action.setActionRow(components);
		return this;
	}
	
	@NotNull
	public MessageWrapper addActionRow(@NotNull Component... components){
		action = action.setActionRow(components);
		return this;
	}
	
	@NotNull
	public MessageWrapper clearActionRows(){
		action = action.setActionRows(List.of());
		return this;
	}
	
	@NotNull
	public MessageWrapper content(String content){
		action = action.content(content);
		return this;
	}
	
	@NotNull
	public MessageWrapper setActionRows(@NotNull ActionRow... actionRows){
		action = action.setActionRows(actionRows);
		return this;
	}
	
	@NotNull
	public CompletableFuture<Message> submit(){
		return action.submit()
				.thenApply(message -> {
					log.info("Sent message to {} : {}", channel, message.getContentRaw());
					return message;
				});
	}
	
	@NotNull
	public CompletableFuture<Message> submitAndDelete(int minutes){
		return submit().thenApply(deleteMessageMins(minutes));
	}
}
