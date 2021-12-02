package fr.raksrinana.rsndiscord.runner.impl;

import static java.awt.Color.GREEN;
import static java.util.concurrent.TimeUnit.HOURS;
import static net.dv8tion.jda.api.entities.MessageEmbed.DESCRIPTION_MAX_LENGTH;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import fr.raksrinana.rsndiscord.runner.api.IScheduledRunner;
import fr.raksrinana.rsndiscord.runner.api.ScheduledRunner;
import fr.raksrinana.rsndiscord.settings.Settings;
import fr.raksrinana.rsndiscord.settings.impl.guild.rss.RSSConfiguration;
import fr.raksrinana.rsndiscord.settings.types.ChannelConfiguration;
import fr.raksrinana.rsndiscord.utils.jda.JDAWrappers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import kong.unirest.Unirest;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@ScheduledRunner
@Log4j2
public class RSSRunner implements IScheduledRunner {

    @Override
    public void executeGlobal(@NotNull JDA jda) throws Exception {
    }

    @Override
    public void executeGuild(@NotNull Guild guild) throws Exception {
        var rssConfiguration = Settings.get(guild).getRss();
        rssConfiguration.getChannel()
            .flatMap(ChannelConfiguration::getChannel)
            .ifPresentOrElse(
                channel -> {
                    var feeds = rssConfiguration.getFeeds();
                    log.info("Processing {} RSS feeds for {}", feeds.size(), guild);
                    feeds.forEach(url -> processFeed(rssConfiguration, channel, url));
                },
                () -> log.warn("No RSS channel defined for {}", guild));
    }

    private void processFeed(@NotNull RSSConfiguration rssConfiguration, @NotNull TextChannel channel, @NotNull URL url) {
        var key = url.toString();
        log.info("Processing feed {} ({})", url, rssConfiguration.getFeedInfo(key).getTitle());

        var feedInfo = rssConfiguration.getFeedInfo(key);
        var lastDate = feedInfo.getLastPublicationDate();
        var entries = getFeedEntries(url);
        var newEntries = entries.stream()
            .sorted(this::sortByPublishDate)
            .filter(entry -> lastDate.map(date -> {
                    var entryTimestamp = Optional.ofNullable(entry.getPublishedDate())
                        .map(Date::toInstant)
                        .map(Instant::toEpochMilli)
                        .orElse(0L);
                    return date < entryTimestamp;
                })
                .orElse(true))
            .toList();

        log.info("Found {} new entries in feed {}", newEntries.size(), key);

        newEntries.forEach(entry -> publish(channel, entry));

        newEntries.stream()
            .flatMap(entry -> Optional.ofNullable(entry.getPublishedDate())
                .map(Date::toInstant)
                .map(Instant::toEpochMilli)
                .stream())
            .mapToLong(l -> l)
            .max()
            .ifPresent(feedInfo::setLastPublicationDate);

        newEntries.stream().findAny()
            .map(SyndEntry::getSource)
            .map(SyndFeed::getTitle)
            .ifPresent(feedInfo::setTitle);
    }

    @NotNull
    private List<SyndEntry> getFeedEntries(@NotNull URL url) {
        var response = Unirest.get(url.toString()).asBytes();
        if (!response.isSuccess()) {
            return List.of();
        }

        try {
            var reader = new XmlReader(new ByteArrayInputStream(response.getBody()));
            var feed = new SyndFeedInput().build(reader);
            return feed.getEntries();
        } catch (IOException | FeedException e) {
            log.error("Failed to parse RSS feed", e);
            return List.of();
        }
    }

    private void publish(@NotNull TextChannel channel, @NotNull SyndEntry entry) {
        var builder = new EmbedBuilder();
        builder.setColor(GREEN);
        builder.setTitle("RSS: " + entry.getSource().getTitle());
        Optional.ofNullable(entry.getDescription())
            .map(SyndContent::getValue)
            .map(desc -> desc.substring(0, Math.min(desc.length(), DESCRIPTION_MAX_LENGTH)))
            .ifPresent(builder::setDescription);
        Optional.ofNullable(entry.getAuthor()).ifPresent(builder::setAuthor);
        var categories = entry.getCategories().stream()
            .map(SyndCategory::getName)
            .collect(Collectors.joining(", "));
        if (!categories.isBlank()) {
            builder.addField("Category", categories, true);
        }
        Optional.ofNullable(entry.getLink()).ifPresent(link -> builder.addField("Link", link, true));
        Optional.ofNullable(entry.getPublishedDate())
            .map(Date::toInstant)
            .ifPresent(builder::setTimestamp);

        JDAWrappers.message(channel, builder.build()).submit();
    }

    private int sortByPublishDate(@NotNull SyndEntry entry1, @NotNull SyndEntry entry2) {
        var date1 = entry1.getPublishedDate();
        var date2 = entry2.getPublishedDate();

        if (Objects.nonNull(date1) && Objects.nonNull(date2)) {
            return date1.compareTo(date2);
        }
        if (Objects.nonNull(date1)) {
            return 1;
        }
        if (Objects.nonNull(date2)) {
            return -1;
        }
        return 0;
    }

    @Override
    public long getDelay() {
        return 0;
    }

    @Override
    public long getPeriod() {
        return 1;
    }

    @Override
    @NotNull
    public String getName() {
        return "rss";
    }

    @Override
    @NotNull
    public TimeUnit getPeriodUnit() {
        return HOURS;
    }
}
