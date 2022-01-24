package io.socol.opticubes.mixins;

import io.socol.opticubes.service.editing.ClientOptiCubeEditingService;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryPlayer.class)
public class InventoryPlayerMixin {

    @Inject(method = "changeCurrentItem", at = @At(value = "HEAD"), cancellable = true)
    public void onWheelScroll(int i, CallbackInfo ci) {
        if (ClientOptiCubeEditingService.getInstance().onWheelScroll(i)) {
            ci.cancel();
        }
    }
}
