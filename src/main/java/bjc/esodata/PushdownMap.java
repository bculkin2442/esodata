package bjc.esodata;

import java.util.function.*;

import bjc.funcdata.*;

/**
 * A variant of a map where inserting a duplicate key shadows the existing value
 * instead of replacing it.
 *
 * This could be useful for things like variable scopes.
 *
 * @author EVE
 *
 * @param <KeyType>
 *                    The key of the map.
 *
 * @param <ValueType>
 *                    The values in the map.
 */
public class PushdownMap<KeyType, ValueType> implements IMap<KeyType, ValueType> {
	/* Our backing storage. */
	private final IMap<KeyType, Stack<ValueType>> backing;

	private boolean isFrozen    = false;
	private boolean thawEnabled = true;
	
	/** Create a new empty stack-based map. */
	public PushdownMap() {
		backing = new FunctionalMap<>();
	}

	@Override
	public void clear() {
		if (isFrozen) throw new ObjectFrozen("Can't clear frozen map");
		
		backing.clear();
	}

	@Override
	public boolean containsKey(final KeyType key) {
		return backing.containsKey(key);
	}
	
	@Override
	public void forEach(final BiConsumer<KeyType, ValueType> action) {
		backing.forEach((key, stk) -> action.accept(key, stk.top()));
	}

	@Override
	public ValueType get(final KeyType key) {
		return backing.get(key).top();
	}

	@Override
	public int size() {
		return backing.size();
	}

	@Override
	public IList<KeyType> keyList() {
		return backing.keyList();
	}

	@Override
	public ValueType put(final KeyType key, final ValueType val) {
		if (isFrozen) throw new ObjectFrozen("Can't insert key " + key + " into frozen map");
		
		if (backing.containsKey(key)) {
			final Stack<ValueType> stk = backing.get(key);

			final ValueType vl = stk.top();

			stk.push(val);

			return vl;
		}

		final Stack<ValueType> stk = new SimpleStack<>();

		stk.push(val);

		return null;
	}

	@Override
	public ValueType remove(final KeyType key) {
		if (isFrozen) throw new ObjectFrozen("Can't remove key " + key + " from frozen map");
	
		final Stack<ValueType> stk = backing.get(key);

		if (stk.size() > 1) return stk.pop();

		return backing.remove(key).top();
	}

	@Override
	public int hashCode() {
		final int prime = 31;

		int result = 1;
		result = prime * result + (backing == null ? 0 : backing.hashCode());

		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)                         return true;
		if (obj == null)                         return false;
		if (!(obj instanceof PushdownMap<?, ?>)) return false;

		final PushdownMap<?, ?> other = (PushdownMap<?, ?>) obj;

		if (backing == null) {
			if (other.backing != null) return false;
		} else if (!backing.equals(other.backing)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return String.format("PushdownMap [backing=%s]", backing);
	}

	@Override
	public boolean freeze() {
		isFrozen = true;
		
		return true;
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
	public boolean deepFreeze() {
		thawEnabled = true;
		
		return freeze();
	}
	
	@Override
	public boolean canFreeze() {
		return true;
	}
	
	@Override
	public boolean canThaw() {
		return thawEnabled;
	}
	
	@Override
	public boolean isFrozen() {
		return isFrozen;
	}
}
