using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace ConsoleApp1.Day6
{
    public class Solve6B
    {
        private string inputFile = @"..\..\..\Day6\Input.txt";


        private static readonly Regex sWhitespace = new Regex(@"\s+");
        public static string ReplaceWhitespace(string input, string replacement)
        {
            return sWhitespace.Replace(input, replacement);
        }


        public Solve6B()
        {
            //Parse File
            double time = -1;
            double record = -1;
            int lineNum = 0;
            foreach (string line in File.ReadLines(inputFile, Encoding.UTF8))
            {
                if (lineNum == 0)
                {
                    time = double.Parse(ReplaceWhitespace(line.Split(':')[1].Trim(), ""));
                }
                else if (lineNum == 1)
                {
                    record = double.Parse(ReplaceWhitespace(line.Split(':')[1].Trim(), ""));
                }

                lineNum++;
            }

            // See Solve6A for reasoning for math expressions

            double t = time;
            double hMax = Math.Floor(t/2);
            //Quadratic formula to get the hold time of the record:
            // for h^2 - th + d_rec
            // h = t+-sqrt(t^2-4*d_rec)/2
            double hRec = Math.Floor((t + Math.Sqrt(t * t - 4 * record)) / 2);
            double delta = hRec - hMax;
            //If the lowest value in the range is EXACTLY the value of the record,
            //we need to exclude it from the range
            if ((hMax-delta)*(hMax+delta) == record) {
                delta--;
            }
            double ways = 2 * delta;
            //Evenly vs not evenly divisible adds/subtracts from the doubled distance
            if (t % 2.0 == 0)
            {
                ways++;
            }

            //Since the combined strings make hilariously large numbers, lets convert from the original mm, ms to some more useful units for display, for fun:)
            Console.WriteLine("Given: "+ Math.Round(time /1000/3600, 2)+" hrs, You can beat the record of: "+ Math.Round(record /1000/1000/300000,2)+" light seconds in "+ ways + " ways.");

        }
    }
}
