-- Comprehensive semantic test case
-- Tests type hierarchy, method dispatch, SELF_TYPE, and more

-- Base class for all shapes
class Shape {
   -- Abstract method to be overridden
   area(): Int { 0 };
   
   -- Concrete method that uses dynamic dispatch
   describe(): String { "A shape with area " };
   
   -- Method that returns self for chaining
   printDetails(): SELF_TYPE {
      {
         (new IO).out_string(describe().concat(area().type_name()).concat("\n"));
         self;
      }
   };
};

-- Concrete shape implementation
class Rectangle inherits Shape {
   width: Int <- 0;
   height: Int <- 0;
   
   -- Constructor-like method
   init(w: Int, h: Int): SELF_TYPE {
      {
         width <- w;
         height <- h;
         self;
      }
   };
   
   -- Override area
   area(): Int { width * height };
   
   -- Override describe
   describe(): String { "A rectangle with width = ".concat(width.type_name()) };
};

-- Another concrete shape
class Circle inherits Shape {
   radius: Int <- 0;
   
   init(r: Int): SELF_TYPE {
      {
         radius <- r;
         self;
      }
   };
   
   -- Override area
   area(): Int { 3 * radius * radius }; -- Simplified π*r²
   
   -- Override describe
   describe(): String { "A circle with radius = ".concat(radius.type_name()) };
};

-- Class that uses type checking and dispatch
class ShapeFactory {
   -- Create and return a specific shape
   createRectangle(w: Int, h: Int): Rectangle {
      (new Rectangle).init(w, h)
   };
   
   createCircle(r: Int): Circle {
      (new Circle).init(r)
   };
   
   -- Method using case expressions for type identification
   identifyShape(s: Shape): String {
      case s of
         r: Rectangle => "Created a Rectangle";
         c: Circle => "Created a Circle";
         s: Shape => "Created a generic Shape";
      esac
   };
   
   -- Method using complex let bindings
   processShape(s: Shape): Int {
      let 
         area: Int <- s.area(),
         description: String <- s.describe(),
         io: IO <- new IO
      in {
         io.out_string(description);
         io.out_string("\n");
         io.out_string("Area = ");
         io.out_int(area);
         io.out_string("\n");
         area;
      }
   };
};

-- Main class to test everything
class Main inherits IO {
   factory: ShapeFactory <- new ShapeFactory;
   
   main(): Object {
      {
         -- Create and test shapes
         let r: Rectangle <- factory.createRectangle(5, 10),
             c: Circle <- factory.createCircle(7)
         in {
            -- Print shape info
            out_string("Testing shapes:\n");
            
            -- Use dynamic dispatch
            out_string(factory.identifyShape(r));
            out_string("\n");
            out_string(factory.identifyShape(c));
            out_string("\n");
            
            -- Process shapes
            factory.processShape(r);
            factory.processShape(c);
            
            -- Test method chaining with SELF_TYPE
            r.printDetails().printDetails();
            c.printDetails().printDetails();
            
            -- Test conditional
            if r.area() > c.area() then
               out_string("Rectangle has larger area\n")
            else
               out_string("Circle has larger area\n")
            fi;
            
            -- Test complex expressions
            let total: Int <- r.area() + c.area() in {
               out_string("Total area: ");
               out_int(total);
               out_string("\n");
               
               -- Test while loop
               let i: Int <- 0 in
                  while i < total / 50 loop {
                     out_string("*");
                     i <- i + 1;
                  } pool;
               out_string("\n");
            };
         };
      }
   };
};
