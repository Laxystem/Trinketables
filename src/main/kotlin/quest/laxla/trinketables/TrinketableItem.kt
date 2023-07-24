package quest.laxla.trinketables

import net.minecraft.item.Item
import net.minecraft.util.Identifier

data class TrinketableItem internal constructor(
    val item: Item,
    override val rendererIdentifier: Identifier? = null,
    override val behaviourIdentifier: Identifier? = null
) : Trinketable {
    override fun iterator(): Iterator<Item> = sequenceOf(item).iterator()
}
