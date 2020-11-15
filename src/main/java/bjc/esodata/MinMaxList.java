package bjc.esodata;

import java.util.*;

// @FIXME Nov 15th, 2020 Ben Culkin :RecalcMinMax
// Is there some sort of way to avoid having to recalculate these elements when
// that element is removed?
/**
 * A list that automatically tracks the minimum & maximum element of a list.
 * 
 * @author Ben Culkin
 * 
 * @param <ValueType> The type of element stored in the list.
 *
 */
public class MinMaxList<ValueType> extends AbstractList<ValueType> {
	private final List<ValueType>       backing;
	private final Comparator<ValueType> picker;
	
	private ValueType minElement;
	private boolean recalcMin = false;
	
	private ValueType maxElement;
	private boolean recalcMax = false;
	
	// Create constructors
	
	/**
	 * Create a new min/max list using the given comparator.
	 * 
	 * @param picker The comparator to use to determine min/max elements.
	 */
	public MinMaxList(Comparator<ValueType> picker) {
		this(picker, new ArrayList<>());
	}
	
	/**
	 * Create a new min/max list using the given comparator.
	 * 
	 * @param picker The comparator to use to determine min/max elements.
	 * @param values The values to fill the list from.
	 */
	@SafeVarargs
	public MinMaxList(Comparator<ValueType> picker, ValueType... values) {
		this(picker);
		
		for (ValueType value : values) {
			add(value);
		}
	}
	/**
	 * Create a new min/max list using the given comparator.
	 * 
	 * @param picker The comparator to use to determine min/max elements.
	 * @param backing The collection to use values from.
	 */
	public MinMaxList(Comparator<ValueType> picker, Collection<ValueType> backing) {
		this(picker, new ArrayList<>(backing));
	}
	
	/**
	 * Create a new min/max list using the given comparator.
	 * 
	 * @param picker The comparator to use to determine min/max elements.
	 * @param backing The list to use as a backing list.
	 */
	public MinMaxList(Comparator<ValueType> picker, List<ValueType> backing) {
		this.backing = backing;
		this.picker  = picker;
		
		calculateBoth();
	}
	
	@Override
	public ValueType get(int index) {
		return backing.get(index);
	}

	@Override
	public ValueType set(int index, ValueType element) {
		ValueType oldElement = backing.set(index, element);
	
		if (minElement == null) {
			minElement = element;
		} else if (picker.compare(element, minElement) < 0) {
			minElement = element;
			recalcMin = false;
		} else if (oldElement.equals(minElement)) {
			minElement = null;
			recalcMin = true;
		}
		
		if (maxElement == null) {
			maxElement = element;
		} else if (picker.compare(element, maxElement) > 0) {
			maxElement = element;
			recalcMax = false;
		} else if (oldElement.equals(maxElement)) {
			maxElement = null;
			recalcMax = true;
		}
		
		return oldElement;
	}
	
	@Override
	public void add(int index, ValueType element) {
		backing.add(index, element);
		
		if (minElement == null) {
			minElement = element;
		} else if (picker.compare(element, minElement) < 0) {
			minElement = element;
			recalcMin = false;
		}
		
		if (maxElement == null) {
			maxElement = element;
		} else if (picker.compare(element, maxElement) > 0) {
			maxElement = element;
			recalcMax = false;
		} 
	}
	
	@Override
	public ValueType remove(int index) {
		ValueType oldElement = backing.remove(index);
		
		if (oldElement.equals(minElement)) {
			minElement = null;
			recalcMin = true;
		}
		
		if (oldElement.equals(maxElement)) {
			maxElement = null;
			recalcMax = true;
		}
		
		return oldElement;
	}
	
	@Override
	public int size() {
		return backing.size();
	}

	/**
	 * Get the minimum element currently stored in this list.
	 * 
	 * @return The minimum element stored in the list.
	 */
	public ValueType minimum() {
		if (recalcMin) calculateMinimum();
		
		return minElement;
	}

	/**
	 * Get the maximum element currently stored in this list.
	 * 
	 * @return The maximum element stored in the list.
	 */
	public ValueType maximum() {
		if (recalcMax) calculateMaximum();
		
		return maxElement;
	}
	
	private void calculateMinimum() {
		for (ValueType element : backing) {
			if (minElement == null) {
				minElement = element;
			} else if (picker.compare(element, minElement) < 0) {
				minElement = element;
			}			
		}
		
		recalcMin  = false;
	}
	
	private void calculateMaximum() {
		for (ValueType element : backing) {
			if (maxElement == null) {
				maxElement = element;
			} else if (picker.compare(element, maxElement) > 0) {
				maxElement = element;
			}
		}

		recalcMax  = false;
	}
	
	private void calculateBoth() {
		for (ValueType element : backing) {
			if (minElement == null) {
				minElement = element;
			} else if (picker.compare(element, minElement) < 0) {
				minElement = element;
			}		
			
			if (maxElement == null) {
				maxElement = element;
			} else if (picker.compare(element, maxElement) > 0) {
				maxElement = element;
			}
		}

		recalcMin  = false;
		recalcMax  = false;
	}

	@Override
	public String toString() {
		return String.format("%s (min: %s) (max: %s)", backing,
				recalcMin ? "(unknown)" : minElement,
				recalcMax ? "(unknown)" : maxElement);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(backing, picker);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)                  return true;
		if (!super.equals(obj))           return false;
		if (getClass() != obj.getClass()) return false;
		
		MinMaxList<?> other = (MinMaxList<?>) obj;
	
		return Objects.equals(backing, other.backing)
				&& Objects.equals(picker, other.picker);
	}
}
