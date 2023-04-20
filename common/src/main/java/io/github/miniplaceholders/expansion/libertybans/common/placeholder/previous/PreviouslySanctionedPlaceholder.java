package io.github.miniplaceholders.expansion.libertybans.common.placeholder.previous;

import io.github.miniplaceholders.api.utils.Components;
import io.github.miniplaceholders.expansion.libertybans.common.LibertyBansExpansion;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import space.arim.libertybans.api.*;

import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

public final class PreviouslySanctionedPlaceholder implements BiFunction<ArgumentQueue, Context, Tag> {
    public final LibertyBansExpansion expansion;
    public final PunishmentType punishmentType;

    public PreviouslySanctionedPlaceholder(LibertyBansExpansion expansion, PunishmentType type) {
        this.expansion = expansion;
        this.punishmentType = type;
    }

    @Override
    public Tag apply(ArgumentQueue argumentQueue, Context context) {
        final String type = argumentQueue.popOr("No provided Victim type").lowerValue();

        final Victim victim = switch (type) {
            case "ip" -> {
                final String ip = argumentQueue.popOr("No IP Provided").value();
                yield AddressVictim.of(NetworkAddress.of(ip.getBytes(StandardCharsets.UTF_8)));
            }
            case "player" -> PlayerVictim.of(expansion.parseArgumentUUID(argumentQueue, context));
            default -> throw context.newException("Invalid Victim Type provided");
        };

        final Integer punishments = expansion.getLibertyBans()
                .getSelector()
                .selectionBuilder()
                .victim(victim)
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
