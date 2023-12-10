using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1.Day4
{
    public class Solve4A
    {
        
        private void UpdateIntListWithSpaceDelimitedString(List<int> list, String data)
        {
            foreach (string number in data.Split(" ",StringSplitOptions.RemoveEmptyEntries))
            {
                list.Add(int.Parse(number));
            }
        }
        
        public Solve4A()
        {
            int total = 0;
            foreach (string line in File.ReadLines(@"..\..\..\..\..\input\Day4\Input.txt", Encoding.UTF8))
            {
                string[] cards = line.Split(':')[1].Split('|');
                List<int> winners = [];
                List<int> given = [];

                UpdateIntListWithSpaceDelimitedString(winners, cards[0].Trim());
                UpdateIntListWithSpaceDelimitedString(given, cards[1].Trim());

                // Reworked the foreach loops in part A to use Linq methods to filter between lists:

                // One less that the count works for our matches,
                // besides when matches == 0. So we get rid of anything
                // with a negative exponent and therefore <1. i.e,
                // matches.Count() == 0 -> n == -1
                // 2^n == 0.5 -> Floor == 0
                // int score = (int)Math.Floor(Math.Pow(2.0, (double)matches.Count() - 1.0));
                // total += score;

                int cardPts = 0;
                foreach(int winner in winners)
                {
                    foreach(int scratch in given)
                    {
                        if (scratch == winner)
                        {
                            cardPts = Math.Max(1,cardPts*2);
                        }
                    }
                }
                total += cardPts;
            }
            Console.WriteLine("Total points: " + total);
        }
    }
}
