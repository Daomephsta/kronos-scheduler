package io.github.daomephsta.kronos_scheduler.api;

import io.github.daomephsta.kronos_scheduler.implementation.ActionSchedulerImpl;
import net.minecraft.world.World;

/**
 * The public interface for the action scheduler
 *
 * @author Daomephsta
 */
public interface IActionScheduler
{
	/**
	 * @return the action scheduler for the current active server
	 */
	public static IActionScheduler getInstance()
	{
		return ActionSchedulerImpl.getInstance();
	}

	/**
	 * Schedules an action to occur in the specified world after a delay
	 * @param world the world
	 * @param delay the delay in ticks
	 * @param action the action
	 */
	public void schedule(World world, long delay, IAction action);
}
