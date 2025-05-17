class Fruit {
   name() : String { "Unknown fruit" };
};

class Apple inherits Fruit {
   name() : String { "Apple" };
};

class Banana inherits Fruit {
   name() : String { "Banana" };
};

class Main {
   main() : Object {
      let fruit : Fruit <- new Apple in
         case fruit of
            a : Apple => out_string(a.name());
            b : Banana => out_string(b.name());
            f : Fruit => out_string(f.name());
         esac
   };
};
