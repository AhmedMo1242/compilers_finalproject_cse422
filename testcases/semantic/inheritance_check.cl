-- Test inheritance checks

-- Base class
class Animal {
   speak(): String { "Animal sound" };
   age: Int;
   
   getAge(): Int { age };
};

-- Valid inheritance
class Dog inherits Animal {
   -- Override method with same signature
   speak(): String { "Woof" };
   
   -- Add new method
   fetch(): String { "Fetched the ball" };
};

-- Invalid inheritance - cannot redefine attribute
class BadCat inherits Animal {
   -- Error: Redefining inherited attribute
   age: String;  -- Type changed from Int to String
};

-- Invalid inheritance - method override wrong return type
class BadBird inherits Animal {
   -- Error: Return type Bool doesn't match parent's String
   speak(): Bool { true };
};

-- Invalid inheritance - method override wrong parameter
class BadFish inherits Animal {
   -- Error: Adding parameter to overridden method
   getAge(scale: Int): Int { age * scale };
};

-- Cannot inherit from Int
class BadNumber inherits Int {  -- Error: Cannot inherit from Int
   value(): Int { 42 };
};

class Main inherits IO {
   main(): Object {
      let dog: Dog <- new Dog in {
         out_string(dog.speak());
         out_string("\n");
      }
   };
};
