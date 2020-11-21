package bjc.functypes;

import java.util.function.*;

public class FixpointExample {
 public static void main(String[] args) {
	 BiFunction<Integer, Function<Integer, Integer>, Integer> func 
	 = (input, self) -> {
		 if (input <= 1) return 1;
		 else            return input * self.apply(input - 1);
	 };
	 
	 Function<Integer, Integer> factorial = Fixpoints.fix(func);
	 
	 for (int i = 0; i < 10; i++) System.out.println(factorial.apply(i));
 }
}
