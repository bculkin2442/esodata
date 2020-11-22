package bjc.data;

import java.util.*;

public class Contexts {
	public static final IContext NULL = new NullContextImpl();

	private Contexts() {
		throw new UnsupportedOperationException();
	}

	public static IContext create() {
		return new ContextImpl(NULL);
	}

	public static IContext create(IContext parent) {
		return new ContextImpl(parent);
	}

	private static class NullContextImpl implements IContext {
		@Override
		public IContext getParent() {
			return this;
		}

		@Override
		public void register(String name, Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object get(String name) {
			throw new NoSuchElementException();
		}
		
		@Override
		public <T> T get(Class<T> contract) {
			throw new NoSuchElementException();
		}
	}

	private static class ContextImpl implements IContext {

		private final IContext parent;

		private final Map<String, Object> objects;

		public ContextImpl(IContext parent) {
			this.parent = parent;
			this.objects = new HashMap<String, Object>();
		}

		@Override
		public void register(String name, Object o) {
			objects.put(name, o);
		}

		@Override
		public Object get(String name) {
			if (objects.containsKey(name)) {
				return objects.get(name);
			}
			return parent.get(name);
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T get(Class<T> contract) {
			for (Object o : objects.values()) {
				if (contract.isInstance(o)) {
					return (T) o;
				}
			}
			return parent.get(contract);
		}

		@Override
		public IContext getParent() {
			return parent;
		}
	}
}
