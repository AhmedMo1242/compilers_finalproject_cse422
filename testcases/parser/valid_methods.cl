-- Valid method definitions

class Main {
   method1() : Int { 5 };
   
   method2(x : Int) : Int { x + 1 };
   
   method3(x : Int, y : String, z : Bool) : Object {
      {
         x <- x + 1;
         y.concat("test");
      }
   };
};
