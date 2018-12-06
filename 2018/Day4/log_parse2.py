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
    max_max_count = 0
    max_max_index = 0
    method_2_guard = 0
    n=0
    while n < len(guard_dict[key]):
        diff = guard_dict[key][n+1] - guard_dict[key][n]
        diff_sum+=diff
        max_count=0
        i=0
        while i<60:
            index_count=0
            if i >= guard_dict[key][n] and i < guard_dict[key][n+1]:
                    index_count+=1
            if index_count > max_count:
                max_count = index_count
                max_index = i
            i+=1
        n+=2
    if max_count > max_max_count:
        max_max_count = max_count
        max_max_index = max_index
        method_2_guard = int(key)
    # print(key+":")
    # print(diff_sum)
    sum_dict[key] = diff_sum
print(sum_dict)

print("The most sleep for a certain minute belongs to guard ID: "+str(method_2_guard))
print("That minute with most overlap is: "+str(max_max_index))
answer2 = method_2_guard*max_max_index
print("Answer2: "+str(answer2))

max_sum = 0
final_key = "invalid"
for key in sum_dict:
    if sum_dict[key] >= max_sum:
        max_sum = sum_dict[key]
        final_key = key
print("The most minutes belong to guard ID: "+final_key)

# Get the minute with the highest frquency for our final guard:

print("The minute with most overlap is: "+str(max_index))

#Question Statement:
# What is the ID of the guard you chose multiplied by the minute you chose?
guard_id = int(final_key)
answer = guard_id*max_index
print("Answer: "+str(answer))