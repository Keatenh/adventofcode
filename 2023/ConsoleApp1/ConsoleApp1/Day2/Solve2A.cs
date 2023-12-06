using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using static System.Net.Mime.MediaTypeNames;

namespace ConsoleApp1.Day2
{
    public class Solve2A
    {
        public Solve2A() {

            int total = 0;
            int lineIndex = 1;
            int redMax = 12;
            int greenMax = 13;
            int blueMax = 14;

            // Check each line for multiple games
            foreach (string line in File.ReadLines(@"..\..\..\..\..\input\Day2\Input.txt", Encoding.UTF8))
            {
                // count how many times we go over the budget (Maybe overkill, maybe useful in part2...)
                int offenses = 0;
                string[] games = line.Split(':')[1].Split(';');
                foreach (string game in games)
                {
                    
                    string[] colorSums = game.Split(",");
                    foreach(string colorSum in  colorSums)
                    {
                        string color = Regex.Replace(colorSum, @"[^a-z]+", String.Empty);
                        int sum = int.Parse(string.Concat(colorSum.Where(char.IsDigit)));
                        switch (color)
                        {
                            case "red":
                                if (sum > redMax)
                                {
                                    offenses++;
                                }
                                break;
                            case "green":
                                if (sum > greenMax)
                                {
                                    offenses++;
                                }
                                break;
                            case "blue":
                                if (sum > blueMax)
                                {
                                    offenses++;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    
                }
                // If we didn't go over our colored cube budget,
                // add the ID of the game to the result total
                if (offenses == 0)
                {
                    total += lineIndex;
                }

                lineIndex++;
            }
            Console.WriteLine("The sum of successful IDs is: " + total);
        }
    }
}
