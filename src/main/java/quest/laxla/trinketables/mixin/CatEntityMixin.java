package quest.laxla.trinketables.mixin;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import quest.laxla.trinketables.Trinketables;
import quest.laxla.trinketables.entity.TamedTemptGoal;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin extends TameableEntity {

	@SuppressWarnings("DataFlowIssue")
	private CatEntityMixin() {
		super(null,null);
	}

	@Inject(method = "initGoals", at = @At("HEAD"))
	void trinketablesInitGoals(CallbackInfo ci) {
		goalSelector.add(4, new TamedTemptGoal(this, 1.5, Ingredient.ofTag(Trinketables.Tags.getCatPlayable()), false));
		Trinketables.INSTANCE.getLogger$trinketables().debug("Cats will now chase anything trinketables:cat_playable if tamed and will purr when succeeding	");
	}
}
