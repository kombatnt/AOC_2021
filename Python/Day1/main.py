
# ---- PART 1 ----

# Read in the file.
with open('D:/Repositories/AOC_2021/app/src/main/resources/Day1.txt') as file:
    lines = file.readlines()
    lines = [line.rstrip() for line in lines]

print('Read ', len(lines), ' lines of data')

numIncreases = 0
for i in range(1, len(lines)):
    lastLine = int(lines[i - 1])
    line = int(lines[i])
    if line > lastLine:
        print(lastLine, ' -> ', line, ' (increased)')
        numIncreases += 1
    elif line < lastLine:
        print(lastLine, ' -> ', line, ' (decreased)')
    else:
        print(lastLine, ' -> ', line, ' (no change)')

file.close()
print('The depth increased ', numIncreases, ' times.')