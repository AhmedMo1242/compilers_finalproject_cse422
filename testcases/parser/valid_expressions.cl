-- Valid expressions

class Main {
   main() : Object {
      {
         -- Method calls
         self.method();
         obj@Type.method(arg1, arg2);
         
         -- If-then-else
         if x < 5 then
            "Less than 5"
         else
            "Greater than or equal to 5"
         fi;
         
         -- While loop
         while i < 10 loop
            i <- i + 1
         pool;
         
         -- Let expression
         let x : Int <- 5,
             y : String <- "Hello"
         in x + y.length();
         
         -- Case expression
         case x of
            i : Int => i + 1;
            s : String => s.concat("!");
            o : Object => new Object;
         esac;
         
         -- New object
         new MyClass;
      }
   };
};
