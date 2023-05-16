package net.partyaddon.gui.old.widget;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class PartyLabel extends WLabel {

    private List<OrderedText> text = Lists.newArrayList();
    private int widthSize;

    public PartyLabel(Text text, int width) {
        super(text);
        this.width = width;
        this.widthSize = width;
    }

    public void addTooltipText(String... strings) {
        for (String string : strings)
            text.add(Text.of(string).asOrderedText());
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        for (int i = 0; i < text.size(); i++) {
            tooltip.add(text.get(i));
        }
    }

    @Override
    public void setSize(int x, int y) {
        this.width = this.widthSize;
        this.height = y;
    }

}
