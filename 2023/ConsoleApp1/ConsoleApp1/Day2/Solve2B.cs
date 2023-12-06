using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace ConsoleApp1.Day2
{
    public class Solve2B
    {
        public Solve2B()
        {
            int total = 0;
            int lineIndex = 1;


            // Check each line for multiple games
            foreach (string line in File.ReadLines(@"..\..\..\..\..\input\Day2\Input.txt", Encoding.UTF8))
            {
                // Find the Least number of colored cubes that would make the given game possible
                // i.e, find the greatest number of cubes for a given color across all pulls in a game

                int redMax = 0;
                int greenMax = 0;
                int blueMax = 0;

                string[] games = line.Split(':')[1].Split(';');
                foreach (string game in games)
                {
                    string[] colorSums = game.Split(",");
                    foreach (string colorSum in colorSums)
                    {
                        string color = Regex.Replace(colorSum, @"[^a-z]+", String.Empty);
                        int sum = int.Parse(string.Concat(colorSum.Where(char.IsDigit)));
                        switch (color)
                        {
                            case "red":
                                if (sum > redMax)
                                {
                                    redMax = sum;
                                }
                                break;
                            case "green":
                                if (sum > greenMax)
                                {
                                    greenMax = sum;
                                }
                                break;
                            case "blue":
                                if (sum > blueMax)
                                {
                                    blueMax = sum;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
                // Add the product of the maximum values of each game per color
                total += redMax * greenMax * blueMax;
                lineIndex++;
            }
            Console.WriteLine("The sum of the power of sets is: " + total);
        }
    }
}