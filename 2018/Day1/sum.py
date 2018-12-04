# some initialization:
sum = 0
with open ('input.txt', 'rt') as f1:  # Open file for reading of text data.
    lines = f1.readlines()
    # list comprehension for int type:
    int_list = [int(i) for i in lines]
    for number in int_list:
        # print(number)
        sum += number
        # print("current sum: "+str(sum))
    print("final sum: "+str(sum))
f1.close()