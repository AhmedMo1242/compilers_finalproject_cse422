class A {
   a : Int <- 1;
   f() : Int { a };
};

class B inherits A {
   b : Int <- 2;
   g() : Int { b + a };
};

class Main {
   main() : Object {
      let obj : B <- new B in
         out_int(obj.g())
   };
};
