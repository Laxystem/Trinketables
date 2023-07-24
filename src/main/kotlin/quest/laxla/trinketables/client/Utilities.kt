package quest.laxla.trinketables.client

import dev.emi.trinkets.api.client.TrinketRenderer
import org.quiltmc.loader.api.minecraft.ClientOnly
import quest.laxla.trinketables.Trinketable

@get:ClientOnly
val Trinketable.renderer: TrinketRenderer? get() = rendererIdentifier?.let { TrinketablesClient.Registries.trinketableRenderer.get(it) }
