// package net.partyaddon.gui.old.widget;

// import io.github.cottonmc.cotton.gui.widget.WButton;
// import io.github.cottonmc.cotton.gui.widget.data.InputResult;
// import io.github.cottonmc.cotton.gui.widget.icon.Icon;
// import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
// import net.minecraft.client.util.math.MatrixStack;
// import net.minecraft.util.Identifier;
// import net.partyaddon.network.PartyAddonClientPacket;

// public class AcceptDeclineButton extends WButton {

//     private final int invitationPlayerId;
//     private final int size;
//     private final boolean isAcceptButton;

//     public AcceptDeclineButton(boolean isAcceptButton, int invitationPlayerId, int size) {
//         super();
//         this.invitationPlayerId = invitationPlayerId;
//         this.width = size;
//         this.height = size;
//         this.size = size;
//         this.isAcceptButton = isAcceptButton;
//     }

//     @Override
//     public InputResult onClick(int x, int y, int button) {
//         InputResult result = super.onClick(x, y, button);
//         if (result.equals(InputResult.PROCESSED)) {
//             if (this.isAcceptButton) {
//                 PartyAddonClientPacket.writeC2SAcceptInvitationPacket(this.invitationPlayerId);
//             } else {
//                 PartyAddonClientPacket.writeC2SDeclineInvitationPacket(this.invitationPlayerId);
//             }
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
//         Icon icon;
//         boolean hovered = (mouseX >= 0 && mouseY >= 0 && mouseX < getWidth() && mouseY < getHeight());
//         if (this.isAcceptButton) {
//             if (this.isEnabled()) {
//                 icon = new TextureIcon(new Identifier("partyaddon:textures/gui/accept_icon.png"));
//                 if (hovered) {
//                     icon = new TextureIcon(new Identifier("partyaddon:textures/gui/hovered_accept_icon.png"));
//                 }
//             } else {
//                 icon = new TextureIcon(new Identifier("partyaddon:textures/gui/disabled_accept_icon.png"));
//             }
//         } else {
//             if (this.isEnabled()) {
//                 icon = new TextureIcon(new Identifier("partyaddon:textures/gui/decline_icon.png"));
//                 if (hovered) {
//                     icon = new TextureIcon(new Identifier("partyaddon:textures/gui/hovered_decline_icon.png"));
//                 }
//             } else {
//                 icon = new TextureIcon(new Identifier("partyaddon:textures/gui/disabled_decline_icon.png"));
//             }
//         }
//         icon.paint(matrices, x + 1, y + 1, this.width);
//     }
// }
