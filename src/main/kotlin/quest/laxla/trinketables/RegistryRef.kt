package quest.laxla.trinketables

import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

@JvmInline
value class RegistryRef<T>(val registry: Registry<T>) {
    infix fun T.at(identifier: Identifier) = Registry.register(registry, identifier, this)
}
