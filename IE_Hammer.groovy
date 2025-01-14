import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock
import blusunrize.immersiveengineering.api.MultiblockHandler
import net.minecraft.entity.player.EntityPlayerMP
import blusunrize.immersiveengineering.common.util.advancements.IEAdvancements

/*
 * A script that makes items that are in "ieHammer" ore dictionary be able to form IE multiblocks.
 */
event_manager.listen(EventPriority.HIGH) { RightClickBlock event ->
	var stack = event.getItemStack()
	var player = event.getEntityPlayer()
	var world = event.getWorld()
	if (stack in ore("ieHammer")) {
		for (var mb : MultiblockHandler.getMultiblocks()) {
			if (mb.isBlockTrigger(world.getBlockState(event.getPos()))) {
				if (MultiblockHandler.postMultiblockFormationEvent(player, mb, event.getPos(), stack).isCanceled()) continue
				if (mb.createStructure(world, event.getPos(), event.getFace(), player)) {
					if (player instanceof EntityPlayerMP) IEAdvancements.TRIGGER_MULTIBLOCK.trigger((EntityPlayerMP) player, mb, stack);
					player.swingArm(event.getHand())
					event.setCanceled(true)
					return
				}
			}
		}
	}
}
