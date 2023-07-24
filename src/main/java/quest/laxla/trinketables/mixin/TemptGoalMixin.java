package quest.laxla.trinketables.mixin;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TemptGoal.class)
public abstract class TemptGoalMixin {
	@Shadow
	@Final
	private Ingredient food;

	@SuppressWarnings("OptionalGetWithoutIsPresent") // Intellij IDEA bug
	@Inject(method = "isTemptedBy", at = @At("RETURN"), cancellable = true)
	void trinketablesIsTemptedBy(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		final var trinkets = TrinketsApi.getTrinketComponent(entity);

		if (trinkets.isPresent()) {
			cir.setReturnValue(cir.getReturnValue() || !TrinketsApi.getTrinketComponent(entity).get().getEquipped(it -> food.test(it)).isEmpty());
		}
	}
}
