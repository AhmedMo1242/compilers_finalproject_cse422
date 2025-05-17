-- This file tests all COOL keywords

class Main inherits IO {
   main() : Object {
      let x : Int <- 5 in
      while x > 0 loop
         if x = 3 then
            out_string("Three")
         else
            out_string("Not Three")
         fi;
         x <- x - 1
      pool
   };
};
