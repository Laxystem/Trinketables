package quest.laxla.trinketables

import net.minecraft.item.Item
import net.minecraft.util.Identifier

sealed interface Trinketable : Iterable<Item> {
    val rendererIdentifier: Identifier?
    val behaviourIdentifier: Identifier?

    companion object {
        operator fun invoke(block: TrinketableBuilder.() -> Unit) = TrinketableBuilder().apply(block)
    }
}

