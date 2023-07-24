package quest.laxla.trinketables

import net.minecraft.item.Item
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

data class TrinketableBuilder internal constructor(
    var renderer: Identifier? = null,
    var behaviour: Identifier? = null
) {
    infix fun registeredAs(item: Item) = TrinketableItem(item, renderer, behaviour).also { Trinketables.trinkets.add(it) }
    infix fun registeredAs(tag: TagKey<Item>) = TrinketableTag(tag, renderer, behaviour).also { Trinketables.trinkets.add(it) }
}
