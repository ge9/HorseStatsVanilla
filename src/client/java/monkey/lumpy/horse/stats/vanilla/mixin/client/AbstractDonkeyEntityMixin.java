package monkey.lumpy.horse.stats.vanilla.mixin.client;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.DecimalFormat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.shedaniel.autoconfig.AutoConfig;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.gui.ToolTipGui;
import monkey.lumpy.horse.stats.vanilla.gui.TooltipDonkey;
import monkey.lumpy.horse.stats.vanilla.util.Converter;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.nbt.NbtCompound;

@Mixin(AbstractDonkeyEntity.class)
public abstract class AbstractDonkeyEntityMixin extends AbstractHorseEntity {
    @Shadow public abstract int getInventoryColumns();

    private ModConfig config;

    protected AbstractDonkeyEntityMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }


    @Inject(at = @At("HEAD"), method = "interactMob")
    public ActionResult interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ret) {
        if(config == null) {
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        }

        if (config.showValue() && !this.isTame() && player.shouldCancelInteraction() && (config == null || config.isTooltipEnabled())) {
            // Show tooltip
            DecimalFormat df = new DecimalFormat("#.#");
            String jumpStrength = df.format( Converter.jumpStrengthToJumpHeight(this.getAttributeValue(EntityAttributes.JUMP_STRENGTH)) );
            String maxHealth = df.format(this.getMaxHealth());

            NbtCompound entityNbt = new NbtCompound();
            this.writeNbt(entityNbt);
            int strength_int;
            if (this.getType() == EntityType.LLAMA || this.getType() == EntityType.TRADER_LLAMA) {
                strength_int = entityNbt.getInt("Strength");
            }else{
                strength_int = this.getInventoryColumns();
            }
            String strength = df.format(3L * strength_int);
            String speed = df.format(Converter.genericSpeedToBlocPerSec(this.getAttributes().getValue(EntityAttributes.MOVEMENT_SPEED)));
            
            double jumpValue = new BigDecimal(jumpStrength.replace(',', '.')).doubleValue();
            double speedValue = new BigDecimal(speed.replace(',', '.')).doubleValue();
            double healthValue = new BigDecimal(maxHealth.replace(',', '.')).doubleValue();
            int strengthValue = new BigDecimal(strength.replace(',', '.')).intValue();

            MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().setScreen(
                new ToolTipGui(new TooltipDonkey(speedValue, jumpValue, healthValue, strengthValue))
            ));
        }
        return ret.getReturnValue();
    }
}
