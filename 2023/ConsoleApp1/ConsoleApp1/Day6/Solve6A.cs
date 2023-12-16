using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace ConsoleApp1.Day6
{
    public class Solve6A
    {
        private string inputFile = @"..\..\..\Day6\Input.txt";

        private int[] CreateIntArrayFromString(string str)
        {
            return Array.ConvertAll(str.Trim().Split(" ", StringSplitOptions.RemoveEmptyEntries), int.Parse);
        }

        private void AddStringifiedArrayToList(List<int> destination, string source)
        {
            foreach(int number in CreateIntArrayFromString(source))
            {
                destination.Add(number);
            }
        }
       
        void LogInts(List<int> numbers)
        {
            foreach (int number in numbers)
            {
                Console.WriteLine(number);
            }
        }


        public Solve6A()
        {
            double result = 1;
            
            //Parse File
            List<int> times = new List<int>();
            List<int> records = new List<int>();
            int lineNum = 0;
            foreach (string line in File.ReadLines(inputFile, Encoding.UTF8))
            {
                if (lineNum == 0)
                {
                    AddStringifiedArrayToList(times, line.Split(':')[1]);
                }
                else if (lineNum == 1)
                {
                    AddStringifiedArrayToList(records, line.Split(':')[1]);
                }

                lineNum++;
            }
            //LogInts(times);
            //LogInts(records);

            // We might be able to take advantage of the symmetry of the problem...
            // distance, d = velocity * "time"
            // velocity, v = hold_time, h
            // "time" = released_time = total_time - hold_time => t - h
            // d = h * (t - h)
            // For max distance, d_max = t^2/4
            // t^2/4 = h(t-h)
            // Factoring: h^2 - th -t^2/4
            // => (h -t/2)^2
            // => h = t/2
            // Therefore the farthest distance is that where the hold time is half the total time.

            //So for the program, we should only need to
            // 1. Find the closest whole number h to the halfway point,
            // 2. Check its distance from the time that produces the record
            // 3. Multiply the delta in 2. by a factor of 2

            for (int i = 0; i< times.Count(); i++)
            {
                double t = times[i];
                double hMax = Math.Floor(t/2);
                //Quadratic formula to get the hold time of the record:
                // for h^2 - th + d_rec
                // h = t+-sqrt(t^2-4*d_rec)/2
                double hRec = Math.Floor((t + Math.Sqrt(t * t - 4 * records[i])) / 2);
                double delta = hRec - hMax;
                //If the lowest value in the range is EXACTLY the value of the record,
                //we need to exclude it from the range
                if ((hMax-delta)*(hMax+delta) == records[i]) {
                    delta--;
                }
                double ways = 2 * delta;
                //Evenly vs not evenly divisible adds/subtracts from the doubled distance
                if (t % 2.0 == 0)
                {
                    ways++;
                }

                //Console.WriteLine("Can beat the record "+ ways +" ways.");
                result*=ways;
            }

            Console.WriteLine("Product of combinations is: " + result);
        }
    }
}
