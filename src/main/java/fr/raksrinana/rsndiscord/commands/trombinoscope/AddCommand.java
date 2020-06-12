package fr.raksrinana.rsndiscord.commands.trombinoscope;

import fr.raksrinana.rsndiscord.commands.generic.BasicCommand;
import fr.raksrinana.rsndiscord.commands.generic.Command;
import fr.raksrinana.rsndiscord.commands.generic.CommandResult;
import fr.raksrinana.rsndiscord.settings.Settings;
import fr.raksrinana.rsndiscord.settings.types.ChannelConfiguration;
import fr.raksrinana.rsndiscord.settings.types.RoleConfiguration;
import fr.raksrinana.rsndiscord.utils.Actions;
import fr.raksrinana.rsndiscord.utils.Utilities;
import fr.raksrinana.rsndiscord.utils.log.Log;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static fr.raksrinana.rsndiscord.utils.LangUtils.translate;
import static fr.raksrinana.rsndiscord.utils.TrombinoscopeUtils.isRegistered;
import static fr.raksrinana.rsndiscord.utils.Utilities.isModerator;
import static fr.raksrinana.rsndiscord.utils.Utilities.isTeam;

class AddCommand extends BasicCommand {
    private static final Path trombinoscopeFolder = Paths.get("trombinoscope");

    /**
     * Constructor.
     *
     * @param parent The parent command.
     */
    AddCommand(final Command parent) {
        super(parent);
    }

    @NonNull
    @Override
    public CommandResult execute(@NonNull final GuildMessageReceivedEvent event, @NonNull final LinkedList<String> args) {
        super.execute(event, args);
        if (event.getMessage().getAttachments().isEmpty()) {
            return CommandResult.BAD_ARGUMENTS;
        }
        var trombinoscope = Settings.get(event.getGuild()).getTrombinoscope();
        var target = event.getMessage().getMentionedMembers().stream().findFirst()
                .or(() -> Optional.ofNullable(event.getMember()))
                .orElseThrow(() -> new IllegalStateException("Failed to get member from event"));
        if (!Objects.equals(target, event.getMember()) && !isModerator(event.getMember())) {
            Actions.sendPrivateMessage(event.getAuthor(), translate(event.getGuild(), "trombinoscope.error.add-other"), null);
            return CommandResult.SUCCESS;
        }
        boolean failed = false;
        try {
            for (Message.Attachment attachment : event.getMessage().getAttachments()) {
                Log.getLogger(event.getGuild()).info("Downloading {}", attachment);
                var savedFile = attachment.downloadToFile(getFilePath(target, attachment))
                        .exceptionally(e -> {
                            Log.getLogger(event.getGuild()).error("Failed to save file", e);
                            Utilities.reportException(e);
                            return null;
                        })
                        .get(30, TimeUnit.SECONDS);
                if (checkFile(attachment, savedFile)) {
                    trombinoscope.registerPicture(target.getUser(), savedFile);
                } else {
                    failed = true;
                    if (Objects.nonNull(savedFile) && savedFile.exists()) {
                        savedFile.delete();
                    }
                }
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException e) {
            failed = true;
            Log.getLogger(event.getGuild()).error("Failed to save file", e);
            Utilities.reportException(e);
        }
        if (failed) {
            Actions.replyPrivate(event.getGuild(), event.getAuthor(), translate(event.getGuild(), "trombinoscope.error.save-error"), null);
        } else {
            trombinoscope.getPosterRole()
                    .flatMap(RoleConfiguration::getRole)
                    .ifPresent(role -> Actions.giveRole(target, role));
            trombinoscope.getPicturesChannel()
                    .flatMap(ChannelConfiguration::getChannel)
                    .ifPresent(channel -> Actions.sendMessage(channel, translate(event.getGuild(), "trombinoscope.picture-added", target.getAsMention(), trombinoscope.getPictures(target.getUser()).size()), null));
        }
        return CommandResult.SUCCESS;
    }

    private File getFilePath(Member member, Message.Attachment attachment) throws IOException {
        var path = trombinoscopeFolder.resolve(member.getId())
                .resolve(String.format("%d-%s", attachment.getIdLong(), attachment.getFileName()));
        Files.createDirectories(path.getParent());
        return path.toFile();
    }

    private boolean checkFile(Message.Attachment attachment, File savedFile) {
        return Objects.nonNull(savedFile) && savedFile.length() == attachment.getSize() && attachment.getSize() != 0;
    }

    @Override
    public boolean isAllowed(Member member) {
        if (super.isAllowed(member)) {
            return isRegistered(member) || isTeam(member);
        }
        return false;
    }

    @Override
    public boolean deleteCommandMessageImmediately() {
        return false;
    }

    @NonNull
    @Override
    public String getName(@NonNull Guild guild) {
        return translate(guild, "command.trombinoscope.add.name");
    }

    @NonNull
    @Override
    public List<String> getCommandStrings() {
        return List.of("add", "a");
    }

    @NonNull
    @Override
    public String getDescription(@NonNull Guild guild) {
        return translate(guild, "command.trombinoscope.add.description");
    }
}
