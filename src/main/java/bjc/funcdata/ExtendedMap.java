package bjc.funcdata;

import java.util.*;
import java.util.function.*;

/**
 * An extended version of a map, that stores values into a map, but can look
 * into a different one for others.
 *
 * @author Ben Culkin
 *
 * @param <KeyType>
 *                    The type of the keys of the map.
 *
 * @param <ValueType>
 *                    The type of the values of the map.
 */
class ExtendedMap<KeyType, ValueType> implements IMap<KeyType, ValueType> {
	/* The map we delegate lookups to. */
	private final IMap<KeyType, ValueType> delegate;
	/* The map we store things in. */
	private final IMap<KeyType, ValueType> store;

	private boolean isFrozen     = false;
	private boolean thawEnabled  = true;
	
	/**
	 * Create a new extended map.
	 *
	 * @param delegate
	 *                 The map to lookup things in.
	 *
	 * @param store
	 *                 The map to store things in.
	 */
	public ExtendedMap(final IMap<KeyType, ValueType> delegate,
			final IMap<KeyType, ValueType> store) {
		this.delegate = delegate;
		this.store = store;
	}

	@Override
	public void clear() {
		if (isFrozen) return;
		
		store.clear();
	}

	@Override
	public boolean containsKey(final KeyType key) {
		if (store.containsKey(key)) return true;
		else                        return delegate.containsKey(key);
	}

	@Override
	public void forEach(final BiConsumer<KeyType, ValueType> action) {
		store.forEach(action);

		delegate.forEach(action);
	}

	@Override
	public void forEachKey(final Consumer<KeyType> action) {
		store.forEachKey(action);

		delegate.forEachKey(action);
	}

	@Override
	public void forEachValue(final Consumer<ValueType> action) {
		store.forEachValue(action);

		delegate.forEachValue(action);
	}

	@Override
	public ValueType get(final KeyType key) {
		if (store.containsKey(key)) return store.get(key);
		else                        return delegate.get(key);
	}

	@Override
	public int size() {
		return store.size() + delegate.size();
	}

	@Override
	public IList<KeyType> keyList() {
		IList<KeyType> ilst = new FunctionalList<>();

		ilst.addAll(store.keyList());
		ilst.addAll(delegate.keyList());

		return ilst;
	}

	@Override
	public ValueType put(final KeyType key, final ValueType val) {
		if (isFrozen)
			throw new ObjectFrozen("Can't insert key " + key + " into frozen map");
		
		return store.put(key, val);
	}

	@Override
	public ValueType remove(final KeyType key) {
		if (isFrozen)
			throw new ObjectFrozen("Can't remove key " + key + " from frozen map");
		
		if (!store.containsKey(key)) return delegate.remove(key);
		else                         return store.remove(key);
	}

	@Override
	public IList<ValueType> valueList() {
		IList<ValueType> ilst = new FunctionalList<>();

		ilst.addAll(store.valueList());
		ilst.addAll(delegate.valueList());

		return ilst;
	}
	
	@Override
  public int hashCode() {
		// isFrozen isn't counted
    return Objects.hash(delegate, store);
  }

	/* Misc. object support. */
  @Override
  public boolean equals(Object obj) {
  	if (this == obj)                  return true;
    if (obj == null)                  return false;
    if (getClass() != obj.getClass()) return false;

    ExtendedMap<?, ?> other = (ExtendedMap<?, ?>) obj;

		// isFrozen isn't counted
    return Objects.equals(delegate, other.delegate) && Objects.equals(store, other.store);
  }

  @Override
	public String toString() {
		return String.format("ExtendedMap [delegate=%s, store=%s]", delegate, store);
	}

  /* IFreezable support */
  
	@Override
	public boolean freeze() {
			isFrozen = true;
			
			return true;
	}

	@Override
	public boolean deepFreeze() {
  	thawEnabled  = false;

  	return freeze();
	}
	
	@Override
	public boolean thaw() {
		if (thawEnabled) {
			isFrozen = false;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isFrozen() {
		return isFrozen;
	}
	
	@Override
	public boolean canFreeze() {
		return true;
	}
	
	@Override
	public boolean canThaw() {
		return thawEnabled;
	}
}