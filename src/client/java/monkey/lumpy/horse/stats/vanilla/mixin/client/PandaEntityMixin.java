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
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(PandaEntity.class)
public abstract class PandaEntityMixin{

    private ModConfig config;
    @Shadow
    public abstract PandaEntity.Gene getMainGene();
    @Shadow
    public abstract PandaEntity.Gene getHiddenGene();

    @Inject(at = @At("HEAD"), method = "interactMob")
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ret) {
        if(config == null) {
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        }

        if (config.showValue() && player.shouldCancelInteraction() && (config == null || config.isTooltipEnabled())) {
            MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().setScreen(
                    new ToolTipGui(new TooltipPanda(this.getMainGene().asString(), this.getHiddenGene().asString()))
            ));
        }
    }
}
