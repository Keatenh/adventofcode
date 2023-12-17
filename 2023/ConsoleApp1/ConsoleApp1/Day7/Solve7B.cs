using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1.Day7
{
    class HandB
    {
        public string cards;
        public int bid;
        public int type;
        public int jokerCount;
        
        public HandB(string cardsString, int bidInt)
        {
            cards = cardsString;
            bid = bidInt;
            type = score(countAlikeChars(cards), this.jokerCount);
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
            int j;
            dict.TryGetValue('J', out j);
            if (j > 0)
            {
                jokerCount = j;
                dict.Remove('J');
            }

            //return the dictionary, sorted by value
            return dict.OrderByDescending(x => x.Value).ToDictionary(x => x.Key, x => x.Value);
        }

        int score(Dictionary<char, int> dict, int jokers)
        {
            int score = 0;
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
                score = 6;
            }
            if (max == 4)
            {
                score = 5;
            }
            if (max == 3)
            {
                //We are overwriting a variable instead of returning,
                //so had to swap order for these in B compared to A
                score = 3;
                if (secondHighest == 2)
                {
                    score = 4;
                }
            }
            if (max == 2)
            {
                score = 1;
                // max & 2nd highest can both be 2, as this just depends on a sorted list
                if (secondHighest == 2)
                {
                    score = 2;
                }
                
            }

            //Transform hand to optimal hand based on number of jokers available:
            switch (jokers)
            {
                case 1:
                    switch (score)
                    {
                        case 1:
                            score = 3;
                            break;
                        case 2:
                            score = 4;
                            break;
                        case 3:
                            score = 5;
                            break;
                        case 5:
                            score = 6;
                            break;
                        default: //0
                            score = 1;
                            break;
                    }
                    break;
                case 2:
                    switch (score)
                    {
                        case 1:
                            score = 5;
                            break;
                        case 3:
                            score = 6;
                            break;
                        default: //0
                            score = 3;
                            break;
                    }
                    break;
                case 3:
                    switch (score)
                    {
                        case 1:
                            score = 6;
                            break;
                        default: //0
                            score = 5;
                            break;
                    }
                    break;
                case 4:
                    score = 6;
                    break;
                case 5: //ALL Jokers
                    score = 6;
                    break;
                default:
                    break;
            }

            return score;
        }
    }
    
    public class Solve7B
    {

        private string inputFile = @"..\..\..\Day7\Input.txt";

        List<HandB> hands = new List<HandB>();
        double total = 0;

        public Solve7B()
        {

            //A,K,Q,T,9,8,7,6,5,4,3,2,J is the NEW card strength order
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
                            cn = 1;
                            break;
                        case 'Q':
                            cn = 11;
                            break;
                        case 'K':
                            cn = 12;
                            break;
                        case 'A':
                            cn = 13;
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
                hands.Add(new HandB(data[0], int.Parse(data[1])));
            }
            //TODO: why CompareTo -vs- > ??
            hands.Sort((x, y) => x.type.CompareTo(y.type));

            //Additionally, sort based on characters in HandB.cards
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
                            HandB current = hands[i];
                            HandB next = hands[i + 1];
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
            foreach (HandB hand in hands)
            {
                total += n * hand.bid;
                n++;
            }

            Console.WriteLine("Total Winnings: " + total);
        }
    }
}
