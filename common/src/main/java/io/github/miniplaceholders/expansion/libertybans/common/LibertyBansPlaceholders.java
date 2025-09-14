package io.github.miniplaceholders.expansion.libertybans.common;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.expansion.libertybans.common.placeholder.audience.IsSanctionedPlaceholder;
import io.github.miniplaceholders.expansion.libertybans.common.placeholder.global.PreviouslySanctionedPlaceholder;
import io.github.miniplaceholders.expansion.libertybans.common.placeholder.global.ProvidedVictimSanctionedPlaceholder;
import io.github.miniplaceholders.expansion.libertybans.common.placeholder.global.TotalActivePunishments;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.arim.libertybans.api.LibertyBans;
import space.arim.libertybans.api.PunishmentType;
import space.arim.libertybans.api.Victim;
import space.arim.libertybans.api.punish.Punishment;
import space.arim.omnibus.OmnibusProvider;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public abstract class LibertyBansPlaceholders {
    private final LibertyBans libertyBans = OmnibusProvider.getOmnibus()
        .getRegistry()
        .getProvider(LibertyBans.class)
        .orElseThrow();
    private final LoadingCache<@NotNull Victim, List<Punishment>> punishmentCache = Caffeine.newBuilder()
            .maximumSize(120)
            .expireAfterWrite(Duration.ofMinutes(1))
            .refreshAfterWrite(Duration.ofSeconds(30))
            .build(victim -> libertyBans.getSelector()
                        .selectionBuilder()
                        .victim(victim)
                        .build()
                        .getAllSpecificPunishments()
                        .toCompletableFuture()
                        .join()
            );

    public Expansion provideExpansion() {
        return Expansion.builder("libertybans")
                .version(Constants.VERSION)
                .author("MiniPlaceholders Contributors")
                .audiencePlaceholder("is_muted", new IsSanctionedPlaceholder(this, PunishmentType.MUTE))
                .audiencePlaceholder("is_warned", new IsSanctionedPlaceholder(this, PunishmentType.WARN))
                .globalPlaceholder("is_player_muted", new ProvidedVictimSanctionedPlaceholder(this, PunishmentType.MUTE))
                .globalPlaceholder("is_player_banned", new ProvidedVictimSanctionedPlaceholder(this, PunishmentType.BAN))
                .globalPlaceholder("has_been_kicked", new PreviouslySanctionedPlaceholder(this, PunishmentType.KICK))
                .globalPlaceholder("has_been_muted", new PreviouslySanctionedPlaceholder(this, PunishmentType.MUTE))
                .globalPlaceholder("has_been_banned", new PreviouslySanctionedPlaceholder(this, PunishmentType.BAN))
                .globalPlaceholder("has_been_warned", new PreviouslySanctionedPlaceholder(this, PunishmentType.WARN))
                .globalPlaceholder("total_active_bans", new TotalActivePunishments(libertyBans, PunishmentType.BAN))
                .globalPlaceholder("total_active_mutes", new TotalActivePunishments(libertyBans, PunishmentType.MUTE))
                .build();
    }

    private static UUID parseUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public UUID parseUUIDArgument(ArgumentQueue queue, Context ctx) {
        final String argumentProvided = queue.popOr("You need to provide an UUID or a Name").value();
        UUID uuid = parseUUID(argumentProvided);
        if (uuid == null) {
            throw ctx.newException("Invalid UUID");
        }
        return uuid;
    }

    public UUID parseNameArgument(ArgumentQueue queue, Context ctx) {
        final String argumentProvided = queue.popOr("You need to provide a Player Name").value();
        final UUID uuid = provideUUIDByName(argumentProvided);
        if (uuid == null) {
            throw ctx.newException("Invalid Player Name");
        }
        return uuid;
    }

    protected abstract @Nullable UUID provideUUIDByName(String name);

    public LibertyBans getLibertyBans() {
        return this.libertyBans;
    }

    public LoadingCache<@NotNull Victim, List<Punishment>> punishmentCache() {
        return this.punishmentCache;
    }
}
