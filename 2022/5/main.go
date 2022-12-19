package main

import (
	"bufio"
	"bytes"
	"fmt"
	"log"
	"os"
	"regexp"
	"strconv"
	"strings"
)

/**
* Want to work implement stacks (LIFO) using Golang slices
* educative.io/answers/how-to-implement-a-stack-in-golang
 */
type Stack []string

func (s *Stack) isEmpty() bool {
	return len(*s) == 0
}

func (s *Stack) push(str string) {
	*s = append(*s, str)
}

func (s *Stack) unshift(str string) {
	*s = append([]string{str}, *s...)
}

func (s *Stack) pop() (string, bool) {
	if s.isEmpty() {
		return "", false
	} else {
		//Get topmost element
		index := len(*s) - 1
		element := (*s)[index]
		//Slice to remove from stack
		*s = (*s)[:index]

		return element, true
	}
}

/**
* https://github.com/golang/go/wiki/SliceTricks#insert
 */
func Insert(s []string, k int, vs ...string) []string {
	if n := len(s) + len(vs); n <= cap(s) {
		s2 := s[:n]
		copy(s2[k+len(vs):], s[k:])
		copy(s2[k:], vs)
		return s2
	}
	s2 := make([]string, len(s)+len(vs))
	copy(s2, s[:k])
	copy(s2[k:], vs)
	copy(s2[k+len(vs):], s[k:])
	return s2
}

func main() {

	// Open file
	file, err := os.Open("./input/puzzle.txt") //./input/demo.txt
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	// Read lines of file

	// Initial Read to determine how many stacks we want
	// fmt.Println("PRE SCANNING FILE...")

	// Need to interact with bytes buffer to reprocess lines we already read later
	buf := bytes.Buffer{}

	searching := true
	preScan := bufio.NewScanner(file)
	columns := "0"
	for preScan.Scan() {
		text := preScan.Text()
		fmt.Fprintln(&buf, text) // save for reprocessing later
		//When we hit the empty line we are done searching
		if text == "" {
			searching = false
		}
		if searching {
			entries := strings.Split(text, "")
			for i := 1; i < len(entries); i += 4 {
				columns = entries[i]
			}
		}
	}
	stackCount, err := strconv.Atoi(columns)

	// Create Slice of Slices to contain all stack info:
	stacks := make([]Stack, stackCount)

	//Separate into Setup and Operations Phases
	// fmt.Println("SCANNING BUFFER...")
	operations := false
	scanner := bufio.NewScanner(&buf)

	for scanner.Scan() {
		text := scanner.Text()
		//Determine which set of instructions we are using by the line break
		if text == "" {
			operations = true
		}
		if operations {
			// B: Perform stack operations based on 2nd set of input

			// Get the numeric instructions we care about
			re := regexp.MustCompile("[0-9]+")
			strs := re.FindAllString(text, -1)
			nums := make([]int, 3)
			for i, s := range strs {
				nums[i], _ = strconv.Atoi(s)
			}

			// Use 2nd & 3rd columns to make move
			// Translate numbers to 0-indexed stacks
			fromStack := nums[1] - 1
			toStack := nums[2] - 1

			//Use the 1st column to determine number of moves
			for i := 0; i < nums[0]; i += 1 {
				temp, popped := stacks[fromStack].pop()
				if popped {
					/**
					* Part 1
					* Move one at a time
					 */
					// stacks[toStack].push(temp)

					/**
					* Part 2
					* Move N at a time, by inserting 1 index farther back everytime we iterate
					 */
					stacks[toStack] = Insert(stacks[toStack], len(stacks[toStack])-i, temp)
				}
			}
		} else {
			// A: Parse 1st set of input into stacks
			/**
			 * Characters we want to read are at positions: 1,5,9...
			 * Due to spaces and brackets
			 * Generalized ->  4n+1
			 */
			entries := strings.Split(text, "")
			for i := 1; i < len(entries); i += 4 {
				//stop when we get to the numbers row - we have that info in index
				if entries[i] == "1" {
					break
				}
				if entries[i] != " " {
					// Put the entries, where the top of the
					// input represents the end of the stack
					// Note that we let stack 1 actually be index 0, etc.
					stacks[(i-1)/4].unshift(entries[i])
				}
			}
		}

	}
	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}
	// C: Determine TOP of each stack and create a message
	word := ""
	for _, n := range stacks {
		letter, _ := n.pop()
		word += letter
	}
	fmt.Println(word)

}
