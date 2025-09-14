package io.github.miniplaceholders.expansion.libertybans.common.placeholder.global;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github.miniplaceholders.api.resolver.GlobalTagResolver;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.NotNull;
import space.arim.libertybans.api.LibertyBans;
import space.arim.libertybans.api.PunishmentType;
import space.arim.libertybans.api.select.SelectionPredicate;

import java.time.Duration;

public class TotalActivePunishments implements GlobalTagResolver {
    private final PunishmentType type;
    private final LoadingCache<@NotNull PunishmentType, Integer> totalPunishmentsCache;

    public TotalActivePunishments(final LibertyBans libertyBans, final PunishmentType type) {
        this.type = type;
        this.totalPunishmentsCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(2))
                .refreshAfterWrite(Duration.ofMinutes(1))
                .build(punishmentType -> libertyBans.getSelector()
                        .selectionBuilder()
                        .victims(SelectionPredicate.matchingAll())
                        .type(punishmentType)
                        .selectActiveOnly()
                        .build()
                        .countNumberOfPunishments()
                        .toCompletableFuture()
                        .join()
                );
    }

    @Override
    public Tag tag(@NotNull ArgumentQueue argumentQueue, @NotNull Context context) {
        final Integer activePunishments = totalPunishmentsCache.get(type);
        if (activePunishments == null) {
            return Tag.preProcessParsed("0");
        }
        return Tag.preProcessParsed(Integer.toString(activePunishments));
    }
}
