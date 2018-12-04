import os
import argparse
import sys
parser = argparse.ArgumentParser()
# Positional Argument:
parser.add_argument('file',
    help='The name of the file providing input'
)
args = parser.parse_args()

start_arr=[[] for i in range(2)]
length_arr=[[] for i in range(2)]
overlap_set = set()
# For Part 2, create a set of IDs for every entry in input that grants us an overlap:
entry_set = set()
# Get our input:
with open(args.file, 'rt') as f1:  # Open file for reading of text data.
    # Stripping newlines from strings in created list:
    lines = [line.strip() for line in f1.readlines()]

    # Form a complete set of integers to subtract entry set ids from later:
    complete_set = set(range(1,len(lines)+1))

    # for line in lines:
    for i in range(len(lines)):
        line_list = lines[i].split(" ")
        # print(line_list)
        start = [int(n.strip(':')) for n in line_list[2].split(",")]
        # print(start)
        start_arr[0].append(start[0])
        start_arr[1].append(start[1])
        length = [int(n) for n in line_list[3].split("x")]
        # print(length)
        length_arr[0].append(length[0])
        length_arr[1].append(length[1])
    # print("start_arr:")
    # print(start_arr)
    # print("length_arr:")
    # print(length_arr)
    for i in range(len(lines)):
        for j in range(i+1, len(lines)):
            temp_start_x = max(start_arr[0][i],start_arr[0][j])
            temp_end_x = min(start_arr[0][i]+length_arr[0][i]-1,start_arr[0][j]+length_arr[0][j]-1)
            temp_start_y = max(start_arr[1][i],start_arr[1][j])
            temp_end_y = min(start_arr[1][i]+length_arr[1][i]-1,start_arr[1][j]+length_arr[1][j]-1)
            temp_coords = [temp_start_x,temp_start_y,temp_end_x,temp_end_y]
            print("for "+str([i,j])+"...")
            if temp_start_x > temp_end_x or temp_start_y > temp_end_y:
                print("out of range, no overlap")
            else:
                print(temp_coords)
                # We have overlapped, so we want to track the entries that did so:
                # Note that we create a set of the IDs, not list indices so we must +1 to not start at zero.
                entry_set.add(i+1)
                entry_set.add(j+1)
                # Loop through the coordinates of the overlapped area to track every point:
                n = temp_coords[0]
                while n <= temp_coords[2]:
                    m = temp_coords[1]          # <- Note location of nested while loop counter initialization
                    while m <= temp_coords[3]:
                        coord = str(n)+"-"+str(m)
                        overlap_set.add(coord)
                        # print([n,m])
                        # print("overlap_set:")
                        # print(overlap_set)
                        m+=1
                    n+=1
f1.close()
# print(overlap_set)
print("size of set (Our total overlap area):")
print(len(overlap_set))
lonely_set = complete_set.difference(entry_set)
print("The set of non-overlapping IDS:")
print(lonely_set)