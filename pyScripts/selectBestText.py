# This is a sample Python script.
import g4f
import sys

sys.stdout.reconfigure(encoding='utf-8')
s = sys.argv[1] + '\n1.' + sys.argv[2] + '\n2.' + sys.argv[3] + '\n3.' + sys.argv[4]


response = g4f.ChatCompletion.create(
    model="gpt-3.5-turbo",
    messages=[{"role": "user", "content": s}],
    stream=True,
)


for message in response:
    print(message, flush=True, end='')


