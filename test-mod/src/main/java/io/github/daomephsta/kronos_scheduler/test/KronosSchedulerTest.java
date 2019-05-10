package io.github.daomephsta.kronos_scheduler.test;

import io.github.daomephsta.kronos_scheduler.api.ActionSerialiser;
import io.github.daomephsta.kronos_scheduler.api.IActionScheduler;
import io.github.daomephsta.kronos_scheduler.implementation.KronosScheduler;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.block.Blocks;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.explosion.Explosion;

public class KronosSchedulerTest
{
	public static void initialise()
	{
		Registry.register(ActionSerialiser.REGISTRY, new Identifier(KronosScheduler.MOD_ID, "ignite_position"),
				IgnitePositionAction.SERIALISER);
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) ->
		{
			IActionScheduler.getInstance().schedule(world, 60, schedulerWorld ->
			{
				schedulerWorld.createExplosion(player, player.x, player.y, player.z,
						1.5F, Explosion.DestructionType.BREAK);
			});
			return ActionResult.PASS;
		});
		AttackBlockCallback.EVENT.register((player, world, hand, position, direction) ->
		{
			if (world.getBlockState(position).getBlock() != Blocks.MAGMA_BLOCK)
				return ActionResult.PASS;
			Direction horizontalFacing = player.getHorizontalFacing();
			for (int i = 1; i <= 3; i++)
			{
				IActionScheduler.getInstance().schedule(world, 20 * i,
						new IgnitePositionAction(position.offset(horizontalFacing, i)));
			}
			return ActionResult.PASS;
		});
	}
}
