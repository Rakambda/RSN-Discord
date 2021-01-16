package fr.raksrinana.rsndiscord.command.impl;

import fr.raksrinana.rsndiscord.command.BasicCommand;
import fr.raksrinana.rsndiscord.command.BotCommand;
import fr.raksrinana.rsndiscord.command.CommandResult;
import fr.raksrinana.rsndiscord.command.DeleteMode;
import fr.raksrinana.rsndiscord.log.Log;
import fr.raksrinana.rsndiscord.permission.IPermission;
import fr.raksrinana.rsndiscord.permission.SimplePermission;
import fr.raksrinana.rsndiscord.settings.Settings;
import fr.raksrinana.rsndiscord.settings.guild.reaction.WaitingReactionMessageConfiguration;
import lombok.NonNull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static fr.raksrinana.rsndiscord.command.CommandResult.*;
import static fr.raksrinana.rsndiscord.command.DeleteMode.NEVER;
import static fr.raksrinana.rsndiscord.reaction.ReactionTag.MEDIA_REACTION;
import static fr.raksrinana.rsndiscord.reaction.ReactionUtils.DELETE_KEY;
import static fr.raksrinana.rsndiscord.utils.BasicEmotes.PACKAGE;
import static fr.raksrinana.rsndiscord.utils.LangUtils.translate;
import static java.lang.Character.isDigit;
import static java.util.stream.Collectors.joining;

@BotCommand
public class MediaReactionCommand extends BasicCommand{
	private static final String COMMENT_STR = "--";
	
	@Override
	public void addHelp(@NonNull Guild guild, @NonNull EmbedBuilder builder){
		super.addHelp(guild, builder);
		builder.addField("option", translate(guild, "command.media-reaction.help.option"), false)
				.addField("text", translate(guild, "command.media-reaction.help.text"), false);
	}
	
	@NonNull
	@Override
	public CommandResult execute(@NonNull final GuildMessageReceivedEvent event, @NonNull final LinkedList<String> args){
		super.execute(event, args);
		
		var guild = event.getGuild();
		var channel = event.getChannel();
		
		try{
			var hasEpisodes = true;
			var askArchive = false;
			var lines = new LinkedList<>(Arrays.asList(String.join(" ", args).strip().split("\n")));
			if(!lines.isEmpty()){
				var defaultDecorator = "||";
				if(lines.peek().startsWith("o")){
					var options = lines.pop();
					hasEpisodes = !options.contains("m");
					askArchive = options.contains("a");
					if(options.contains("c")){
						defaultDecorator = "";
					}
				}
				if(lines.isEmpty()){
					return BAD_ARGUMENTS;
				}
				var newText = "";
				if(hasEpisodes){
					newText += "__**EP " + lines.pop() + "**__";
				}
				if(!lines.isEmpty()){
					var finalDefaultDecorator = defaultDecorator;
					newText += "\n" + lines.stream().map(s -> {
						var decorator = finalDefaultDecorator;
						if(s.isBlank()){
							return "";
						}
						if(s.startsWith(COMMENT_STR)){
							s = s.substring(COMMENT_STR.length()).trim();
							decorator = "";
						}
						var parts = new LinkedList<>(Arrays.asList(s.split(" ", 2)));
						if(parts.isEmpty()){
							return s;
						}
						return convertTime(parts) + decorator + String.join(" ", parts) + decorator;
					}).collect(joining("\n"));
				}
				if(askArchive){
					newText += "\n\n" + translate(guild, "media-reaction.archive", PACKAGE.getValue());
				}
				var restMessage = channel.sendMessage(newText).submit();
				if(askArchive){
					restMessage = restMessage.thenApply(message -> {
						message.addReaction(PACKAGE.getValue()).submit();
						
						var reactionMessageConfiguration = new WaitingReactionMessageConfiguration(message, MEDIA_REACTION,
								Map.of(DELETE_KEY, Boolean.toString(false)));
						Settings.get(guild).addMessagesAwaitingReaction(reactionMessageConfiguration);
						
						return message;
					});
				}
				
				restMessage.thenAccept(message -> event.getMessage().delete().submit())
						.exceptionally(error -> {
							event.getChannel().sendMessage("Failed to send message: " + error.getMessage()).submit();
							return null;
						});
				return SUCCESS;
			}
			return BAD_ARGUMENTS;
		}
		catch(Exception e){
			Log.getLogger(guild).error("Failed to parse anime reaction", e);
			channel.sendMessage(translate(guild, "media-reaction.parse-error")).submit();
		}
		return FAILED;
	}
	
	private static String convertTime(LinkedList<String> stringList) throws IllegalArgumentException{
		if(stringList.size() < 1){
			return "";
		}
		
		var originalStr = stringList.peek();
		if(originalStr.isBlank() || originalStr.chars().anyMatch(c -> !isDigit(c))){
			return "N/A ";
		}
		stringList.pop();
		
		try{
			var str = originalStr.replace(":", "");
			if(str.length() <= 2){
				return String.format("00:%02d ", Integer.parseInt(str));
			}
			if(str.length() <= 4){
				var cut = str.length() - 2;
				return String.format("%02d:%02d ", Integer.parseInt(str.substring(0, cut)), Integer.parseInt(str.substring(cut)));
			}
			var cut = str.length() - 4;
			return String.format("%02d:%02d:%02d ",
					Integer.parseInt(str.substring(0, cut)),
					Integer.parseInt(str.substring(cut, str.length() - 2)),
					Integer.parseInt(str.substring(str.length() - 2)));
		}
		catch(Exception e){
			throw new IllegalArgumentException("Failed to parse " + originalStr, e);
		}
	}
	
	@Override
	public @NonNull String getCommandUsage(){
		return super.getCommandUsage() + "[o<option>] <text>";
	}
	
	@Override
	public DeleteMode getDeleteMode(){
		return NEVER;
	}
	
	@Override
	public @NonNull IPermission getPermission(){
		return new SimplePermission("command.media-reaction", false);
	}
	
	@NonNull
	@Override
	public String getName(@NonNull Guild guild){
		return translate(guild, "command.media-reaction.name");
	}
	
	@NonNull
	@Override
	public String getDescription(@NonNull Guild guild){
		return translate(guild, "command.media-reaction.description");
	}
	
	@NonNull
	@Override
	public List<String> getCommandStrings(){
		return List.of("mediareaction", "mr");
	}
}
