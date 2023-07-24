package quest.laxla.trinketables

import net.minecraft.item.Item
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

inline operator fun <T> Registry<T>.invoke(block: RegistryRef<T>.() -> Unit) = block(RegistryRef(this))

infix fun String.of(namespace: String) = Identifier(namespace, this)

infix fun <T> Identifier.of(registry: Registry<T>): TagKey<T> = TagKey.of(registry.key, this)!!

val Trinketable.behaviour: TrinketBehaviour? get() = behaviourIdentifier?.let { Trinketables.Registries.trinketableBehaviour.get(it) }
