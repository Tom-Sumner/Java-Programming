package tomsumner.chestbrowser;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;


public class ChestBrowser implements ClientModInitializer {
	public static final String MOD_ID = "chestbrowser";
	private static List<BlockPos> selectedEntities = new ArrayList<>();
	public static final Logger LOGGER = LoggerFactory.getLogger("ChestBrowserMod");

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			Timer toolTimer = new Timer();
			toolTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					// Check if the player is holding the browse tool
					PlayerEntity player = client.player;
					if (player != null) {
						ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);
						if (heldItem.getItem().getName().getString().equals("minecraft:bamboo") && heldItem.hasCustomName() && heldItem.getName().getString().equals("browse tool")) {
							player.sendMessage(Text.of("Chest Browse Tool Enabled"));
							LOGGER.info("Player is holding tool");
						}
						LOGGER.info("Player is holding " + heldItem.toString());
					}
				}
			}, 0, 2500);
		});
	}
}