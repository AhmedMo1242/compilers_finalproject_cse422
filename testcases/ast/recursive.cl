-- Recursive methods and mutual recursion
class RecursiveTest inherits IO {
   -- Factorial using recursion
   factorial(n : Int) : Int {
      if n = 0 then 
         1 
      else 
         n * factorial(n - 1) 
      fi
   };
   
   -- Fibonacci sequence using recursion
   fibonacci(n : Int) : Int {
      if n <= 1 then
         n
      else
         fibonacci(n-1) + fibonacci(n-2)
      fi
   };
   
   -- Even/odd mutual recursion example
   isEven(n : Int) : Bool {
      if n = 0 then
         true
      else
         isOdd(n-1)
      fi
   };
   
   isOdd(n : Int) : Bool {
      if n = 0 then
         false
      else
         isEven(n-1)
      fi
   };
   
   main() : Object {
      {
         out_string("Factorial of 5: ");
         out_int(factorial(5));
         out_string("\nFibonacci(7): ");
         out_int(fibonacci(7));
         out_string("\nIs 10 even? ");
         if isEven(10) then out_string("Yes") else out_string("No") fi;
         out_string("\nIs 7 odd? ");
         if isOdd(7) then out_string("Yes") else out_string("No") fi;
         out_string("\n");
      }
   };
};
