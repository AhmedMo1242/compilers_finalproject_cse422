-- This file tests all types of literals

class Main {
   main() : Object {
      {
         -- Integer literals
         let x : Int <- 12345 in
            x;
         
         -- String literals
         let s : String <- "Hello, world!\nThis is a test \"string\" with escapes." in
            s;
         
         -- Boolean literals
         let b1 : Bool <- true,
             b2 : Bool <- false
         in {
            b1;
            b2;
         };
      }
   };
};
