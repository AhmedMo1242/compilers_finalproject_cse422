class Main inherits IO {
   x : Int <- 5;
   y : Int <- 10;
   
   add(a : Int, b : Int) : Int {
      a + b
   };
   
   main() : Object {
      out_int(add(x, y))
   };
};