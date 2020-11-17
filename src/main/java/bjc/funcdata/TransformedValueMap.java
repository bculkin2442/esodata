package bjc.funcdata;

import java.util.function.*;

/**
 * A map that transforms values from one type to another
 *
 * @author ben
 *
 * @param <OldKey>
 *                   The type of the map's keys
 *
 * @param <OldValue>
 *                   The type of the map's values
 *
 * @param <NewValue>
 *                   The type of the transformed values
 *
 */
final class TransformedValueMap<OldKey, OldValue, NewValue>
		implements IMap<OldKey, NewValue> {
	/* Our backing map. */
	private final IMap<OldKey, OldValue> backing;
	/* Our transforming function. */
	private final Function<OldValue, NewValue> transformer;

	private boolean isFrozen    = false;
	private boolean thawEnabled = true;
	
	/**
	 * Create a new transformed-value loop.
	 *
	 * @param backingMap
	 *                   The map to use as backing.
	 *
	 * @param transform
	 *                   The function to use for the transform.
	 */
	public TransformedValueMap(final IMap<OldKey, OldValue> backingMap,
			final Function<OldValue, NewValue> transform) {
		backing = backingMap;
		transformer = transform;
	}

	@Override
	public void clear() {
		if (isFrozen) throw new ObjectFrozen("Can't clear frozen map");
		
		backing.clear();
	}

	@Override
	public boolean containsKey(final OldKey key) {
		return backing.containsKey(key);
	}

	@Override
	public void forEach(final BiConsumer<OldKey, NewValue> action) {
		backing.forEach((key, value) -> {
			action.accept(key, transformer.apply(value));
		});
	}

	@Override
	public void forEachKey(final Consumer<OldKey> action) {
		backing.forEachKey(action);
	}

	@Override
	public void forEachValue(final Consumer<NewValue> action) {
		backing.forEachValue(value -> {
			action.accept(transformer.apply(value));
		});
	}

	@Override
	public NewValue get(final OldKey key) {
		return transformer.apply(backing.get(key));
	}

	@Override
	public int size() {
		return backing.size();
	}

	@Override
	public IList<OldKey> keyList() {
		return backing.keyList();
	}

	@Override
	public NewValue put(final OldKey key, final NewValue value) {
		throw new UnsupportedOperationException("Can't add items to transformed map");
	}

	@Override
	public NewValue remove(final OldKey key) {
		if (isFrozen) throw new ObjectFrozen("Can't remove key " + key + " from frozen map");
		
		return transformer.apply(backing.remove(key));
	}

	@Override
	public String toString() {
		return backing.toString();
	}

	@Override
	public IList<NewValue> valueList() {
		return backing.valueList().map(transformer);
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
		thawEnabled = false;
		
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