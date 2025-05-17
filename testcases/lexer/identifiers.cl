-- This file tests identifiers

class Main inherits IO {
   x : Int;
   y : String;
   CONSTANT : Int <- 100;
   
   someMethod(param1 : Int, param2 : String) : Object {
      let localVar : Int <- param1,
          anotherVar : String <- param2
      in {
         self.x <- localVar;
         self@Main.printSomething();
      }
   };
   
   printSomething() : Object {
      out_string("This is a test")
   };
};
