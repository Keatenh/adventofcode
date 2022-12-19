package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"
)

func main() {
	// Part1: Need to determine how many duplicate sections exist
	// (contained sets in pairs)
	duplicateSets := 0

	//Part2: Need to determine how many pairs have any overlap at all in pairs
	overlaps := 0

	// Open file
	file, err := os.Open("./input/puzzle.txt") //./input/demo.txt
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	// Read lines of file
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		// Give significance to each number we find
		entries := strings.Split(scanner.Text(), ",")
		minA, err := strconv.Atoi(strings.Split(entries[0], "-")[0])
		maxA, err := strconv.Atoi(strings.Split(entries[0], "-")[1])
		minB, err := strconv.Atoi(strings.Split(entries[1], "-")[0])
		maxB, err := strconv.Atoi(strings.Split(entries[1], "-")[1])
		if err != nil {
			log.Fatal(err)
		}
		// Determine if one set contains the other
		if (minA <= minB && maxA >= maxB) || (minA >= minB && maxA <= maxB) {
			duplicateSets += 1
		}
		// Determine if there is any overlap in the sets
		// An edge of B is contained by A, or B contains A entirely...
		if (minB >= minA && minB <= maxA) || (maxB >= minA && maxB <= maxA) || (minA >= minB && maxA <= maxB) {
			overlaps += 1
		}

	}

	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}

	fmt.Println(duplicateSets)
	//Note 209, 703 too low...
	fmt.Println(overlaps)

}
