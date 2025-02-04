/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.modules.owo;

import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import meteordevelopment.meteorclient.events.entity.player.AttackEntityEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
public class MaceDMG extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    
    private final Setting<Boolean> limit = sgGeneral.add(new BoolSetting.Builder()
        .name("limit")
        .description("Send limit y.")
        .defaultValue(true)
        .build()
    );
    private final Setting<Double> high = sgGeneral.add(new DoubleSetting.Builder()
        .name("High")
        .description("Send packet's high.")
        .defaultValue(20)
        .sliderRange(0, 200)
        .build()
    );
    private final Setting<Boolean> autoSwitch = sgGeneral.add(new BoolSetting.Builder()
        .name("autoSwitch")
        .description("Auto switch to Mace.")
        .defaultValue(true)
        .build()
    );
    private final Setting<Integer> switchDelay = sgGeneral.add(new IntSetting.Builder()
        .name("SwitchDelay")
        .description("SwitchDelay.")
        .defaultValue(50)
        .sliderRange(0, 100)
        .build()
    );
    private Timer timer;
    public MaceDMG() {
        super(Categories.Owo, "MaceDMG", "Just Test.");
    }
    @EventHandler
    private void onAttack(AttackEntityEvent event) {
        for(int i = 0; i < 4; i++)
			sendFakeY(0);
		int oldSlot = mc.player.getInventory().selectedSlot;
		if(autoSwitch.get()) {
			InvUtils.swap(this.getMaceslot(), false);
		}
		if(limit.get()) {
		    sendFakeY(Math.sqrt(500));
		} else {
		    sendFakeY(high.get());
		}
		sendFakeY(0);
		timer = new Timer();
		timer.schedule(new TimerTask() {
		    @Override
		    public void run() {
		        InvUtils.swap(oldSlot, false);
		    }
		}, switchDelay.get());
    }
    private void sendFakeY(double offset)
	{
		mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + offset, mc.player.getZ(), false));//this is copied from wurst
	}
	private int getMaceslot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof MaceItem) {
                return i;
            }
        }
        return mc.player.getInventory().selectedSlot;
    }
}