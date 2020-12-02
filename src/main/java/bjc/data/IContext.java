package bjc.data;

/**
 * Represents a 'context' which is a hierarchical set of objects.
 * @author Ben Culkin
 *
 */
public interface IContext {
	/**
	 * Register an object with this context.
	 * 
	 * @param name The name of the object.
	 * @param o The object to register.
	 */
	void register(String name, Object o);

	/**
	 * Get the parent of this context.
	 * 
	 * @return The parent of this context.
	 */
	IContext getParent();

	/**
	 * Get an object from this context.
	 * 
	 * @param name The name of the object.
	 * 
	 * @return The object bound to that name.
	 */
	Object get(String name);
	
	/**
	 * Get an object which is an instance of the provided class or a subclass
	 * thereof.
	 * 
	 * @param <T> The type of the object.
	 * 
	 * @param contract The class of the object.
	 * 
	 * @return An instance of the provided class.
	 */
	<T> T get(Class<T> contract);
	
	/**
	 * Get a named object which is an instance of the provided class or a subclass
	 * thereof.
	 * 
	 * @param <T> The type of the object.
	 * 
	 * @param name The name of the object
	 * @param contract The class of the object.
	 * 
	 * @return An instance of the provided class, with the given name..
	 */
	default <T> T get(String name, Class<T> contract) {
		Object obj = get(name);
		return obj == null 
				? getParent().get(name, contract) 
						: contract.cast(obj);
	};
}
