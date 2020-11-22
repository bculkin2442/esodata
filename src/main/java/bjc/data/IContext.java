package bjc.data;

public interface IContext {
	void register(String name, Object o);

	IContext getParent();

	Object get(String name);
	
	<T> T get(Class<T> contract);
	
	default <T> T get(String name, Class<T> contract) {
		Object obj = get(name);
		return obj == null 
				? getParent().get(name, contract) 
						: contract.cast(obj);
	};
}
