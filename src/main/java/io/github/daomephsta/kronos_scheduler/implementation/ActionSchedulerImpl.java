package io.github.daomephsta.kronos_scheduler.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import io.github.daomephsta.kronos_scheduler.api.IAction;
import io.github.daomephsta.kronos_scheduler.api.IActionScheduler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class ActionSchedulerImpl implements IActionScheduler
{
	private static final ActionSchedulerImpl INSTANCE = new ActionSchedulerImpl();
	
	private ActionSchedulerImpl() {}
	
	public static ActionSchedulerImpl getInstance()
	{
		return INSTANCE;
	}
	
	@Override
	public void schedule(World world, long delay, IAction action)
	{
		if (world instanceof ServerWorld)
		{
			PersistentStateManager persistentStateManager = ((ServerWorld) world).getPersistentStateManager();
			WorldActionScheduler worldActionScheduler = persistentStateManager.getOrCreate(() ->
					new WorldActionScheduler(world), WorldActionScheduler.getDataKey(world));
			worldActionScheduler.scheduleAction(action, delay);
		}
	}
	
	public void tick(World world)
	{
		if (world instanceof ServerWorld)
		{
			PersistentStateManager persistentStateManager = ((ServerWorld) world).getPersistentStateManager();
			WorldActionScheduler worldActionScheduler = persistentStateManager.getOrCreate(() ->
					new WorldActionScheduler(world), WorldActionScheduler.getDataKey(world));
			worldActionScheduler.tick();
		}
	}
}