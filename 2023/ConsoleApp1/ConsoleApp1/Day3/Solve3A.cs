using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1.Day3
{
    public class Solve3A
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

        public Solve3A()
        {
            // Find all the numbers in the schematic that are adjacent, even diagonally, to any non-period symbol;
            // Take the sum of these numbers.

            List<NumData> currentNumbers = new List<NumData>();
            List<int> currentSymbolLocations = new List<int>();
            List<int> lastSymbolLocations = new List<int>();

            foreach (string line in File.ReadLines(@"..\..\..\..\..\input\Day3\Example.txt", Encoding.UTF8))
            {
                for (int i=0; i< line.Length; i++)
                {
                    Console.WriteLine("Initial i: " + i);
                    //char c = line[i];
                    if (line[i] != '.')
                    {
                        // Parse numbers out of line, noting their initial and final index
                        if (int.TryParse(Convert.ToString(line[i]), out int _n1))
                        {
                            int startIndex = i;
                            string curNum = "";
                            while (int.TryParse(Convert.ToString(line[i]), out int _n2)) {
                                curNum += Convert.ToString(line[i]);
                                i++; //skip ahead so we don't reprocess the same digits of the current number
                            }
                            int endIndex = i-1;
                            int num = int.Parse(curNum);
                            currentNumbers.Add(new NumData(num,startIndex,endIndex));
                        }
                        else // Note the symbol's index
                        {
                            currentSymbolLocations.Add(i);
                        }
                    }
                }
                // Make decisions to cut out numbers based on problem criteria...
                // For the current line, if there is any overlap at i+1 we are touching a symbol
                // For the previous line, if there is any overlap at i+1 we are touching a symbol

                foreach (NumData number in currentNumbers)
                {
                    Console.WriteLine(number.value.ToString());
                    Console.WriteLine(number.indexLow.ToString());
                    Console.WriteLine(number.indexHigh.ToString());
                    Console.WriteLine("\n");
                }
                Console.WriteLine("~~~~~~");
                
                // Clear/reset the lists
                currentNumbers.Clear();
                lastSymbolLocations = currentSymbolLocations;
                currentSymbolLocations.Clear();
            }
           
            Console.WriteLine("COMPLETE");
            }
    }
}
