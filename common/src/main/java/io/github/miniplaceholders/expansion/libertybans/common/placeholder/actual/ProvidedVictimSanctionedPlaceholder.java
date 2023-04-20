package io.github.miniplaceholders.expansion.libertybans.common.placeholder.actual;

import io.github.miniplaceholders.api.utils.Components;
import io.github.miniplaceholders.expansion.libertybans.common.LibertyBansExpansion;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import space.arim.libertybans.api.PlayerVictim;
import space.arim.libertybans.api.PunishmentType;

import java.util.UUID;
import java.util.function.BiFunction;

public final class ProvidedVictimSanctionedPlaceholder implements BiFunction<ArgumentQueue, Context, Tag> {
    private final PunishmentType punishmentType;
    private final LibertyBansExpansion expansion;

    public ProvidedVictimSanctionedPlaceholder(LibertyBansExpansion expansion, PunishmentType type) {
        this.expansion = expansion;
        this.punishmentType = type;
    }

    @Override
    public Tag apply(ArgumentQueue argumentQueue, Context context) {
        final UUID uuid = expansion.parseArgumentUUID(argumentQueue, context);

        final Integer punishments = expansion.getLibertyBans()
                .getSelector()
                .selectionBuilder()
                .victim(PlayerVictim.of(uuid))
                .selectActiveOnly()
                .type(punishmentType)
                .build()
                .countNumberOfPunishments()
                .toCompletableFuture()
                .join();

        return Tag.selfClosingInserting(punishments == 0
                ? Components.TRUE_COMPONENT
                : Components.FALSE_COMPONENT);
    }
}
