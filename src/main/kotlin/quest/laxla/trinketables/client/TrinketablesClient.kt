package quest.laxla.trinketables.client

import com.mojang.serialization.Lifecycle
import dev.emi.trinkets.api.client.TrinketRenderer
import dev.emi.trinkets.api.client.TrinketRendererRegistry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.loader.api.minecraft.ClientOnly
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer
import quest.laxla.trinketables.Trinketables
import quest.laxla.trinketables.of

/**
 * Client-side mod initializer for [Trinketables], responsible for [trinketable][quest.laxla.trinketables.Trinketable] rendering.
 */
@ClientOnly
object TrinketablesClient : ClientModInitializer {

    /**
     * Client-side registries for [Trinketables].
     * @see [Trinketables.Registries]
     */
    @ClientOnly
    object Registries {
        @get:JvmName("getTrinketableRendererRegistry") @JvmStatic @ClientOnly
        val trinketableRenderer = SimpleRegistry<TrinketRenderer>(
            RegistryKey.ofRegistry("renderer" of Trinketables.modId),
            Lifecycle.stable(),
            false
        )
    }

    override fun onInitializeClient(mod: ModContainer?) {
        Trinketables.forEach { trinketable ->
            trinketable.forEach { item ->
                trinketable.renderer?.let { TrinketRendererRegistry.registerRenderer(item, it) }
            }
        }
    }
}
