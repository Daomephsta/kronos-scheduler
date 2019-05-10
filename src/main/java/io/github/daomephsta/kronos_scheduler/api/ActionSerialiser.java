package io.github.daomephsta.kronos_scheduler.api;

import io.github.daomephsta.kronos_scheduler.implementation.KronosScheduler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;

/**
 * Defines how to convert a particular subtype of {@link IPersistentAction} to and from NBT.
 * A persistent action type's serialiser must be registered with {@link #REGISTRY} and
 * returned from that action's implementation of {@link IPersistentAction#getSerialiser()}.
 *
 * @param <T> the subtype of {@link IPersistentAction} this type can (de)serialise
 *
 * @author Daomephsta
 */
public abstract class ActionSerialiser<T extends IPersistentAction>
{
	/**The registry for action serialisers*/
	@SuppressWarnings("RedundantTypeArguments")
	public static final Registry<ActionSerialiser<?>> REGISTRY =
			Registry.<SimpleRegistry<ActionSerialiser<?>>>register(Registry.REGISTRIES, new Identifier(KronosScheduler.MOD_ID, "action_serialiser"), new SimpleRegistry<>());

	/**
	 * Writes an action of type {@link T} to the given NBT tag.
 	 * @param nbt the NBT tag to write to
	 * @param actionInstance the action to write
	 */
	public abstract void serialise(CompoundTag nbt, T actionInstance);

	/**
	 * Creates an action of type {@link T} from the given NBT tag.
	 * @param nbt the NBT tag to read from
	 */
	public abstract T deserialise(CompoundTag nbt);
}
