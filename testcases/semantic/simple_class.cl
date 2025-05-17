class Main {
   x : Int <- 5;
   y : String <- "Hello, world!";
   
   main() : Object {
     let z : Int <- x + 10 in
       out_string(y).out_int(z)
   };
};
