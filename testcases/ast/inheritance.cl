-- Class hierarchy demonstrating inheritance
class Shape {
   area() : Int { 0 };  -- Base method
};

class Rectangle inherits Shape {
   width : Int;
   height : Int;
   
   -- Initializer with multiple assignments
   init(w : Int, h : Int) : Rectangle {
      {
         width <- w;
         height <- h;
         self;
      }
   };
   
   -- Override base method
   area() : Int { width * height };
};

class Square inherits Rectangle {
   -- Static dispatch to parent method
   init(side : Int) : Square {
      self@Rectangle.init(side, side)
   };
};

class Main inherits IO {
   main() : Object {
      let s : Square <- new Square,
          r : Rectangle <- new Rectangle
      in {
         r.init(3, 4);
         s.init(5);
         out_string("Rectangle area: ");
         out_int(r.area());
         out_string("\nSquare area: ");
         out_int(s.area());
         out_string("\n");
      }
   };
};
