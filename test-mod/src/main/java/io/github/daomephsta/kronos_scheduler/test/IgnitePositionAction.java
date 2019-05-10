package io.github.daomephsta.kronos_scheduler.test;

import io.github.daomephsta.kronos_scheduler.api.ActionSerialiser;
import io.github.daomephsta.kronos_scheduler.api.IPersistentAction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IgnitePositionAction implements IPersistentAction
{
	public static final ActionSerialiser<IgnitePositionAction> SERIALISER = new Serialiser();

	private final BlockPos position;

	public IgnitePositionAction(BlockPos position)
	{
		this.position = position;
	}

	@Override
	public void apply(World world)
	{
		BlockState fire = ((FireBlock) Blocks.FIRE).getStateForPosition(world, position);
		world.setBlockState(position, fire);
	}
	
	@Override
	@SuppressWarnings("unchecked") //If only java had self types
	public <T extends IPersistentAction> ActionSerialiser<T> getSerialiser()
	{
		return (ActionSerialiser<T>) SERIALISER;
	}
	
	private static class Serialiser extends ActionSerialiser<IgnitePositionAction>
	{
		@Override
		public void serialise(CompoundTag nbt, IgnitePositionAction action)
		{
			nbt.putInt("x", action.position.getX());
			nbt.putInt("y", action.position.getY());
			nbt.putInt("z", action.position.getZ());
		}

		@Override
		public IgnitePositionAction deserialise(CompoundTag nbt)
		{
			int x = nbt.getInt("x");
			int y = nbt.getInt("y");
			int z = nbt.getInt("z");
			return new IgnitePositionAction(new BlockPos(x, y, z));
		}	
	}
}

