-- Test method checks

class Main inherits IO {
   -- Method with correct return type
   add(a: Int, b: Int): Int { a + b };
   
   -- Method with incompatible return type
   badAdd(a: Int, b: Int): String { a + b };  -- Error: Int doesn't conform to String
   
   -- Method with duplicate parameters
   dupParams(a: Int, a: Int): Int { a };  -- Error: Duplicate parameter name
   
   -- Valid self type return
   getSelf(): SELF_TYPE { self };
   
   -- Static vs. dynamic dispatch
   main(): Object {
      {
         -- Valid method calls
         let x: Int <- add(5, 10),
             y: Int <- 5,
             z: Int <- 10
         in {
            out_int(x);
            out_string("\n");
            out_int(add(y, z));
            out_string("\n");
         };
         
         -- Method call with wrong number of arguments
         let a: Int <- add(1, 2, 3) in  -- Error: Too many arguments
            out_int(a);
         
         -- Method call with wrong argument types
         let b: Int <- add("hello", 5) in  -- Error: String doesn't conform to Int
            out_int(b);
         
         -- Calling a non-existent method
         let c: Int <- multiply(5, 10) in  -- Error: Method not defined
            out_int(c);
      }
   };
};
