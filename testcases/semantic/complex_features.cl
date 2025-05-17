-- Test more complex semantic features

class Shape {
   area(): Int { 0 };
};

class Rectangle inherits Shape {
   width: Int;
   height: Int;
   
   init(w: Int, h: Int): SELF_TYPE {
      {
         width <- w;
         height <- h;
         self;
      }
   };
   
   area(): Int { width * height };
};

class Circle inherits Shape {
   radius: Int;
   
   init(r: Int): SELF_TYPE {
      {
         radius <- r;
         self;
      }
   };
   
   area(): Int { 3 * radius * radius };  -- Simple approximation of π*r²
};

class Main inherits IO {
   -- Test dynamic dispatch and SELF_TYPE
   printShape(s: Shape): Object {
      {
         out_string("Area: ");
         out_int(s.area());
         out_string("\n");
      }
   };
   
   -- Test case expressions
   getShapeName(s: Shape): String {
      case s of
         r: Rectangle => "Rectangle";
         c: Circle => "Circle";
         s: Shape => "Generic Shape";
      esac
   };
   
   -- Test let expressions with initialization
   makeRectangle(): Rectangle {
      let r: Rectangle <- new Rectangle,
          initialized: Rectangle <- r.init(5, 10)
      in
         initialized
   };
   
   main(): Object {
      {
         -- Test dispatch
         let r: Rectangle <- makeRectangle(),
             c: Circle <- (new Circle).init(7)
         in {
            printShape(r);
            printShape(c);
            
            -- Test case
            out_string(getShapeName(r));
            out_string("\n");
            out_string(getShapeName(c));
            out_string("\n");
         };
         
         -- Test while loop
         let i: Int <- 0 in
            while i < 5 loop {
               out_int(i);
               out_string("\n");
               i <- i + 1;
            } pool;
      }
   };
};
