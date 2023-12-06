using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1.Day1
{
    public class Solve1A
    {
        public Solve1A()
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

                //process the characters in the line, looking for numbers/digits
                for (int i = 0; i < line.Length; i++)
                {
                    if (int.TryParse(Convert.ToString(line[i]), out int n))
                    {
                        if (found)
                        {
                            lastNum = n;
                        }
                        else //1st time we found a number
                        {
                            firstNum = n;
                            lastNum = n;
                        }
                        //note we have found at least 1 number if we entered this branch
                        found = true;
                    }
                }
                lineNum = 10 * firstNum + lastNum;
                //Console.WriteLine(lineNum);
                total += lineNum;
            }
            Console.WriteLine("The Sum of the Calibration Values is: " + total);
        }
    }

}

