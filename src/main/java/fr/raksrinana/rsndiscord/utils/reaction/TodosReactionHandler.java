package fr.raksrinana.rsndiscord.utils.reaction;

import fr.raksrinana.rsndiscord.settings.Settings;
import fr.raksrinana.rsndiscord.settings.guild.WaitingReactionMessageConfiguration;
import fr.raksrinana.rsndiscord.settings.types.ChannelConfiguration;
import fr.raksrinana.rsndiscord.utils.Actions;
import fr.raksrinana.rsndiscord.utils.BasicEmotes;
import lombok.NonNull;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import java.util.Objects;
import java.util.Optional;
import static fr.raksrinana.rsndiscord.utils.BasicEmotes.CHECK_OK;
import static fr.raksrinana.rsndiscord.utils.BasicEmotes.PAPERCLIP;

public class TodosReactionHandler implements ReactionHandler{
	@Override
	public boolean acceptTag(@NonNull ReactionTag tag){
		return Objects.equals(tag, ReactionTag.TODO);
	}
	
	@Override
	public ReactionHandlerResult accept(@NonNull GuildMessageReactionAddEvent event, @NonNull WaitingReactionMessageConfiguration reaction){
		if(event.getReactionEmote().isEmoji()){
			final var emote = BasicEmotes.getEmote(event.getReactionEmote().getEmoji());
			if(isValidEmote(emote)){
				return processTodoCompleted(event, emote, reaction);
			}
		}
		return ReactionHandlerResult.PROCESSED;
	}
	
	protected boolean isValidEmote(@NonNull BasicEmotes emote){
		return emote == CHECK_OK || emote == PAPERCLIP;
	}
	
	@Override
	public int getPriority(){
		return 1000;
	}
	
	protected ReactionHandlerResult processTodoCompleted(@NonNull GuildMessageReactionAddEvent event, @NonNull BasicEmotes emote, @NonNull WaitingReactionMessageConfiguration todo){
		return todo.getMessage().getMessage().map(message -> {
			if(emote == PAPERCLIP){
				final var forwarded = Optional.ofNullable(Settings.get(event.getGuild()).getReactionsConfiguration().getSavedForwarding().get(new ChannelConfiguration(event.getChannel()))).flatMap(ChannelConfiguration::getChannel).map(forwardChannel -> {
					Actions.sendMessage(forwardChannel, message.getContentRaw(), message.getEmbeds().stream().findFirst().orElse(null));
					return true;
				}).orElse(false);
				if(!forwarded){
					Actions.replyWithPrivateMessage(event, "The saved channel isn't configured yet for this channel, please contact and admin.", null);
					Actions.removeReaction(event.getReaction(), event.getUser());
					return ReactionHandlerResult.PROCESSED;
				}
			}
			if(message.isPinned()){
				Actions.unpin(message);
			}
			if(Optional.ofNullable(todo.getData().get(ReactionUtils.DELETE_KEY)).map(Boolean::parseBoolean).orElse(false)){
				Actions.deleteMessage(message);
			}
			else{
				Actions.editMessage(message, BasicEmotes.OK_HAND.getValue() + " __**DONE**__:  " + message.getContentRaw());
				Actions.clearReactions(message);
			}
			return ReactionHandlerResult.PROCESSED_DELETE;
		}).orElse(ReactionHandlerResult.PROCESSED);
	}
}
