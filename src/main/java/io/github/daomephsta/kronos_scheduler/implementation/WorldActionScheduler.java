package io.github.daomephsta.kronos_scheduler.implementation;

import io.github.daomephsta.kronos_scheduler.api.ActionSerialiser;
import io.github.daomephsta.kronos_scheduler.api.IAction;
import io.github.daomephsta.kronos_scheduler.api.IPersistentAction;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

class WorldActionScheduler extends PersistentState
{
	private static final Logger LOGGER = LogManager.getLogger();
	private final World world;
	private Queue<TimestampedAction> actionQueue;

	WorldActionScheduler(World world)
	{
		super(getDataKey(world));
		this.world = world;
	}

	static String getDataKey(World world)
	{
		return KronosScheduler.MOD_ID + ":actions" + world.getDimension().getType().getSuffix();
	}

	void tick()
	{
		long timestamp = world.getTime();
		while(!actionQueue.isEmpty())
		{
			TimestampedAction action = actionQueue.peek();
			if (action.timestamp <= timestamp)
				actionQueue.remove().apply(world);
			else break;
		}
	}

	void scheduleAction(IAction action, long delay)
	{
		actionQueue.add(new TimestampedAction(action, world.getTime() + delay));
	}



	@Override
	public void fromTag(CompoundTag nbt)
	{
		this.actionQueue = nbt.getList("actions", NbtType.COMPOUND).stream()
				.map(tag ->
				{
					IAction action = actionFromTag(nbt.getCompound("action"));
					long timestamp = nbt.getLong("timestamp");
					if (action != null)
						return new TimestampedAction(action, timestamp);
					else
						return null;
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toCollection(PriorityQueue::new));
	}

	private IPersistentAction actionFromTag(CompoundTag nbt)
	{
		Identifier actionType = new Identifier(nbt.getString("action_type"));
		ActionSerialiser<?> serDes = ActionSerialiser.REGISTRY.get(actionType);
		if (serDes == null)
		{
			LOGGER.warn("No deserialiser found for the action type {}", actionType);
			return null;
		}
		return serDes.deserialise(nbt);
	}

	@Override
	public CompoundTag toTag(CompoundTag nbt)
	{
		nbt.put("actions", actionQueue.stream()
				.filter(tsa -> tsa.action instanceof IPersistentAction)
				.map(tsa ->
				{
					IPersistentAction persistentAction = (IPersistentAction) tsa.action;
					CompoundTag tsaTag = new CompoundTag();
					tsaTag.put("action", actionToTag(persistentAction));
					tsaTag.putLong("timestamp", tsa.timestamp);
					return tsaTag;
				})
				.collect(Collectors.toCollection(ListTag::new)));
		return nbt;
	}
	
	private CompoundTag actionToTag(IPersistentAction action)
	{
		CompoundTag nbt = new CompoundTag();
		ActionSerialiser<IPersistentAction> serDes = action.getSerialiser();
		Identifier typeId = ActionSerialiser.REGISTRY.getId(serDes);
		if (typeId == null)
		{
			LOGGER.warn("Could not get registry id for {}", action);
			return nbt;
		}
		nbt.putString("action_type", typeId.toString());
		serDes.serialise(nbt, action);
		return nbt;
	}

	private static class TimestampedAction implements Comparable<TimestampedAction>
	{
		private final IAction action;
		private final long timestamp;
		
		private TimestampedAction(IAction action, long timestamp)
		{
			this.action = action;
			this.timestamp = timestamp;
		}

		void apply(World world)
		{
			action.apply(world);
		}

		@Override
		public int compareTo(TimestampedAction other)
		{
			return Long.compare(this.timestamp, other.timestamp);
		}
	}
}
