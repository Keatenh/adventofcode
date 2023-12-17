using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1.Day7
{
    class Hand
    {
        public string cards;
        public int bid;
        public int type;
        
        public Hand(string cardsString, int bidInt)
        {
            cards = cardsString;
            bid = bidInt;
            type = score(countAlikeChars(cards));
        }
        Dictionary<char, int> countAlikeChars(string input)
        {
            Dictionary<char, int> dict = new Dictionary<char, int>();
            foreach (char c in input)
            {
                // build values in dictionary
                int i;
                dict.TryGetValue(c, out i);
                i++;
                dict[c] = i;
            }

            //return the dictionary, sorted by value
            return dict.OrderByDescending(x => x.Value).ToDictionary(x => x.Key, x => x.Value);
        }

        int score(Dictionary<char, int> dict)
        {
            int i = 0;
            int max = 0;
            int secondHighest = 0;
            // check for highest occurances
            foreach (var item in dict)
            {
                if (i == 0)
                {
                    max = item.Value;
                }
                else if (i == 1)
                {
                    secondHighest = item.Value;
                }
                i++;
            }
            //Console.WriteLine("max: " + max);

            //types:
            //5 of a kind   -> 6
            //4 of a kind   -> 5
            //Full house    -> 4
            //3 of a kind   -> 3
            //2 pair        -> 2
            //1 pair        -> 1
            //high card     -> 0

            if (max == 5)
            {
                return 6;
            }
            if (max == 4)
            {
                return 5;
            }
            if (max == 3)
            {
                if (secondHighest == 2)
                {
                    return 4;
                }
                return 3;
            }
            if (max == 2)
            {
                // max & 2nd highest can both be 2, as this just depends on a sorted list
                if(secondHighest == 2)
                {
                    return 2;
                }
                return 1;
            }
            return 0;
        }
    }
    
    public class Solve7A
    {

        private string inputFile = @"..\..\..\Day7\Input.txt";

        List<Hand> hands = new List<Hand>();
        double total = 0;

        public Solve7A()
        {

            //A,K,Q,J,T,9,8,7,6,5,4,3,2 is the card strength order
            int cardTypeNum(char c)
            {
                int cn;
                if (!int.TryParse(c.ToString(), out cn))
                {
                    switch (c)
                    {
                        case 'T':
                            cn = 10;
                            break;
                        case 'J':
                            cn = 11;
                            break;
                        case 'Q':
                            cn = 12;
                            break;
                        case 'K':
                            cn = 13;
                            break;
                        case 'A':
                            cn = 14;
                            break;
                        default:
                            cn = 0;
                            break;
                    }
                }
                return cn;
            }
            
            foreach (string line in File.ReadLines(inputFile, Encoding.UTF8))
            {
                string[] data = line.Trim().Split(' ');
                hands.Add(new Hand(data[0], int.Parse(data[1])));
            }
            //TODO: why CompareTo -vs- > ??
            hands.Sort((x, y) => x.type.CompareTo(y.type));

            //Additionally, sort based on characters in Hand.cards
            bool swapped;
            for (int m = 0; m < hands.Count-1; m++)
            {
                swapped = false;
                for (int i = 0; i < hands.Count - m - 1; i++)
                {
                    if (hands[i].type == hands[i + 1].type)
                    {
                        //Check for the 1st card that differs, without going out of bounds
                        int j = 0;
                        int x = cardTypeNum(hands[i].cards[j]);
                        int y = cardTypeNum(hands[i + 1].cards[j]);
                        while (x == y && j < 5)
                        {
                            j++;
                            x = cardTypeNum(hands[i].cards[j]);
                            y = cardTypeNum(hands[i + 1].cards[j]);
                        }

                        //when we have the cards that differ, compare them and swap if we are out of order
                        if (x > y)
                        {
                            Hand current = hands[i];
                            Hand next = hands[i + 1];
                            hands[i + 1] = current;
                            hands[i] = next;
                            swapped = true;
                        }
                    }
                }
                if (swapped == false) { break; }
            }

            //Console.WriteLine("Hands: " + hands);

            //Calculate winnings of each hand based on bid & position in array
            int n = 1;
            foreach (Hand hand in hands)
            {
                total += n * hand.bid;
                n++;
            }

            Console.WriteLine("Total Winnings: " + total);
        }
    }
}
