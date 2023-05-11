package tomsumner.chestbrowser;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.Items;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;


public class ChestBrowser implements ClientModInitializer {
	public static final String MOD_ID = "chestbrowser";
	private static List<BlockPos> selectedEntities = new ArrayList<>();
	public static final Logger LOGGER = LoggerFactory.getLogger("ChestBrowserMod");

	@Override
	public void onInitializeClient() {
		MinecraftClient mcClient = MinecraftClient.getInstance();
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
//			Exclude if block isn't BlockEntity

			BlockPos blockEntityPos = hitResult.getBlockPos();
			BlockEntity blockEntity = world.getBlockEntity(blockEntityPos);

			if (!(blockEntity instanceof ChestBlockEntity)) {
				return ActionResult.PASS;
			}
			if (!world.isClient) {
				ItemStack heldItem = player.getStackInHand(hand);
				if (heldItem.getItem().equals(Items.BAMBOO) && heldItem.hasCustomName() && heldItem.getName().getString().equals("tool")) {
					if (selectedEntities.contains(blockEntityPos)) {
						player.sendMessage(Text.of("deselected block at " + blockEntityPos.toString()), true);
						selectedEntities.remove(blockEntityPos);
					}
					LOGGER.info("Selected block at " + blockEntityPos.toString());
					player.sendMessage(Text.of("Selected block at " + blockEntityPos.toString()), true);

					selectedEntities.add(blockEntityPos);

					return ActionResult.CONSUME_PARTIAL;
				}
				player.sendMessage(Text.of("Use a bamboo named 'tool' to select."), true);
				return ActionResult.PASS;
			};
			return ActionResult.PASS;
		});
	}
}