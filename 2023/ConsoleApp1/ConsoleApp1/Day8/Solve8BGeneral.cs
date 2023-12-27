using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace ConsoleApp1.Day8
{
    //This solution is more generalized to do a brute force traversal of all nodes.
    //However, for the input given, this process takes forever to run.
    //Therefore, based on clues online, we can try again with a different technique that takes advantage of knowing our input
    public class Solve8BGeneral
    {
        private string inputFile = @"..\..\..\Day8\Input.txt";

        public Solve8BGeneral()
        {
            //Goal: report number of steps taken to get all paths '__A' destinations '__Z'
            //Note: found steps over billions so int32 would overflow
            double steps = 0;

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

            //Loop through turns based on instructions until all the keys we are searching end with 'Z'
            int instructionIndex = 0;
            List<string> targets = map.Keys.Where(x => x.EndsWith('A')).ToList();
            while (targets.Count == 0 || !targets.All(target => target.EndsWith('Z')))
            {
                //using a different way to loop over limited array for int instead of double type
                if (instructionIndex == zeroOneInstructions.Length) 
                {
                    instructionIndex = 0; 
                }
                //We should have some amound of instructions < 1000, so int is fine for indexing...
                int instruction = zeroOneInstructions[instructionIndex];
                string[] possibleTargets;

                for (int i=0; i<targets.Count; i++)
                {
                    if (!map.TryGetValue(targets[i], out possibleTargets))
                    {
                        throw new Exception("Could NOT find instrucitons for key.");
                    }
                    targets[i] = possibleTargets[instruction];
                }

                steps++;
                instructionIndex++;
            }

            Console.WriteLine(steps + " steps taken.");
        }
    }
}