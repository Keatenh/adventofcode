# Notes:
# Lesson learned for me is that if we only care about the uniqueness of a value to query,
# Should be using "sets"; tried creating a list of sums I created and it took far too many resources.
# https://bytes.com/topic/python/answers/543980-best-way-handle-large-lists

# some initialization:
sum = 0
with open ('input.txt', 'rt') as f1:  # Open file for reading of text data.
    lines = f1.readlines()
    # list comprehension for int type:
    int_list = [int(i) for i in lines]
    int_len = len(int_list)
    print("length of int list found: "+str(int_len))
    i = 0
    # sum_list = []
    sum_set = {""} #Could replace with = set() for truly empty set
    while i < int_len:
        sum += int_list[i]
        if sum in sum_set:
            print("Number of unique sums: "+str(len(sum_set)))
            break
        # sum_list.append(sum)
        sum_set.add(sum)
        # print("sum from va: "+str(sum))
        # print("current sum: "+str(sum_list[i]))
        # if len(sum_list) % 1000 is 0:
        #     print("num of sums: "+str(len(sum_list)))
        i+=1
        if i >= int_len:
            i=0 #restart loop
    print("sum at time of repeat: "+str(sum))
f1.close()