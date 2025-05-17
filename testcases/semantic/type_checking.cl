-- Test basic type checking

class Main inherits IO {
   main(): Object {
      {
         -- Valid assignments
         let a: Int <- 5, b: Int <- a in
            out_int(a + b);
            
         -- Type error: string assigned to int
         let c: Int <- "hello" in
            out_int(c);
            
         -- Type error: bool in arithmetic
         let d: Int <- 5 + true in
            out_int(d);
            
         -- Valid conditional
         if 5 < 10 then
            out_string("5 is less than 10\n")
         else
            out_string("Math is broken\n")
         fi;
         
         -- Type error: condition should be Bool
         if 5 then
            out_string("This should not compile\n")
         else
            out_string("Neither should this\n")
         fi;
      }
   };
};
