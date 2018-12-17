import os
import argparse
import sys

# Filename as argument:
parser = argparse.ArgumentParser()
# Positional Argument:
parser.add_argument('file',
    help='The name of the file providing input'
)
args = parser.parse_args()

# Remove character at index i for string:
def remove_at(i,s):
    return s[:i]+s[i+1:]

# Get string from file:
with open(args.file, 'rt') as f1:  # Open file for reading of text data.
    polymer = f1.readlines()[0]
f1.close()
# print(polymer)

i=0
while i < len(polymer)-1:
    if polymer[i].lower() == polymer[i+1].lower() and polymer[i] != polymer[i+1]:
        # print("Hmm, "+polymer[i]+" & "+polymer[i+1]+" are the same letter.")
        polymer = remove_at(i+1,polymer)
        # print(polymer)
        polymer = remove_at(i,polymer)
        # print(polymer)

        # i=i-2
        i=-1 #reset
    # else:
    #     print("Hey! "+polymer[i]+" & "+polymer[i+1]+" are NOT the same letter.")
    i=i+1

# print("Our final polymer is: "+polymer)
print("Our final polymer length is: "+str(len(polymer)))

# 44337 is too high -> comparing .lower() chars
# 23087 is too high -> and making sure char[i] != char[i+1]
# 11670 is too high -> resetting i to 0 instead of i-2
# 11668 is too correct! -> resetting i to -1 instead of 0 
# ^ (iterator increments before going to next stage of while loop,
# so the previous was ignoring comparisons of the first 2 characters)