#Settings.OcrDataPath = "/home/rd/workspace/sikulix"
Settings.OcrLanguage = "chi_sim"
#Settings.OcrLanguage = "eng"
r=Region(30,222,178,48)
#r=Region(194,383,1030,39)
lines = r.collectLines() # a list of match objects
firstLineMatch = lines[0] # the region on screen containing the line
firstLine = firstLineMatch.getText()
print(firstLine)
