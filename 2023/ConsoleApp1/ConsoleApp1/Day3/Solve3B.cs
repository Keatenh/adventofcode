using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1.Day3
{
    public class Solve3B
    {
        public class NumData
        {
            public int value;
            public int indexLow;
            public int indexHigh;

            // constructor
            public NumData(int value, int indexLow, int indexHigh)
            {
                this.value = value;
                this.indexLow = indexLow;
                this.indexHigh = indexHigh;
            }
        }

        public Solve3B()
        {
            // Find all the numbers in the schematic that are adjacent, even diagonally, to any non-period symbol;
            // Take the sum of these numbers.

            List<List<NumData>> numberData = new List<List<NumData>>();
            List<List<int>> symbolLocations = new List<List<int>>();

            int row = 0;
            int total = 0;

            foreach (string line in File.ReadLines(@"..\..\..\..\..\input\Day3\Input.txt", Encoding.UTF8))
            {
                List<NumData> numbers = new List<NumData>();
                List<int> symbols = new List<int>();

                for (int i = 0; i < line.Length; i++)
                {
                    if (line[i] != '.')
                    {
                        // Parse numbers out of line, noting their initial and final index
                        if (int.TryParse(Convert.ToString(line[i]), out int _n1))
                        {
                            int startIndex = i;
                            string curNum = "";
                            // Must check we don't go outside the original line length when the number is at the end of the line
                            while (i < line.Length && int.TryParse(Convert.ToString(line[i]), out int _n2))
                            {
                                curNum += Convert.ToString(line[i]);
                                i++; //skip ahead so we don't reprocess the same digits of the current number
                            }
                            int endIndex = i - 1;
                            int num = int.Parse(curNum);
                            numbers.Add(new NumData(num, startIndex, endIndex));
                            i--; //take away one to start from correct spot next loop
                        }
                        else if (line[i] == '*')// Note the symbol's index
                        {
                            symbols.Add(i);
                        }
                    }
                }
                numberData.Add(numbers);
                symbolLocations.Add(symbols);
                row++;
            }

            // Make decisions to cut out numbers based on problem criteria...
            // For the current line, if there is any overlap at i+-1 we are touching a symbol
            // For the previous line, if there is any overlap at i+-1 we are touching a symbol
            for (int i = 0; i < row; i++)
            {
                //Console.WriteLine("row: "+i);
                foreach (int symbolCoord in symbolLocations[i])
                {
                    //Console.WriteLine(symbolCoord);
                    List<int> factors = new List<int>();

                    foreach (NumData number in numberData[i - 1])
                    {
                        
                        if (
                            symbolCoord >= number.indexLow - 1 &&
                            symbolCoord <= number.indexHigh + 1
                            )
                        {
                            factors.Add(number.value);
                        }
                    }
                    foreach (NumData number in numberData[i])
                    {
                        if (
                            symbolCoord >= number.indexLow - 1 &&
                            symbolCoord <= number.indexHigh + 1
                            )
                        {
                            factors.Add(number.value);
                        }
                    }
                    foreach (NumData number in numberData[i + 1])
                    {
                        if (
                            symbolCoord >= number.indexLow - 1 &&
                            symbolCoord <= number.indexHigh + 1
                            )
                        {
                            factors.Add(number.value);
                        }
                    }
                    if (factors.Count == 2)
                    {
                        total += factors[0] * factors[1];
                    }
                }
            }

            Console.WriteLine("Total of part numbers is: " + total);
        }
    }
}
