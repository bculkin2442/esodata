package bjc.esodata;

/**
 * An interface which allows you to view a given type as a tape.
 * 
 * @author Ben Culkin
 *
 * @param <ElementType> The type of element contained in the tape.
 */
public interface TapeView<ElementType> extends Tape<ElementType>
{
	/**
	 * Return a view of this object as a tape.
	 * 
	 * @return A view of this object as a tape.
	 */
	public Tape<ElementType> tapeView();
	
	@Override
	public default ElementType item()
	{
		return tapeView().item();
	}

	@Override
	public default void item(ElementType itm)
	{
		tapeView().item(itm);
	}

	@Override
	public default int size()
	{
		return tapeView().size();
	}

	@Override
	public default int position()
	{
		return tapeView().position();
	}

	@Override
	public default void insertBefore(ElementType itm)
	{
		tapeView().insertBefore(itm);
	}

	@Override
	public default void insertAfter(ElementType itm)
	{
		tapeView().insertAfter(itm);
	}

	@Override
	public default ElementType remove()
	{
		return tapeView().remove();
	}

	@Override
	public default void first()
	{
		tapeView().first();
	}

	@Override
	public default void last()
	{
		tapeView().last();
	}

	@Override
	public default boolean left()
	{
		return tapeView().left();
	}

	@Override
	public default boolean left(int amt)
	{
		return tapeView().left(amt);
	}

	@Override
	public default boolean right()
	{
		return tapeView().right();
	}

	@Override
	public default boolean right(int amt)
	{
		return tapeView().right(amt);
	}

	@Override
	public default boolean seekTo(int pos)
	{
		return tapeView().seekTo(pos);
	}
}
