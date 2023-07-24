package quest.laxla.trinketables.entity

import net.minecraft.entity.ai.goal.TemptGoal
import net.minecraft.entity.passive.TameableEntity
import net.minecraft.recipe.Ingredient

/**
 * This is a [TemptGoal] that only works when the [entity] is [tamed][TameableEntity.isTamed].
 */
class TamedTemptGoal(
    private val entity: TameableEntity,
    speed: Double,
    food: Ingredient,
    canBeScared: Boolean
) : TemptGoal(entity, speed, food, canBeScared) {
    override fun canStart(): Boolean {
        return super.canStart() && entity.isTamed
    }
}
