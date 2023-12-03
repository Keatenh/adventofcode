using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    public class Solve1B
    {
        public Solve1B()
        {
            int total = 0;
            foreach (string line in File.ReadLines(@"..\..\..\..\..\input\Day1\1.txt", Encoding.UTF8))
            {
                //Console.WriteLine(line);

                //The problem statement does not mention the default for zero numbers in a line
                // so will not worry about this initialization falling through the logic...
                int firstNum = -1;
                int lastNum = -1;
                int lineNum = 0;

                //track if we have found a number in this line
                bool found = false;


                //TODO: Break the lines into arrays of numbers, after parsing the spelled out words that match digits 0-9,
                // before evaluating 1st and last numbers and performing summation.
                // e.g, 7pqrstsixteen -> 7 + 6

                //process the characters in the line, looking for numbers/digits

                //PLACEHOLDER FOR BUILD ERROR:
                Console.WriteLine(found);

                lineNum = 10 * firstNum + lastNum;
                //Console.WriteLine(lineNum);
                total += lineNum;
            }
            Console.WriteLine("The Sum of the Calibration Values is: " + total);


        }
    }
}
