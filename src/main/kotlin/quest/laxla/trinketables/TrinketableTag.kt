package quest.laxla.trinketables

import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import kotlin.jvm.optionals.getOrElse

data class TrinketableTag internal constructor(
    val tag: TagKey<Item>,
    override val rendererIdentifier: Identifier? = null,
    override val behaviourIdentifier: Identifier? = null
) : Trinketable {
    private val tagContents by lazy { Registries.ITEM.getTag(tag).getOrElse { emptyList() }.map { it.value() } }

    override fun iterator() = tagContents.iterator()
}
