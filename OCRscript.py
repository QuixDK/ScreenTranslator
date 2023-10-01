import easyocr
import json

def recognize_text(image_path):
    reader = easyocr.Reader(['en'])
    result = reader.readtext(image_path)

    # Функция для преобразования числовых значений int32 в int
    def convert_to_int(o):
        if isinstance(o, int32):
            return int(o)
        raise TypeError

    return json.dumps(result, default=convert_to_int)

if __name__ == "__main__":
    import sys
    if len(sys.argv) != 2:
        print("Usage: python ваш_скрипт.py путь_к_изображению")
        sys.exit(1)

    image_path = sys.argv[1]
    result = recognize_text(image_path)
    print(result)
