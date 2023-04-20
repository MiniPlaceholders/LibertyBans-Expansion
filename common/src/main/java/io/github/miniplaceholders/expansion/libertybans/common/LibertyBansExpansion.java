package io.github.miniplaceholders.expansion.libertybans.common;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.expansion.libertybans.common.placeholder.actual.ProvidedVictimSanctionedPlaceholder;
import io.github.miniplaceholders.expansion.libertybans.common.placeholder.connected.ConnectedSanctionedPlaceholder;
import io.github.miniplaceholders.expansion.libertybans.common.placeholder.previous.PreviouslySanctionedPlaceholder;
import io.github.miniplaceholders.expansion.libertybans.common.placeholder.total.TotalActivePunishments;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.Nullable;
import space.arim.libertybans.api.LibertyBans;
import space.arim.libertybans.api.PunishmentType;
import space.arim.omnibus.OmnibusProvider;

import java.util.UUID;

public abstract class LibertyBansExpansion {
    private final LibertyBans libertyBans = OmnibusProvider.getOmnibus()
            .getRegistry()
            .getProvider(LibertyBans.class)
            .orElseThrow();

    public void register() {
        Expansion.builder("libertybans")
                .audiencePlaceholder("is_muted", new ConnectedSanctionedPlaceholder(this, PunishmentType.MUTE))
                .audiencePlaceholder("is_warned", new ConnectedSanctionedPlaceholder(this, PunishmentType.WARN))
                .globalPlaceholder("is_player_muted", new ProvidedVictimSanctionedPlaceholder(this, PunishmentType.MUTE))
                .globalPlaceholder("is_player_banned", new ProvidedVictimSanctionedPlaceholder(this, PunishmentType.BAN))
                .globalPlaceholder("has_been_kicked", new PreviouslySanctionedPlaceholder(this, PunishmentType.KICK))
                .globalPlaceholder("has_been_muted", new PreviouslySanctionedPlaceholder(this, PunishmentType.MUTE))
                .globalPlaceholder("has_been_banned", new PreviouslySanctionedPlaceholder(this, PunishmentType.BAN))
                .globalPlaceholder("has_been_warned", new PreviouslySanctionedPlaceholder(this, PunishmentType.WARN))
                .globalPlaceholder("total_active_bans", new TotalActivePunishments(libertyBans, PunishmentType.BAN))
                .globalPlaceholder("total_active_mutes", new TotalActivePunishments(libertyBans, PunishmentType.MUTE))
                //TODO:
                // Audience
                // - Current Punishment ID
                // - Current Punishment reason
                // Argument
                // - Punishment Expiration by ID
                .build()
                .register();
    }

    private static UUID parseUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public UUID parseArgumentUUID(ArgumentQueue queue, Context ctx) {
        final String argumentProvided = queue.popOr("").value();
        UUID uuid = parseUUID(argumentProvided);
        if (uuid == null) {
            uuid = provideUUIDByName(argumentProvided);
            if (uuid == null) {
                throw ctx.newException("");
            }
        }
        return uuid;
    }

    protected abstract @Nullable UUID provideUUIDByName(String name);

    public LibertyBans getLibertyBans() {
        return this.libertyBans;
    }
}
