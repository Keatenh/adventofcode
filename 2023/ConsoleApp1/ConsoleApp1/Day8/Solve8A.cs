using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace ConsoleApp1.Day8
{
    
    public class Solve8A
    {
        private string inputFile = @"..\..\..\Day8\Input.txt";

        public Solve8A()
        {
            //Goal: report number of steps taken to get to destination 'ZZZ'
            int steps = 0;

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

            //Loop through turns based on instructions until we find the last key
            string target = "AAA";
            while (target != "ZZZ")
            {
                int instructionIndex = steps % zeroOneInstructions.Length;
                int instruction = zeroOneInstructions[instructionIndex];
                string[] possibleTargets;
                if (!map.TryGetValue(target, out possibleTargets))
                {
                    throw new Exception("Could NOT find instrucitons for key.");
                }
                target = possibleTargets[instruction];

                steps++;
            }

            Console.WriteLine(steps + " steps taken.");
        }
    }
}