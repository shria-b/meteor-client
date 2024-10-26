/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.modules.owo;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;

public class CancelPacket extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgC2S = settings.createGroup("C2S");
    private final SettingGroup sgS2C = settings.createGroup("S2C");
    
     private final Setting<When> when = sgGeneral.add(new EnumSetting.Builder<When>()
        .name("when")
        .description("when to cancel packet")
        .defaultValue(When.Always)
        .build()
    );
    
    private final Setting<Boolean> PlayerMoveC2SPacket = sgC2S.add(new BoolSetting.Builder()
        .name("PlayerMoveC2SPacket")
        .description("PlayerMoveC2SPacket")
        .defaultValue(true)
        .build()
    );
    private final Setting<Boolean> PlayerInteractBlockC2SPacket = sgC2S.add(new BoolSetting.Builder()
        .name("PlayerInteractBlockC2SPacket")
        .description("PlayerInteractBlockC2SPacket")
        .defaultValue(false)
        .build()
    );
    private final Setting<Boolean> BlockUpdateS2CPacket = sgS2C.add(new BoolSetting.Builder()
        .name("BlockUpdateS2CPacket")
        .description("BlockUpdateS2CPacket")
        .defaultValue(false)
        .build()
    );
    public CancelPacket() {
        super(Categories.Owo, "Cancelpacket", "cancel packet");
    }
    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if((PlayerMoveC2SPacket.get() && event.packet instanceof PlayerMoveC2SPacket ||
        PlayerInteractBlockC2SPacket.get() && event.packet instanceof PlayerInteractBlockC2SPacket)
         && this.getWhenCancelPacket()){
            event.cancel();
        }
    }
    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if((BlockUpdateS2CPacket.get() && event.packet instanceof BlockUpdateS2CPacket)
         && this.getWhenCancelPacket()) {
            event.cancel();
        }
    }
    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        toggle();
    }
    private boolean getWhenCancelPacket() {
        return (when.get() == When.Always ||
               when.get() == When.OffGround && !mc.player.isOnGround() ||
               when.get() == When.OnGround && mc.player.isOnGround());
    }
    public enum When {
        Always,
        OffGround,
        OnGround
    }
}
