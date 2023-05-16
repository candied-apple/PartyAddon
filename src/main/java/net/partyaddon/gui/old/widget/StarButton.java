// package net.partyaddon.gui.old.widget;

// import io.github.cottonmc.cotton.gui.widget.WButton;
// import io.github.cottonmc.cotton.gui.widget.data.InputResult;
// import io.github.cottonmc.cotton.gui.widget.icon.Icon;
// import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
// import net.minecraft.client.util.math.MatrixStack;
// import net.minecraft.util.Identifier;
// import net.partyaddon.network.PartyAddonClientPacket;

// public class StarButton extends WButton {

//     private final int playerId;
//     private boolean activated;

//     public StarButton(int playerId, boolean activated) {
//         super();
//         this.playerId = playerId;
//         this.width = 7;
//         this.height = 7;
//         this.activated = activated;
//     }

//     public void setActivated(boolean activated) {
//         this.activated = activated;
//     }

//     @Override
//     public InputResult onClick(int x, int y, int button) {
//         InputResult result = super.onClick(x, y, button);
//         if (result.equals(InputResult.PROCESSED)) {
//             setActivated(!this.activated);
//             PartyAddonClientPacket.writeC2SChangeStarListPacket(this.playerId);
//         }
//         return result;
//     }

//     @Override
//     public void setSize(int x, int y) {
//         this.width = 7;
//         this.height = 7;
//     }

//     @Override
//     public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
//         Icon icon;
//         if (activated) {
//             icon = new TextureIcon(new Identifier("partyaddon:textures/gui/star_icon.png"));
//         } else {
//             icon = new TextureIcon(new Identifier("partyaddon:textures/gui/disabled_star_icon.png"));
//         }
//         if ((mouseX >= 0 && mouseY >= 0 && mouseX < getWidth() && mouseY < getHeight())) {
//             icon = new TextureIcon(new Identifier("partyaddon:textures/gui/hovered_star_icon.png"));
//         }
//         icon.paint(matrices, x + 1, y + 1, this.width);
//     }
// }
