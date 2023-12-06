using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    
    public class Solve1C
    {
        public Solve1C()
        {
            int total = 0;
            foreach (string line in File.ReadLines(@"..\..\..\..\..\input\Day1\1.txt", Encoding.UTF8))
            {
                //The problem statement does not mention the default for zero numbers in a line
                // so will not worry about this initialization falling through the logic...
                int firstNum = -1;
                int lastNum = -1;
                int lineNum = 0;

                //track if we have found a number in this line
                bool found = false;


                //Parse the spelled out words that match digits 0-9,
                // before evaluating 1st and last numbers and performing summation.
                // e.g, 7pqrstsixteen -> 7pqrst6teen -> 7 + 6

                Console.WriteLine("Line: " + line);
                //process the characters in the line, looking for numbers/digits
                for (int i = 0; i < line.Length; i++)
                {

                    int newN = -1;
                    if (int.TryParse(Convert.ToString(line[i]), out int n))
                    {
                        newN = n;
                    }
                    else //The current character is NOT a number
                    {

                        if (i <= line.Length - 3 && line.Substring(i, 3) == "one")
                        {
                            newN = 1;
                        }
                        else if (i <= line.Length - 3 && line.Substring(i, 3) == "two")
                        {
                            newN = 2;
                        }
                        else if (i <= line.Length - 5 && line.Substring(i, 5) == "three")
                        {
                            newN = 3;
                        }
                        else if (i <= line.Length - 4 && line.Substring(i, 4) == "four")
                        {
                            newN = 4;
                        }
                        else if (i <= line.Length - 4 && line.Substring(i, 4) == "five")
                        {
                            newN = 5;
                        }
                        else if (i <= line.Length - 3 && line.Substring(i, 3) == "six")
                        {
                            newN = 6;
                        }
                        else if (i <= line.Length - 5 && line.Substring(i, 5) == "seven")
                        {
                            newN = 7;
                        }
                        else if (i <= line.Length - 5 && line.Substring(i, 5) == "eight")
                        {
                            newN = 8;
                        }
                        else if (i <= line.Length - 4 && line.Substring(i, 4) == "nine")
                        {
                            newN = 9;
                        }

                    }

                    if (newN >= 0)
                    {
                        if (found)
                        {
                            lastNum = newN;
                        }
                        else //1st time we found a number
                        {
                            firstNum = newN;
                            lastNum = newN;
                        }
                        //note we have found at least 1 number if we entered this branch
                        found = true;
                    }
                }
                lineNum = 10 * firstNum + lastNum;
                Console.WriteLine("Calibration : " + lineNum);
                total += lineNum;
            }
            Console.WriteLine("The Sum of the Calibration Values is: " + total);


        }
    }
}
