import string
alphabet = list(string.ascii_lowercase)
ID = 'The quick brown fox jumps over the lazy dog.'
two_count = 0
three_count = 0
with open ('input-github.txt', 'rt') as f1:  # Open file for reading of text data.
    lines = f1.readlines()
    for ID in lines:
        # print("ID : "+ID)
        # For a given ID/line/string, do not increment specific counts if we already found a letter with that count
        # Replace this with creating a count set for each ID?
        counted2 = False
        counted3 = False
        for letter in alphabet:
            count = ID.count(letter)
            if count == 2:
                # print(letter+": "+str(count))
                if not counted2:
                    counted2 = True
                    two_count+=1
            elif count == 3:
                # print(letter+": "+str(count)+"!")
                if not counted3:
                    counted3 = True
                    three_count+=1
f1.close()
print("Final count of 2 letters: "+str(two_count))
print("Final count of 3 letters: "+str(three_count))
print("Checksum: "+str(two_count*three_count))