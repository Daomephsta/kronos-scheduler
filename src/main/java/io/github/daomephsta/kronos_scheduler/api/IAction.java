package io.github.daomephsta.kronos_scheduler.api;

import net.minecraft.world.World;

/**
 * A functional interface that represents an action to apply to a {@link World} at some point in the future.
 * Actions are purely server-side, and do not persist unless they implement {@link IPersistentAction}.
 *
 * @see IPersistentAction
 *
 * @author Daomephsta
 */
@FunctionalInterface
public interface IAction
{
	/**
	 * Applies this action to the given {@link World}
	 *
	 * @param world the world to apply this action to
	 */
	public void apply(World world);
}
