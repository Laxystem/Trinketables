package quest.laxla.trinketables

import com.mojang.serialization.Lifecycle
import dev.emi.trinkets.api.Trinket
import dev.emi.trinkets.api.TrinketsApi
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.command.argument.ItemStackArgumentType
import net.minecraft.command.argument.ItemStackArgumentType.itemStack
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.text.Text
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.command.api.CommandRegistrationCallback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.streams.asSequence
import net.minecraft.item.Items as MinecraftItems
import net.minecraft.registry.Registries as MinecraftRegistries


typealias TrinketBehaviour = Trinket

/**
 * The Trinketables mod.
 *
 * For rendering, see [TrinketablesClient][quest.laxla.trinketables.client.TrinketablesClient].
 *
 * To add a new *simple* trinketable, simply add your item to the wanted tag. The TrinketKind will decide on its rendering.
 */
object Trinketables : ModInitializer, Iterable<Trinketable> {
    const val modId = "trinketables"
    const val hugeBackpack = "huge_backpack"
    const val leashLike = "leash_like"

    @JvmStatic
    lateinit var modName: String

    internal val logger: Logger = LoggerFactory.getLogger(modId)
    internal val trinkets = mutableListOf<Trinketable>()

    private const val item = "item"
    private val defaultTrinketBehaviour = object : Trinket { }

    object ItemGroups {
        @JvmStatic
        val trinketables = FabricItemGroup.builder().apply {
            name(Text.translatable("itemGroup.$modId.$modId"))

            icon {
                ItemStack(MinecraftItems.WHEAT).apply {
                    addEnchantment(Enchantments.UNBREAKING, 3)
                }
            }

        }.build()
    }

    object Tags {
        @JvmStatic
        val catPlayable = "cat_playable" of modId of MinecraftRegistries.ITEM
        @JvmStatic
        val tamable = "tamable" of modId of MinecraftRegistries.ITEM
    }

    @Suppress("unused")
    object Items

    object Registries {
        @get:JvmName("getTrinketableRendererRegistry") @JvmStatic
        val trinketableBehaviour: Registry<TrinketBehaviour> =
            SimpleRegistry(RegistryKey.ofRegistry("behaviour" of modId), Lifecycle.stable(), false)
    }

    override fun onInitialize(mod: ModContainer) {
        modName = mod.metadata()?.name().toString()

        logger.info("Fun fact: Steve is a wheat-head.")

        Trinketable {
            renderer = hugeBackpack of modId
        } registeredAs Tags.tamable

        Trinketable {
            renderer = leashLike of modId
        } registeredAs Tags.catPlayable

        MinecraftRegistries.ITEM_GROUP {
            ItemGroups.trinketables at (modId of modId) // trinketables:trinketables
        }

        val scanned = mutableListOf<Item>()
        ItemGroupEvents.modifyEntriesEvent(MinecraftRegistries.ITEM_GROUP.getKey(ItemGroups.trinketables).get()).register { content ->
            trinkets.asSequence().sortedBy { it is TrinketableItem }.forEach { trinketable ->
                content.addItem(MinecraftItems.WHITE_STAINED_GLASS_PANE)
                trinketable.asSequence()/*.filterNot { scanned.contains(it) }*/.forEach { item ->
                    content.addItem(MinecraftItems.LIGHT_GRAY_STAINED_GLASS_PANE)
                    content.addItem(item)
                    TrinketsApi.registerTrinket(item, trinketable.behaviour ?: defaultTrinketBehaviour)
                    scanned += item
                }
            }
        }

        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, buildContext, _ ->

            dispatcher.register(literal("tags").then(argument(item, itemStack(buildContext))).executes { context ->
                val item = ItemStackArgumentType.getItemStackArgument(context, item).item

                context.source.sendFeedback({
                    Text.literal(buildString {
                        appendLine("Tags of ${item.name}: ")

                        MinecraftRegistries.ITEM.tags.asSequence().map { it.second }.filter { tag ->
                            tag.any {
                                it.unwrap() === item
                            }
                        }.forEachIndexed { index, tag ->
                            if (index != 0) append(", ")

                            append(tag.key.id.toString())
                        }

                        append(';')
                    })
                }, false)

                0
            })
        })
    }

    override fun iterator(): Iterator<Trinketable> = trinkets.iterator()
}
