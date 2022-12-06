package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strings"
)

func readInputFile(filePath string) []string {
	var lines []string

	file, err := os.Open(filePath)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := scanner.Text()
		if line == "" {
			line = "-1"
		}

		lines = append(lines, line)
	}

	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}

	return lines
}

/**
* Encode string of len 1 -> rune -> int
* Satisfys 	a-z = 1-26
* 	 		A-Z = 27-52
 */
func encodeInt(letter string) int {
	// fmt.Println(letter)
	i := int(int32([]rune(letter)[0]))
	if i > 96 { //lowercase a-z
		return i - 96
	} else { //uppercase A-Z
		return i - 38
	}
}

/**
* Chunks array into sub arrays of length N
* https://stackoverflow.com/questions/35179656/slice-chunking-in-go
 */
func chunkSlice[T any](items []T, chunkSize int) (chunks [][]T) {
	//While there are more items remaining than chunkSize...
	for chunkSize < int(len(items)) {
		//We take a slice of size chunkSize from the items array and append it to the new array
		chunks = append(chunks, items[0:chunkSize])
		//Then we remove those elements from the items array
		items = items[chunkSize:]
	}
	//Finally we append the remaining items to the new array and return it
	return append(chunks, items)
}

/**
* Pt 1 - split lines and half and find matching
* characters between substrings.
* Take sum of numerical points attributed to each common letter
 */
func partOne(input []string) int {
	prioritySum := 0

	for _, l := range input {
		line := strings.Split(l, "")
		//split line in half to compare letters
		wordPairs := chunkSlice(line, len(line)/2)
		// fmt.Println(wordPairs)
		matchFound := false
		for _, i := range wordPairs[0] {
			for _, j := range wordPairs[1] {
				if i == j {
					prioritySum += encodeInt(i)
					matchFound = true
					break
				}
			}
			//We should eventually find 1 match per group
			if matchFound {
				break
			}
		}
	}
	return prioritySum
}

/**
* Pt 2 - take groups of 3 lines and find common character bewteen the 3
* Take sum of numerical points attributed to each common letter
 */
func partTwo(input []string) int {
	prioritySum := 0
	for i := 0; i < len(input); i += 3 {
		letters0 := strings.Split(input[i], "")
		letters1 := strings.Split(input[i+1], "")
		letters2 := strings.Split(input[i+2], "")
		matchFound := false
		for _, j := range letters0 {
			for _, k := range letters1 {
				for _, l := range letters2 {
					if j == k && j == l && k == l {
						prioritySum += encodeInt(j)
						matchFound = true
						break
					}

				}
				//We should eventually find 1 match per group
				if matchFound {
					break
				}
			}
			//We should eventually find 1 match per group
			if matchFound {
				break
			}
		}
	}
	return prioritySum
}

func main() {
	input := readInputFile("./input/puzzle.txt")
	// fmt.Println(partOne(input))
	fmt.Println(partTwo(input))
}
