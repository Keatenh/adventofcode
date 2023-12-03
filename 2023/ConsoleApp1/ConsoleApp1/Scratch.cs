using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    internal class Scratch
    {
        //var testVar = "noob";

        //Console.WriteLine(testVar);
        //Console.Beep();

        // Variables
        // camelCase for variables
        //int a = 99;
        //string b = "abc";
        //byte c = 255;
        //char d = 'a'; //single quotes matter
        //double testDouble = 14.43; //default, huge range with good precision
        //float testFloat = 0.11156154f; //less precise
        //decimal testDecimal = .1212121m; //smaller range, great precision (28 digits)

        //int testVariable = 7 / 2; //3 (double 7.0 would give 3.5)
        //Console.WriteLine(testVariable);

        // operators
        // +=
        // -=
        // *=
        // ++
        // --

        // Type Casting

        //double x = 1234.7;
        //int a;
        //// Cast double to int (Explicit Converstion)
        //a = (int)x;
        //Console.WriteLine(a);

        //// Cast string to int
        //string value = "152367123";
        //int result = 0;
        //if(int.TryParse(value, out result))
        //{
        //    Console.WriteLine("Success: "+ result);
        //}
        //else
        //{
        //    Console.WriteLine("Failure.");
        //}

        // Stack vs. Heap
        // Stack is for value types (int, byte, bool)
        // Heap is for reference types (string)

        //using ConsoleApp1;

        //StackHeapExample stackHeap = new(); //new StackHeapExample();
        //stackHeap.AddFive(4);

        // Array - collection of fixed sets of values
        //array is a reference type

        //int[] intArray = new int[2]; //Fixed array 
        //int[] staticIntArray = new int[] { 5,2,1,4,3};
        //string[] stringArray = new string[2];
        //intArray[0] = 1;

        ////Linq extension methods
        //staticIntArray.Average();

        //Array.Sort(staticIntArray);

        //Console.WriteLine(string.Join(", ", staticIntArray));
    }
}
