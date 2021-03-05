package fr.raksrinana.rsndiscord.command.impl;

import fr.raksrinana.rsndiscord.Main;
import fr.raksrinana.rsndiscord.command.BasicCommand;
import fr.raksrinana.rsndiscord.command.BotCommand;
import fr.raksrinana.rsndiscord.command.CommandResult;
import fr.raksrinana.rsndiscord.log.Log;
import fr.raksrinana.rsndiscord.permission.IPermission;
import fr.raksrinana.rsndiscord.permission.SimplePermission;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import static fr.raksrinana.rsndiscord.command.CommandResult.BAD_ARGUMENTS;
import static fr.raksrinana.rsndiscord.command.CommandResult.SUCCESS;
import static fr.raksrinana.rsndiscord.utils.LangUtils.translate;
import static java.util.concurrent.TimeUnit.SECONDS;

@BotCommand
public class ColorCommand extends BasicCommand{
	private final Random random;
	
	public ColorCommand(){
		random = new Random();
	}
	
	@Override
	public void addHelp(@NotNull Guild guild, @NotNull EmbedBuilder builder){
		super.addHelp(guild, builder);
		builder.addField("role", translate(guild, "command.color.help.role"), false)
				.addField("time", translate(guild, "command.color.help.time"), false);
	}
	
	@NotNull
	@Override
	public CommandResult execute(@NotNull GuildMessageReceivedEvent event, @NotNull LinkedList<String> args){
		super.execute(event, args);
		if(event.getMessage().getMentionedRoles().size() != 1){
			return BAD_ARGUMENTS;
		}
		args.poll();
		
		var targetRole = event.getMessage().getMentionedRoles().stream().findFirst().orElseThrow();
		return getArgumentAsInteger(args)
				.map(time -> {
					colorize(time, targetRole);
					return SUCCESS;
				})
				.orElse(BAD_ARGUMENTS);
	}
	
	private void colorize(int time, Role role){
		var originalColor = role.getColor();
		
		var executorService = Main.getExecutorService();
		var changeColorTask = executorService.scheduleAtFixedRate(() -> {
			var color = random.nextInt(0xffffff + 1);
			Log.getLogger(role.getGuild()).info("Setting {} color to {}", role, color);
			role.getManager().setColor(color).submit();
		}, 0, 15, TimeUnit.SECONDS);
		
		executorService.schedule(() -> {
			Log.getLogger(role.getGuild()).info("Stopping color change for {}", role);
			changeColorTask.cancel(false);
			while(!changeColorTask.isDone()){
				try{
					Thread.sleep(500);
				}
				catch(InterruptedException e){
					Log.getLogger(role.getGuild()).error("Failed to wait for role change task completion");
				}
				role.getManager().setColor(originalColor).submit();
			}
		}, time, SECONDS);
	}
	
	@NotNull
	@Override
	public String getCommandUsage(){
		return super.getCommandUsage() + " <@role> <time>";
	}
	
	@Override
	public @NotNull IPermission getPermission(){
		return new SimplePermission("command.color", false);
	}
	
	@NotNull
	@Override
	public List<String> getCommandStrings(){
		return List.of("color");
	}
	
	@NotNull
	@Override
	public String getName(@NotNull Guild guild){
		return translate(guild, "command.color.name");
	}
	
	@NotNull
	@Override
	public String getDescription(@NotNull Guild guild){
		return translate(guild, "command.color.description");
	}
}
