-- Class with attributes and methods
class Person {
   name : String;  -- Attribute without initialization
   age : Int <- 0; -- Attribute with initialization
   
   -- Method with parameters and block expression
   init(n : String, a : Int) : Person {
      {
         name <- n;
         age <- a;
         self;
      }
   };
   
   -- Simple getter methods
   getName() : String { name };
   getAge() : Int { age };
};
