package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
)

func readInputFileAsInt(filePath string) []int {
	var lines []int

	file, err := os.Open(filePath)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		// fmt.Println(scanner.Text())
		line := scanner.Text()
		if line == "" {
			line = "-1"
		}
		num, err := strconv.Atoi(line)
		if err != nil {
			log.Fatal(err)
		}
		lines = append(lines, num)
	}

	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}

	return lines
}

// Part 1 - Get the top sum
func maxSum(input []int) int {
	max := 0
	cur := 0
	delim := -1 //number representing new group

	for i, s := range input {
		if s != delim {
			cur += s
		}
		//If we hit the separation point, or the last index => check if we are max and reset current sum
		if s == delim || i == len(input)-1 {
			if cur > max {
				max = cur
			}
			//fmt.Println(cur)
			cur = 0
		}
	}
	return max
}

// Part 2 - Get the sum of top N sums (3 in our prompt)
func topNSum(input []int, N int) int {
	sumOfSums := 0
	//Create array of sums
	cur := 0    //currently calculated sum
	delim := -1 //number representing new group
	var sums []int

	for i, s := range input {
		if s != delim {
			cur += s
		}
		//If we hit the separation point, or the last index => check if we are max and reset current sum
		if s == delim || i == len(input)-1 {
			sums = append(sums, cur)
			cur = 0
		}
	}
	//Sort array of sums
	sort(sums)
	//Sum top N sums (3 in our prompt)
	topN := sums[len(sums)-N:]
	for _, v := range topN {
		sumOfSums += v
	}
	return sumOfSums
}

/**
* Mutates input array into ascending order
 */
func sort(input []int) []int {
	searching := true
	for searching == true {
		//If we make it through the array without changing anything we are done
		searching = false
		//Check each index against the one after it
		//Place the numbers in ascending order
		for i := 0; i < len(input)-1; i++ {
			if input[i+1] < input[i] {
				temp := input[i+1]
				input[i+1] = input[i]
				input[i] = temp
				searching = true
			}

		}
	}
	return input

}

func main() {
	// input := []int{1000, 2000, 3000, -1, 4000, -1, 5000, 6000, -1, 7000, 8000, 9000, -1, 10000}
	// fmt.Println(maxSum(readInputFileAsInt("./input/pt1.txt")))
	// fmt.Println(sort([]int{4, 5, 6, 3, 2, 23, 4, 24, 1, 34}))
	fmt.Println(topNSum(readInputFileAsInt("./input/pt1.txt"), 3))
}
