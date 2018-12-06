import os
import argparse
import sys
parser = argparse.ArgumentParser()
# Positional Argument:
parser.add_argument('file',
    help='The name of the file providing input'
)
args = parser.parse_args()

delimiter = "#"
delim_index = 25
guard_dict = {}
minutes_start = 15

with open(args.file, 'rt') as f1:  # Open file for reading of text data.
    # Stripping newlines from strings in created list:
    # sorted() here is some magic that will arrange input text by timestamp ascending
    lines = [line.strip() for line in sorted(f1.readlines())]
    for i in range(len(lines)):
        line_number = i+1
        if delimiter in lines[i]:
            # print(lines[i])
            # print(lines[i].find(delimiter)) #based on input: will always return 25 for #
        # Construct guard ID:
            char = delim_index+1
            guard_id = ""
            while not lines[i][char].isspace():
                guard_id += lines[i][char]
                # print(guard_id)
                char+=1
        else: #Not an identifier line/log
            # Get the minutes from the timestamp:
            # Note in slice notation, val after colon is first excluded char/list item
            minute = int(lines[i][minutes_start:minutes_start+2])
            if guard_id not in guard_dict:
                guard_dict[guard_id] = [minute]
            else:
                guard_dict[guard_id].append(minute)
f1.close()
# print(guard_dict)
# Do stuff with generated dictionary of lists:
sum_dict = dict.fromkeys(guard_dict,[])
# print(sum_dict)

for key in guard_dict:
    diff_sum = 0
    n=0
    while n < len(guard_dict[key]):
        diff = guard_dict[key][n+1] - guard_dict[key][n]
        diff_sum+=diff
        n+=2
    # print(key+":")
    # print(diff_sum)
    sum_dict[key] = diff_sum
print(sum_dict)

max_sum = 0
final_key = "invalid"
for key in sum_dict:
    if sum_dict[key] >= max_sum:
        max_sum = sum_dict[key]
        final_key = key
print("The most minutes belong to guard ID: "+final_key)

# Get the minute with the highest frquency for our final guard:
max_count=0
i=0
while i<60:
    n=0
    index_count=0
    while n < len(guard_dict[final_key]):
        # The guard counts as awake in the last minute, so that cannot count in sleep window:
        if i >= guard_dict[final_key][n] and i < guard_dict[final_key][n+1]:
            # print(str(guard_dict[final_key][n])+","+str(guard_dict[final_key][n+1])+" - "+"in range of: "+str(i))
            index_count+=1
        # else:
            # print("not in range of: "+str(i)+"...")
        n+=2
    if index_count > max_count:
        max_count = index_count
        max_index = i
    i+=1
print("The minute with most overlap is: "+str(max_index))

#Question Statement:
# What is the ID of the guard you chose multiplied by the minute you chose?
guard_id = int(final_key)
answer = guard_id*max_index
print("Answer: "+str(answer))