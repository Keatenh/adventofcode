using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection.Metadata.Ecma335;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1.Day5
{
    public class Solve5B
    {
        private long TranslateAlmanac(List<long[]>[] map, long n)
        {
            int i = 0; //tracking outer loop iterations
            foreach(List<long[]> set in map)
            {
                foreach (long[] instruction in set)
                {
                    if (instruction.Length != 3)
                    {
                        throw new ArgumentException("instruction in map should be int[3]");
                    }

                    long destStart = instruction[0];
                    long srcStart = instruction[1];
                    long r = instruction[2];
                    long delta = destStart - srcStart;

                    if (n >= srcStart && n < srcStart + r)
                    {
                        //Console.WriteLine("Found MATCH in set: " + i);
                        n += delta;
                        break; //go to next set of instructions with this value
                    }
                    
                    // Any source numbers that aren't mapped correspond to the same destination number.
                }
                //Console.WriteLine("Result of set: " + i +" is: " + n);
                i++;
            }
            
            //throw new ArgumentException("number outside of map???");
            return n;
        }

        enum State
        {
            Default,
            Mapping,
            Title
        }

        private long[] CreateIntArrayFromString(string str)
        {
            return Array.ConvertAll(str.Trim().Split(" ", StringSplitOptions.RemoveEmptyEntries), long.Parse);
        }

        private string inputFile = @"..\..\..\Day5\Input.txt";

        public Solve5B() {

            long lowestLocation = -1;
            
            int lineNum = 0;
            List<long> seeds = new List<long>();
            List<long[]>[] almanac = new List<long[]>[7];
            for (int i=0; i<almanac.Length; i++)
            {
                almanac[i] = new List<long[]>();
            }
            State state = State.Default;
            int setNum = -1;

            foreach (string line in File.ReadLines(inputFile, Encoding.UTF8))
            {
                if (line.Length == 0) //L1
                {
                    //Empty, set state for next line, that titles the map, but does not give numbers
                    state = State.Title;
                }
                else if (state == State.Title) //L3
                {
                    //Setup for actually adding the numbers next line
                    state = State.Mapping;
                    setNum++;
                }
                else if (state == State.Mapping)
                {
                    long[] instruction = CreateIntArrayFromString(line);
                    almanac[setNum].Add(instruction);
                }

                lineNum++;
            }

            //Making a giant List with the full input leads to OOM errors...
            //Is there a smart way to pick a subset of seeds to compare?

            //Decided to pre-process the mappings and process the now larger seed range data as we go.
            //Works to save memory, since we only care about carrying around our current candidate to the single number result.
            //This is a long running process of at least several minutes.

            //Read just the 1st line
            long[] seedsLine = CreateIntArrayFromString(File.ReadLines(inputFile).First().Split(':')[1]);
            long startLoc = -1;
            for (int i = 0; i < seedsLine.Length; i++)
            {
                if (i % 2 == 0) //Start location
                {
                    startLoc = seedsLine[i];
                }
                else //Range
                {
                        
                    long r = seedsLine[i];
                    for (long j = startLoc; j < startLoc + r; j++)
                    {
                        long loc = TranslateAlmanac(almanac, j);
                        if (lowestLocation < 0 || loc < lowestLocation)
                        {
                            lowestLocation = loc;
                        }
                    }

                }
            }

            Console.WriteLine("The lowest location number of the given seeds is: "+lowestLocation);
        }
    }
}
