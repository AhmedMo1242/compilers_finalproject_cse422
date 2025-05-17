class Parent {
   value : Int <- 10;
   getValue() : Int { value };
   printValue() : Object { out_int(getValue()) };
};

class Child inherits Parent {
   childValue : Int <- 20;
   getValue() : Int { value + childValue };
};

class Main {
   main() : Object {
      let parent : Parent <- new Parent,
          child : Child <- new Child in
      {
         out_int(parent.getValue());
         out_string("\n");
         out_int(child.getValue());
         out_string("\n");
         parent.printValue();
         out_string("\n");
         child.printValue();
      }
   };
};
