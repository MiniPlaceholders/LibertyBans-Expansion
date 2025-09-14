package io.github.miniplaceholders.expansion.libertybans;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.api.provider.ExpansionProvider;
import io.github.miniplaceholders.api.provider.LoadRequirement;
import io.github.miniplaceholders.api.provider.PlatformData;
import io.github.miniplaceholders.api.types.Platform;
import io.github.miniplaceholders.expansion.example.velocity.VelocityProvider;
import io.github.miniplaceholders.expansion.libertybans.common.LibertyBansPlaceholders;
import io.github.miniplaceholders.expansion.libertybans.paper.PaperProvider;
import team.unnamed.inject.Inject;

public final class LibertyBansExpansionProvider implements ExpansionProvider {
    @Inject
    private PlatformData data;

    @Override
    public Expansion provideExpansion() {
        final LibertyBansPlaceholders placeholders = switch (MiniPlaceholders.platform()) {
            case PAPER -> new PaperProvider();
            case VELOCITY -> new VelocityProvider(data.serverInstance());
            // TODO: Sponge Platform
            case SPONGE -> null;
            default -> throw new UnsupportedOperationException("Unsupported Platform");
        };
        return placeholders.provideExpansion();
    }

    @Override
    public LoadRequirement loadRequirement() {
        return LoadRequirement.allOf(
                LoadRequirement.platform(Platform.PAPER, Platform.VELOCITY),
                LoadRequirement.requiredComplement("libertybans", "LibertyBans")
        );
    }
}
