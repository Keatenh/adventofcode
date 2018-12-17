import os
import argparse
import sys
import string

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
with open(args.file, 'rt') as f1: # Open file for reading of text data.
    polymer = f1.readlines()[0]
f1.close()
# print(polymer)

def find_final_string(p,name):
    i=0
    while i < len(p)-1:
        if p[i].lower() == p[i+1].lower() and p[i] != p[i+1]:
            p = remove_at(i+1,p)
            p = remove_at(i,p)
            i=-1 #reset
        i=i+1
    # print("Our final "+name+" length is: "+str(len(p)))
    return len(p)

alphabet = list(string.ascii_lowercase)

# for i in range(len(alphabet)):
    # print(str(i)+" : "+alphabet[i]+" : "+alphabet[i].upper())

short_length = len(polymer)
for letter in alphabet:
    new_polymer = polymer.replace(letter,"").replace(letter.upper(),"")
    # print(new_polymer)
    new_length = find_final_string(new_polymer,"new_polymer")
    print("For letter "+letter+" the length is: "+str(new_length))
    if new_length < short_length:
        short_length = new_length

# find_final_string(polymer,"polymer")
print("The shortest result is: "+str(short_length))

# 4652 is correct!
