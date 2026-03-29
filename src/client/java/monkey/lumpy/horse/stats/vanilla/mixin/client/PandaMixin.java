package monkey.lumpy.horse.stats.vanilla.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.shedaniel.autoconfig.AutoConfig;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.gui.ToolTipGui;
import monkey.lumpy.horse.stats.vanilla.gui.TooltipPanda;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

@Mixin(Panda.class)
public abstract class PandaMixin {

    private ModConfig config;
    @Shadow
    public abstract Panda.Gene getMainGene();
    @Shadow
    public abstract Panda.Gene getHiddenGene();

    @Inject(at = @At("HEAD"), method = "mobInteract")
    public void interactMob(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> ret) {
        if(config == null) {
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        }

        if (config.showValue() && player.isSecondaryUseActive() && (config == null || config.isTooltipEnabled())) {
            Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(
                    new ToolTipGui(new TooltipPanda(this.getMainGene().getSerializedName(), this.getHiddenGene().getSerializedName()))
            ));
        }
    }
}
