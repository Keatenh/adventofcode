using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

// This was an incorrect solution, fixed in 1C.

namespace ConsoleApp1.Day1
{
    public class Solve1B
    {
        public Solve1B()
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

                // Progress note: 54607 is too high...
                // One example of a failure is 9nqjzjjhhps9twonex
                // where the last parsable number is one, but we parse 2 before its "o" can be used
                // So we can probably rework this to read the numbers without overwriting them in-place in new strings...

                Console.WriteLine("Line: " + line);
                //process the characters in the line, looking for numbers/digits
                int variableLength = line.Length + 1;
                string editedLine = line;
                for (int i = 0; i < variableLength; i++)
                {
                    if (i >= 3)
                    {
                        string last3 = editedLine.Substring(i - 3, 3);
                        string lastEditedLine = editedLine;
                        editedLine = new string(editedLine.AsSpan()[0..(i - 3)]) + last3.Replace("one", "1")
                            .Replace("two", "2")
                            .Replace("six", "6") + new string(editedLine.AsSpan()[i..]);
                        //Console.WriteLine("Constructed Line: " + editedLine);
                        // Reset our index based on how far we moved, and to re-process the number instead of the word in the TryParse() section
                        // e.g, two1nine --> 21nine (Here index 3 after processing "two" is "n" instead of "1")
                        if (editedLine.Length < lastEditedLine.Length)
                        {
                            i -= 3;
                            variableLength -= 2;
                        }

                    }
                    if (i >= 4)
                    {
                        string last4 = editedLine.Substring(i - 4, 4);
                        string lastEditedLine = editedLine;
                        editedLine = new string(editedLine.AsSpan()[0..(i - 4)]) + last4.Replace("zero", "0")
                            .Replace("four", "4")
                            .Replace("five", "5")
                            .Replace("nine", "9") + new string(editedLine.AsSpan()[i..]);
                        if (editedLine.Length < lastEditedLine.Length)
                        {
                            i -= 4;
                            variableLength -= 3;
                        }

                    }
                    if (i >= 5)
                    {
                        string last5 = editedLine.Substring(i - 5, 5);
                        string lastEditedLine = editedLine;
                        editedLine = new string(editedLine.AsSpan()[0..(i - 5)]) + last5.Replace("three", "3")
                            .Replace("seven", "7")
                            .Replace("eight", "8") + new string(editedLine.AsSpan()[i..]);
                        if (editedLine.Length < lastEditedLine.Length)
                        {
                            i -= 5;
                            variableLength -= 4;
                        }

                    }
                    //Console.WriteLine("i: "+i+" editedLine: " + editedLine);
                    //For some reason we get OOB exception for all numbers replaced in example
                    if (i < editedLine.Length && int.TryParse(Convert.ToString(editedLine[i]), out int n))
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
                    else //The current character is NOT a number
                    {
                        //input.Substring(_,_)
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
