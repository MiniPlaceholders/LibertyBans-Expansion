package io.github.miniplaceholders.expansion.libertybans.common.placeholder.global;

import io.github.miniplaceholders.expansion.libertybans.common.LibertyBansPlaceholders;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import space.arim.libertybans.api.AddressVictim;
import space.arim.libertybans.api.NetworkAddress;
import space.arim.libertybans.api.PlayerVictim;
import space.arim.libertybans.api.Victim;

import java.nio.charset.StandardCharsets;

public interface VictimProvider {
    default Victim victimFrom(
            final LibertyBansPlaceholders placeholders,
            final ArgumentQueue argumentQueue,
            final Context context
    ) {
        final String type = argumentQueue.popOr("No provided Victim type").lowerValue();

        return switch (type) {
            case "ip" -> {
                final String ip = argumentQueue.popOr("No IP Provided").value();
                yield AddressVictim.of(NetworkAddress.of(ip.getBytes(StandardCharsets.UTF_8)));
            }
            case "player" -> PlayerVictim.of(placeholders.parseNameArgument(argumentQueue, context));
            case "uuid", "id" -> PlayerVictim.of(placeholders.parseUUIDArgument(argumentQueue, context));
            default -> throw context.newException("Invalid Victim Type provided");
        };
    }
}
