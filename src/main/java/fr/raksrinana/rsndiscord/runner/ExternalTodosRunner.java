package fr.raksrinana.rsndiscord.runner;

import fr.raksrinana.rsndiscord.api.externaltodos.ExternalTodosApi;
import fr.raksrinana.rsndiscord.button.impl.ExternalTodoCompletedButtonHandler;
import fr.raksrinana.rsndiscord.button.impl.ExternalTodoDiscardedButtonHandler;
import fr.raksrinana.rsndiscord.settings.Settings;
import fr.raksrinana.rsndiscord.settings.types.ChannelConfiguration;
import fr.raksrinana.rsndiscord.utils.jda.JDAWrappers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import static fr.raksrinana.rsndiscord.api.externaltodos.data.Status.EXTERNAL;
import static java.util.concurrent.TimeUnit.MINUTES;

@ScheduledRunner
public class ExternalTodosRunner implements IScheduledRunner{
	private final JDA jda;
	
	public ExternalTodosRunner(@NotNull JDA jda){
		this.jda = jda;
	}
	
	@Override
	public long getDelay(){
		return 2;
	}
	
	@Override
	public long getPeriod(){
		return 15;
	}
	
	@Override
	public void execute(){
		jda.getGuilds().forEach(guild -> {
			var configuration = Settings.get(guild).getExternalTodos();
			configuration.getEndpoint().ifPresent(endpoint -> {
				var token = configuration.getToken().orElse(null);
				
				configuration.getNotificationChannel()
						.flatMap(ChannelConfiguration::getChannel)
						.ifPresent(channel -> {
							var response = ExternalTodosApi.getTodos(endpoint, token);
							
							response.ifPresent(todos -> todos.getTodos()
									.forEach(todo -> {
										var components = new ArrayList<Component>();
										components.add(new ExternalTodoCompletedButtonHandler().asButton());
										
										if(todo.getKind().isCancellable()){
											components.add(new ExternalTodoDiscardedButtonHandler().asButton());
										}
										
										JDAWrappers.message(channel, "`" + todo.getKind().name() + "` => " + todo.getDescription())
												.addActionRow(components)
												.submit()
												.thenAccept(message -> ExternalTodosApi.setStatus(endpoint, token, todo, EXTERNAL));
									}));
						});
			});
		});
	}
	
	@NotNull
	@Override
	public String getName(){
		return "External todos fetcher";
	}
	
	@NotNull
	@Override
	public TimeUnit getPeriodUnit(){
		return MINUTES;
	}
}
