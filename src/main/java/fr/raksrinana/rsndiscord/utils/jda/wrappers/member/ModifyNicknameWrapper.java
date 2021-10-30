package fr.raksrinana.rsndiscord.utils.jda.wrappers.member;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

@Log4j2
public class ModifyNicknameWrapper{
	private final Member target;
	private final String nickname;
	private final AuditableRestAction<Void> action;
	
	public ModifyNicknameWrapper(@NotNull Guild guild, @NotNull Member target, @Nullable String nickname){
		this.target = target;
		this.nickname = nickname;
		action = guild.modifyNickname(target, nickname);
	}
	
	public CompletableFuture<Void> submit(){
		return action.submit()
				.thenAccept(empty -> log.info("Set nickname of {} to {}", target, nickname));
	}
}
