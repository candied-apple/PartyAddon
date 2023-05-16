// package net.partyaddon.gui.old.widget;

// import io.github.cottonmc.cotton.gui.widget.WButton;
// import io.github.cottonmc.cotton.gui.widget.data.InputResult;
// import io.github.cottonmc.cotton.gui.widget.icon.Icon;
// import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
// import net.minecraft.client.util.math.MatrixStack;
// import net.minecraft.util.Identifier;
// import net.partyaddon.network.PartyAddonClientPacket;

// public class PartyButton extends WButton {

//     private final int playerId;
//     private final int size;
//     private final boolean isInGroup;
//     private int requestTicker = 0;

//     public PartyButton(int playerId, int size, boolean isInGroup) {
//         super();
//         this.playerId = playerId;
//         this.width = size;
//         this.height = size;
//         this.size = size;
//         this.isInGroup = isInGroup;
//     }

//     @Override
//     public InputResult onClick(int x, int y, int button) {
//         InputResult result = super.onClick(x, y, button);
//         if (result.equals(InputResult.PROCESSED)) {
//             PartyAddonClientPacket.writeC2SInvitePlayerToGroupPacket(this.playerId);
//             this.setEnabled(false);
//             requestTicker = 1200;
//         }
//         return result;
//     }

//     @Override
//     public void setSize(int x, int y) {
//         this.width = size;
//         this.height = size;
//     }

//     @Override
//     public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
//         if (requestTicker > 0) {
//             requestTicker--;
//             if (requestTicker == 1 && !isInGroup) {
//                 this.setEnabled(true);
//             }
//         }
//         Icon icon;
//         if (this.isEnabled()) {
//             boolean hovered = (mouseX >= 0 && mouseY >= 0 && mouseX < getWidth() && mouseY < getHeight());
//             icon = new TextureIcon(new Identifier("partyaddon:textures/gui/plus_icon.png"));
//             if (hovered) {
//                 icon = new TextureIcon(new Identifier("partyaddon:textures/gui/hovered_plus_icon.png"));
//             }
//         } else {
//             icon = new TextureIcon(new Identifier("partyaddon:textures/gui/disabled_plus_icon.png"));
//         }
//         icon.paint(matrices, x + 1, y + 1, this.width);
//     }
// }
