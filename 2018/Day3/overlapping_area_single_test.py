start_x1 = 1
start_y1 = 3
len_x1 = 4
len_y1 = 4
# delta_x1 = len_x1-1...
start_x2 = 3
start_y2 = 1
len_x2 = 4
len_y2 = 4

start_x12 = max(start_x1,start_x2)
end_x12 = min(start_x1+len_x1-1,start_x2+len_x2-1)
start_y12 = max(start_y1,start_y2)
end_y12 = min(start_y1+len_y1-1,start_y2+len_y2-1)

coords_12 = [start_x12,start_y12,end_x12,end_y12]

print(coords_12)