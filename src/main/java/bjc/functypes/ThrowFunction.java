package bjc.functypes;

import java.util.function.*;

/**
 * An instance of {@link Function} that can throw an exception.
 * 
 * @author Ben Culkin
 *
 * @param <InputType> The input to the function.
 * @param <ReturnType> The output to the function.
 * @param <ExType> The type of exception thrown.
 */
public interface ThrowFunction<InputType, ReturnType, ExType extends Throwable> {
	/**
	 * Does the possibly throwing computation embodied by this function.
	 * 
	 * @param arg The input to the function.
	 * 
	 * @return The result of the function.
	 * 
	 * @throws ExType If something went wrong with the function.
	 */
	public ReturnType apply(InputType arg) throws ExType;
	
	/**
	 * Converts this into a {@link Function} by handling any thrown exceptions.
	 * 
	 * @param clasz The class of the handled exception. This needs to be provided
	 *              because you can't catch a generic exception, and we want to
	 *              make sure that we aren't catching any types of exception that
	 *              we aren't supposed to.
	 *              
	 * @param handler The handler to use.
	 * 
	 * @return A function which will either return its proper value, or the result
	 *         of invoking the handler.
	 */
	@SuppressWarnings("unchecked")
	default Function<InputType, ReturnType> swallow(
			Class<ExType> clasz, Function<ExType, ReturnType> handler) {
		return (inp) -> {
			try {
				return this.apply(inp);
			} catch (Throwable ex) {
				if (clasz.isInstance(ex)) {
					// Swallow this
					return handler.apply((ExType) ex);
				} else {
					String msg = "Exception of incorrect type to be handled, only "
											 + clasz.getName()
											 + " are handled";
					
					throw new RuntimeException(msg, ex);
				}
			}
		};
	}
	
	/**
	 * Convert this to a function which will attempt to recover from the thrown exception.
	 * 
	 * @param clasz The class of the exception. Needed for type-safety reasons.
	 * @param rescue The function to use to convert an exception into a safe input value.
	 * 
	 * @return A function which attempts to recover from a exception.
	 */
	@SuppressWarnings("unchecked")
	default Function<InputType, ReturnType> recover(
			Class<ExType> clasz, BiFunction<InputType, ExType, InputType> rescue) {
		return Fixpoints.fix((arg, self) -> {
			try {
				return this.apply(arg);
			} catch (Throwable ex) {
				if (clasz.isInstance(ex)) {
					// Swallow this
					return self.apply(rescue.apply(arg, (ExType) ex));
				} else {
					String msg = "Exception of incorrect type to be handled, only "
											 + clasz.getName()
											 + " are handled";
					
					throw new RuntimeException(msg, ex);
				}
			}
		});
	}
}
