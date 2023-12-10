using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Runtime.ExceptionServices;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace ConsoleApp1.Day4
{
    public class Solve4B
    {
        private int total = 0;
        // Create lists of numbers that represent cards
        // Loop through each card once, calling a recursive function,
        // which will return more references to call itself, or add to the total cards

        private void UpdateIntListWithSpaceDelimitedString(List<int> list, String data)
        {
            foreach (string number in data.Split(" ",StringSplitOptions.RemoveEmptyEntries))
            {
                list.Add(int.Parse(number));
            }
        }

       
        private List<int> FilterInclusiveIntLists(List<int> raw, List<int> filter) {
            return raw.Where(n => filter.Any(x => x == n)).ToList();
        }
        
        private void RecurseLists(List<List<int>>[] reference, int location, int depth)
        {
            //Just count everything that reaches the function - to include original cards at depth == 0
            this.total++;

            // get map of where to go next:
            // element 0 is the filter
            // element 1 is the values to filter

            //Console.WriteLine("depth: " + depth);
            //Console.WriteLine("location: " + location);

            int newDepth = depth + 1;
            int numberOfmatches = FilterInclusiveIntLists(reference[1][location], reference[0][location]).Count();

            if (numberOfmatches <= 0)
            { 
                return;
            }
            
            // Iterate over the next few card indices, based on the number of matches we got
            for (int i = location + 1; i < location + 1 + numberOfmatches; i++)
            {
                //Console.WriteLine(i);
                RecurseLists(reference, i, newDepth);
            }

            return;
        }

        public Solve4B()
        {

            
            List<List<int>>[] map = [new List<List<int>>(), new List<List<int>>()];
            int lineNum = 0;
            foreach (string line in File.ReadLines(@"..\..\..\..\..\input\Day4\Input.txt", Encoding.UTF8))
            {
                string[] cards = line.Split(':')[1].Split('|');
                List<int> winners = [];
                List<int> given = [];

                UpdateIntListWithSpaceDelimitedString(winners, cards[0].Trim());
                UpdateIntListWithSpaceDelimitedString(given, cards[1].Trim());

                map[0].Add([]);
                map[1].Add([]);
                map[0][lineNum].AddRange(winners);
                map[1][lineNum].AddRange(given);

                //Unroot objects to free memory?
                //winners = null;
                //given = null;

                lineNum++;
            }

            for (int cardIndex = 0; cardIndex < map[0].Count(); cardIndex++)
            {
                RecurseLists(map, cardIndex, 0);
            }

            Console.WriteLine("Total scratchcards: " + total);
        }
    }
}
