package net.partyaddon.gui.old.widget;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.partyaddon.network.PartyAddonClientPacket;

public class KickButton extends WButton {

    private final int playerId;
    private final int size;
    private final boolean leaveGroup;

    public KickButton(int playerId, boolean leaveGroup, int size) {
        super();
        this.playerId = playerId;
        this.width = size;
        this.height = size;
        this.size = size;
        this.leaveGroup = leaveGroup;
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        InputResult result = super.onClick(x, y, button);
        if (result.equals(InputResult.PROCESSED)) {
            // PartyAddonClientPacket.writeC2SKickPlayerPacket(this.playerId, this.leaveGroup);
        }
        return result;
    }

    @Override
    public void setSize(int x, int y) {
        this.width = size;
        this.height = size;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        Icon icon;
        boolean hovered = (mouseX >= 0 && mouseY >= 0 && mouseX < getWidth() && mouseY < getHeight());
        icon = new TextureIcon(new Identifier("partyaddon:textures/gui/kick_icon.png"));
        if (hovered) {
            icon = new TextureIcon(new Identifier("partyaddon:textures/gui/hovered_kick_icon.png"));
        }

        icon.paint(matrices, x + 1, y + 1, this.width);
    }
}
