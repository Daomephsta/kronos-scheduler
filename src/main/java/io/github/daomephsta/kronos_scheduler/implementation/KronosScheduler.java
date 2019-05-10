package io.github.daomephsta.kronos_scheduler.implementation;

import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;

public class KronosScheduler
{
	public static final String MOD_ID = "kronos_scheduler";

	public static void initialise()
	{
		WorldTickCallback.EVENT.register(ActionSchedulerImpl.getInstance()::tick);
	}
}
