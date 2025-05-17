-- Class demonstrating many expression types
class ExpressionTest {
   -- Method with various expression types
   testExpressions() : Object {
      {
         -- Arithmetic expressions with precedence
         let x : Int <- 5 + 3 * 2 in
            x + (10 - 2) / 4;
         
         -- Logical expressions
         let a : Bool <- true,
             b : Bool <- false,
             c : Bool <- not (a = b)
         in
            if c then "Different" else "Same" fi;
         
         -- While loop
         let counter : Int <- 0 in {
            while counter < 5 loop {
               counter <- counter + 1;
               out_int(counter);
            } pool;
         };
         
         -- New object creation
         new String;
         
         -- Method calls with parameters
         out_string("Result: ".concat("test"));
      }
   };
};
