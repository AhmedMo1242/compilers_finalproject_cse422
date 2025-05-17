-- This file has syntax errors to test error handling

class Main {
   -- Missing semicolon
   x : Int
   
   -- Missing return type
   method1() { 5 };
   
   -- Missing closing brace
   method2() : Int {
      5
   ;
   
   -- Incorrectly formed if statement
   method3() : Object {
      if x < 5 then "Less than 5"
   };
};
