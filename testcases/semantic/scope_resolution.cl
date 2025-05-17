-- Test scope resolution

class Main inherits IO {
   x: Int <- 10;
   
   main(): Object {
      {
         -- x refers to class attribute
         out_int(x);
         out_string("\n");
         
         -- Local x shadows class attribute
         let x: String <- "hello" in {
            out_string(x);
            out_string("\n");
         };
         
         -- Back to class attribute
         out_int(x);
         out_string("\n");
         
         -- Nested scopes
         let a: Int <- 5 in {
            let b: Int <- 10 in {
               let c: Int <- a + b in
                  out_int(c);  -- Should be 15
               out_string("\n");
            };
            
            -- c is out of scope here
            -- let d: Int <- c in  -- This would be an error
            --    out_int(d);
         };
         
         -- Variable used before definition
         -- let e: Int <- f, f: Int <- 5 in  -- Error in COOL
         --    out_int(e + f);
      }
   };
};
