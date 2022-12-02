package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strings"
)

/**
* Encode letter into int
 */
func encodeInt(letter string) int {
	if letter == "A" || letter == "X" {
		return 1
	} else if letter == "B" || letter == "Y" {
		return 2
	} else if letter == "C" || letter == "Z" {
		return 3
	}
	return -1
}

/**
* Picks matchup for rock/paper/scissors
* 1 = Rock, Lose
* 2 = Paper, Draw
* 3 = Scissors, Win
 */
func pickMatchup(oppChoice int, outcome int) int {
	if outcome == 2 {
		return oppChoice
	}
	if oppChoice == 1 {
		if outcome == 3 {
			return 2
		} else {
			return 3
		}

	} else if oppChoice == 2 {
		if outcome == 3 {
			return 3
		} else {
			return 1
		}
	} else if oppChoice == 3 {
		if outcome == 3 {
			return 1
		} else {
			return 2
		}

	}
	return -1
}

/**
* Read file and return 2 integer encoded arrays
 */
func readInputFileAsTwoArrays(filePath string) ([]int, []int) {
	var opponent []int
	var player []int

	file, err := os.Open(filePath)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		fmt.Println()
		entries := strings.Split(scanner.Text(), " ")

		opponent = append(opponent, encodeInt(entries[0]))
		player = append(player, encodeInt(entries[1]))
	}

	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}

	return opponent, player
}

/**
* Part 1 - Find your score based on map of opponent vs your RPS choice
 */
func ptOne(opp []int, pla []int) (int, int) {
	opponentPts := 0
	playerPts := 0
	// We assume the 2 arrays are complete pairs (same length for comparisons to be meaningful)

	// Shape Score Calculation (what players pick)
	for i := 0; i < len(opp); i++ {
		opponentPts += opp[i]
		playerPts += pla[i]
	}

	// Outcome Score Calculation (who wins matters)
	// Pt. 1
	// A Y 1 2 win
	// B X 2 1 lose
	// C Z 3 3 draw

	for i := 0; i < len(opp); i++ {
		oppi := opp[i]
		plai := pla[i]

		// Draw / Tie Conditions
		if oppi == plai {
			opponentPts += 3
			playerPts += 3
		} else if (plai == 1 && oppi == 3) || (plai == 3 && oppi == 2) || (plai == 2 && oppi == 1) { // Win Conditions
			playerPts += 6
		} else {
			// Lose Conditions
			opponentPts += 6
		}
	}
	return opponentPts, playerPts
}

/**
* Part 2 - Find your score based on map of opponent RPS choice vs encoded outcome
 */
func ptTwo(opp []int, out []int) (int, int) {
	opponentPts := 0
	playerPts := 0
	// We assume the 2 arrays are complete pairs (same length for comparisons to be meaningful)

	// Pt. 2
	// A Y draw 1 1 => 3+1 = 4
	// B X lose 2 1 => 0+1 = 1
	// C Z win 	3 1 => 6+1 = 7

	// Shape Score Calculation (what players pick)
	// Outcome Score Calculation (who wins matters)

	for i := 0; i < len(opp); i++ {
		oppi := opp[i]
		plai := pickMatchup(opp[i], out[i])
		opponentPts += oppi
		playerPts += plai

		// Draw / Tie Conditions
		if oppi == plai {
			opponentPts += 3
			playerPts += 3
		} else if (plai == 1 && oppi == 3) || (plai == 3 && oppi == 2) || (plai == 2 && oppi == 1) { // Win Conditions
			playerPts += 6
		} else {
			// Lose Conditions
			opponentPts += 6
		}
	}
	return opponentPts, playerPts
}

func main() {

	opp, pla := readInputFileAsTwoArrays("./input/pt1.txt")
	// opponentPts, playerPts := ptOne(opp, pla)
	opponentPts, playerPts := ptTwo(opp, pla)
	log.Printf("Opponent points: %d", opponentPts)
	log.Printf("Player points: %d", playerPts)

}
