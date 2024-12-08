package monkey.lumpy.horse.stats.vanilla.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.math.Color;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import net.minecraft.text.Text;

public class TooltipPanda extends LightweightGuiDescription {
    private ModConfig config;

    public TooltipPanda(String main, String hidden) {
        super();
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        WBox root = new WBox(Axis.VERTICAL);
        setRootPanel(root);
        root.setSpacing(-8);
        root.setInsets(new Insets(5,5,0,5));
        root.setSize(65,30);//this 30 seems effectively ignored

        Color mainColor = config.getNeutralColor();
        Color hiddenColor = config.getNeutralColor();

        WLabel mainSymbol = new WLabel(Text.literal("[main]"), mainColor.hashCode());
        WLabel mainLabel = new WLabel(Text.literal(main), mainColor.hashCode());
        WLabel hiddenSymbol = new WLabel(Text.literal("[hidden]"), hiddenColor.hashCode());
        WLabel hiddenLabel = new WLabel(Text.literal(hidden), hiddenColor.hashCode());

        root.add(mainSymbol);
        root.add(mainLabel);
        root.add(hiddenSymbol);
        root.add(hiddenLabel);
        root.validate(this);
    }
}
