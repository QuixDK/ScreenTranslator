# This is a sample Python script.
import easyocr
import sys
from os import path
sys.stdout.reconfigure(encoding='utf-8')
img = sys.argv[1]

reader = easyocr.Reader(['en'])

result = reader.readtext(img, detail=0)

for message in result:
    print(message, flush=True, end='')
