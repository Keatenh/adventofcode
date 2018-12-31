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

with open(args.file, 'rt') as f1:  # Open file for reading of text data.
    # for line in f1:
    #     print(line.strip())
    lines = [line.strip() for line in sorted(f1.readlines())]
    print(lines)
    print(len(lines))
    areaList = [0]*len(lines)
    binList = [1]*len(lines)
    #For the given input, find our bounds/ranges in x,y:
    x_init = int(lines[0].split(',')[0])
    y_init = int(lines[0].split(',')[1])
    x_min = x_init
    x_max = x_init
    y_min = y_init
    y_max = y_init
    for i in range(len(lines)):
        hey = lines[i].split(',')
        x = int(hey[0])
        y = int(hey[1])
        if x < x_min:
            x_min = x
        if x > x_max:
            x_max = x
        if y < y_min:
            y_min = y
        if y > y_max:
            y_max = y
    x_min = max(0,x_min-1)
    x_max+=1
    y_min = max(0,y_min-1)
    y_max+=1
    print(x_min,y_min,x_max,y_max)

    #Loop through each point in our ranges, comparing each input coord to it,
    #taking the Manhattan Distance, looking for the minimum value (and which input 
    # it belongs to):
    m=y_min
    n=x_min
    i=0
    man_min = x_max+y_max

    while m < y_max:
        while n < x_max:
            for i in range(len(lines)):
                hey = lines[i].split(',')
                x = int(hey[0])
                y = int(hey[1])
                # calculate Manhattan Distace:
                man = abs(x-n)+abs(y-m)
                if man < man_min:
                    man_min = man
                    myIndex = i
                elif man == man_min:
                # Two inputs are equally closest, so neither can claim...
                    myIndex = None
            if myIndex is not None:
                areaList[myIndex]+=1
            # In order to eliminate infinite areas, we find the border cases:
            if n == x_min or m == y_min or n == x_max or m == y_max:
                binList[myIndex] = 0 
            n+=1
        m+=1
    print(areaList)
    print(binList)