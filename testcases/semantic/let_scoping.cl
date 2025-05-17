class Main {
   x : Int <- 5;
   
   main() : Object {
      let x : Int <- 10,
          y : Int <- x + 5 in
      {
         out_int(y);  -- Should print 15
         out_string("\n");
         let x : String <- "Hello" in
            out_string(x);  -- Should print "Hello"
      }
   };
};
