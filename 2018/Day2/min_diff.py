comparisons = 0
final_string = ""
with open('input-github.txt', 'rt') as f1:  # Open file for reading of text data.
    # Stripping newlines from strings in created list:
    lines = [line.strip() for line in f1.readlines()]
    # Assuming our list is off all the same size string, we arbitrarily get the max diff from the first entry:
    min_diff = len(lines[0])
    print("Size of strings in list: "+str(min_diff))
    for i in range(len(lines)):
        for j in range(i+1, len(lines)):
            # compare(lines[i],lines[j])
            # Note the total number of comparisons matches combinations in mathematics
            # n!/((n-r)!*r!)
            # e.g. from num of lines (7 in test), pick 2 (7!/((7-2)!*2! = 21)
            # print("comparing "+str(i)+" and "+str(j)+"...")
            l_diff = 0
            for l in range(len(lines[i])):
                if lines[i][l] != lines[j][l]:
                    # print(lines[i][l]+' != '+lines[j][l])
                    l_diff+=1
                # else:
                    # print(lines[i][l]+' == '+lines[j][l])
            if l_diff < min_diff: #Mark if we found the best diff so far
                min_diff = l_diff
                coords = [i,j]
            comparisons+=1
            # print(str(comparisons)+" diff: "+str(l_diff))
f1.close()
print("Total number of comparisons: "+str(comparisons))
print("minimum diff found: "+str(min_diff))
print("Location of close strings: "+str(coords))
print(lines[coords[0]])
print(lines[coords[1]])
for l in range(len(lines[coords[0]])):
    if lines[coords[0]][l] == lines[coords[1]][l]:
        final_string += lines[coords[0]][l]
print("common letters: "+final_string)

# s1 = 'HELPMEPLZ'
# s2 = 'HELPNEPLX'
# list = [i for i in range(len(s1)) if s1[i] != s2[i]]
# print(str(len(list)))