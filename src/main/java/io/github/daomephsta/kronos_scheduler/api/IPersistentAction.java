package io.github.daomephsta.kronos_scheduler.api;

/**
 * Represents an action that persists between sessions.
 */
public interface IPersistentAction extends IAction
{
	/**
	 * @return the serialiser for this type of persistent action.
	 * The instance returned must also be registered in {@link ActionSerialiser#REGISTRY}.
	 * Due to limitations of Java's generics, implementations may need to cast their return value
	 * to {@code ActionSerialiser<T>}.
	 */
	public <T extends IPersistentAction> ActionSerialiser<T> getSerialiser();
}
