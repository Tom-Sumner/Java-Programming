package tomsumner.chestbrowser;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class ChestBrowser implements ClientModInitializer {
		public static final String MOD_ID = "chestbrowser";
		private final List<BlockPos> selectedEntities = new ArrayList<>();
		public static final Logger LOGGER = LoggerFactory.getLogger("ChestBrowserMod");

		@Override
		public void onInitializeClient() {
			MinecraftClient mcClient = MinecraftClient.getInstance();
			UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
				BlockPos blockEntityPos = hitResult.getBlockPos();
				BlockEntity blockEntity = world.getBlockEntity(blockEntityPos);

				if (!(blockEntity instanceof ChestBlockEntity)) {
					return ActionResult.PASS;
				}
				if (!world.isClient) {
					ItemStack heldItem = player.getStackInHand(hand);
					if (heldItem.getItem().equals(Items.BAMBOO) && heldItem.hasCustomName() && heldItem.getName().getString().equals("tool")) {
						String posAsString = PosAsString(blockEntityPos);
						if (selectedEntities.contains(blockEntityPos)) {
							player.sendMessage(Text.of("Deselected block at " + posAsString), true);
							LOGGER.info(selectedEntities.toString());
							selectedEntities.remove(blockEntityPos);
						} else {
							LOGGER.info("Selected block at " + posAsString);
							LOGGER.info(selectedEntities.toString());
							player.sendMessage(Text.of("Selected block at " + posAsString), true);

							selectedEntities.add(blockEntityPos);

							// Check for neighboring chest blocks
							if (isDoubleChest(world, blockEntityPos)) {
								BlockPos neighborPos = getNeighborChestPosition(blockEntityPos, world);
								selectedEntities.add(neighborPos);
								LOGGER.info("Selected neighbor block at " + PosAsString(neighborPos));
								player.sendMessage(Text.of("Selected neighbor block at " + PosAsString(neighborPos)), true);
							}
						}

						return ActionResult.CONSUME_PARTIAL;
					}
					return ActionResult.PASS;
				}
				return ActionResult.PASS;
			});
		}

		public String PosAsString(BlockPos pos) {
			return "X: %d Y:%d Z:%d".formatted(pos.getX(), pos.getY(), pos.getZ());
		}

		public boolean isDoubleChest(World world, BlockPos chestPos) {
			// Check if the neighboring block in the east or west direction is also a chest
			BlockState eastBlockState = world.getBlockState(chestPos.east());
			BlockState westBlockState = world.getBlockState(chestPos.west());
			return eastBlockState.getBlock() instanceof ChestBlock || westBlockState.getBlock() instanceof ChestBlock;
		}

		public BlockPos getNeighborChestPosition(BlockPos chestPos, World world) {
			// Check if the neighboring block in the east or west direction is also a chest
			if (world.getBlockState(chestPos.east()).getBlock() instanceof ChestBlock) {
				return chestPos.east();
			} else if (world.getBlockState(chestPos.west()).getBlock() instanceof ChestBlock) {
				return chestPos.west();
			}
			return null;
		}
	}
