-- Complex example using both case and let expressions
class CaseLetTest inherits IO {
   -- Test case expression
   typeCase(obj : Object) : String {
      case obj of
         i : Int => "Integer: ".concat(i.toString());
         s : String => "String: ".concat(s);
         b : Bool => if b then "Boolean: true" else "Boolean: false" fi;
         o : Object => "Some other object";
      esac
   };
   
   -- Test complex let expressions
   nestedLet() : Object {
      let x : Int <- 10 in
         let y : Int <- x * 2,
             z : Int <- y + 5
         in {
            out_string("x = ");
            out_int(x);
            out_string("\ny = ");
            out_int(y);
            out_string("\nz = ");
            out_int(z);
            out_string("\n");
         }
   };
   
   -- Test let with variable shadowing
   shadowingLet() : Int {
      let x : Int <- 10 in {
         let x : Int <- 20 in {
            x;  -- This refers to the inner x (20)
         };
         x;  -- This refers to the outer x (10)
      }
   };
   
   main() : Object {
      {
         nestedLet();
         out_string("Shadowing result: ");
         out_int(shadowingLet());
         out_string("\n");
         
         out_string(typeCase(100));
         out_string("\n");
         out_string(typeCase("test"));
         out_string("\n");
         out_string(typeCase(true));
         out_string("\n");
      }
   };
};
