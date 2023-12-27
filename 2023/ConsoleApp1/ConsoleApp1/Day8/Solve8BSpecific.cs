using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace ConsoleApp1.Day8
{

    //We already found the path distance from AAA->ZZZ == 14429
    //Based on the fact that the brute force method was taking a really long time,
    //could it be that the directions lead to periodic sequences that all eventually 
    //land on __Z addresses? 
    //If so, we could follow the patterns until they repeat in from each starting path (besides AAA above).
    //Then, compare the lengths of these loops to find when they would eventually align at the __Z values.
    // We can test with some initial value, like 'DNA' and peek at how it gets to '__Z' and if it loops...
    public class Solve8B
    {
        private string inputFile = @"..\..\..\Day8\Input.txt";

        public Solve8B()
        {
            //The hint I received is that LCM is involved...
            //Let's use a function that can calculate that for us when we get to that point
            static double gcf(double a, double b)
            {
                while (b != 0)
                {
                    double temp = b;
                    b = a % b;
                    a = temp;
                }
                return a;
            }

            static double lcm(double a, double b)
            {
                return (a / gcf(a, b)) * b;
            }

            static double lcmArr(double[] arr)
            {
                double multiple = 1;
                for (int i = 0; i < arr.Length; i++)
                {
                    multiple = lcm(arr[i], multiple);
                }
                return multiple;
            }

            //Testing function
            //LCM of 12, 15, 75 == 300
            //int[] a = { 12, 15, 75 };
            //int test = lcmArr(a);
            //Console.WriteLine(test);

            // Create the pattern of left/right instructions to loop over
            string lRInstructions = File.ReadLines(inputFile, Encoding.UTF8).First().Trim();
            int[] zeroOneInstructions = new int[lRInstructions.Length];
            zeroOneInstructions = lRInstructions.Select(c => c == 'L' ? 0 : 1).ToArray();

            //Parse remainder of file and generate map of addresses based on LR turns
            Dictionary<string, string[]> map = new Dictionary<string, string[]>();
            int lineNum = 0;
            
            
            foreach (string line in File.ReadLines(inputFile, Encoding.UTF8))
            {
                if (lineNum > 1)
                {
                    string[] data = line.Trim().Split('=');
                    string address = data[0].Trim();
                    string[] destinations = data[1].Trim().Split(',').Select(s => Regex.Replace(s, "[^a-zA-Z0-9_.]+", "", RegexOptions.Compiled)).ToArray();
                    map.Add(address, destinations);
                }
                lineNum++;
            }
            //Console.WriteLine(map);
            
            double steps = 0;
            List<string> targets = map.Keys.Where(x => x.EndsWith('A')).ToList();
            //number of steps taken for each looping path
            List<double> paths = new List<double>();

            //Changed the loops based on assumption that the loops are closed and independent
            //Spoiler alert - it works for this data :)
            for (int i = 0; i < targets.Count; i++)
            {
                steps = 0;
                int instructionIndex = 0;
                while (!targets[i].EndsWith('Z'))
                {
                    if (instructionIndex == zeroOneInstructions.Length)
                    {
                        instructionIndex = 0;
                    }
                    int instruction = zeroOneInstructions[instructionIndex];
                    string[] possibleTargets;

                    if (!map.TryGetValue(targets[i], out possibleTargets))
                    {
                        throw new Exception("Could NOT find instrucitons for key.");
                    }
                    targets[i] = possibleTargets[instruction];
                    steps++;
                    instructionIndex++;
                }
                
                //If we land on our target, remove this element from the targets
                //& add the steps to our list to take LCM
                paths.Add(steps);
                targets.RemoveAt(i);
                i--; //go back one to make up for lopping off an element
            }


            double result = lcmArr(paths.ToArray());

            //Console.WriteLine(steps + " last loop steps taken.");
            Console.WriteLine(result + " steps taken.");
        }
    }
}