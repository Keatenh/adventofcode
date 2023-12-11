using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection.Metadata.Ecma335;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1.Day5
{
    public class Solve5A
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

        public Solve5A() {

            long lowestLocation = -1;
            
            int lineNum = 0;
            long[] seeds = [];
            List<long[]>[] almanac = new List<long[]>[7];
            for (int i=0; i<almanac.Length; i++)
            {
                almanac[i] = new List<long[]>();
            }
            State state = State.Default;
            int setNum = -1;

            foreach (string line in File.ReadLines(@"..\..\..\Day5\Input.txt", Encoding.UTF8))
            {
                
                if (lineNum == 0)
                {
                    seeds = CreateIntArrayFromString(line.Split(':')[1]);
                }
                else if (line.Length == 0) //L1
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

            foreach (long seed in seeds)
            {
                long loc = TranslateAlmanac(almanac, seed);
                if (lowestLocation < 0 || loc < lowestLocation)
                {
                    lowestLocation = loc;
                }
            }

            Console.WriteLine("The lowest location number of the given seeds is: "+lowestLocation);
        }
    }
}
