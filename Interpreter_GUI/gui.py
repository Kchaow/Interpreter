from tkinter import *
from tkinter import ttk
import requests

BASE_URL = "http://localhost:8080"


def run():
    input_area.tag_remove("error", "1.0", END)
    output_area["text"] = ""
    request = {
        "listing": input_area.get("1.0", END)[:-1]
    }
    response = requests.post(f"{BASE_URL}/run", json=request)
    if response.status_code == 200:
        var_table = response.json()["table"]
        result_text = "Результаты вычислений: \n"
        for key in var_table:
            result_text += f"{key} = {var_table[key]}\n"
        output_area["text"] = result_text
    elif response.status_code == 400:
        error_status = response.json()
        output_area["text"] = error_status["error"]
        input_area.insert(END, " ")
        print(f"{error_status['line']}.{error_status['symbolCount'] - 1}")
        print(f"{error_status['line']}.{error_status['symbolCount'] + error_status['symbolLength']}")
        input_area.tag_add("error",
                           f"{error_status['line']}.{error_status['symbolCount'] - 1}",
                           f"{error_status['line']}.{error_status['symbolCount'] + error_status['symbolLength'] - 1}")
        input_area.tag_configure("error", background='#FF0000')


root = Tk()
root.title("Interpreter")
root.geometry("1200x700")
root.resizable(False, False)

bnf = """Язык = "Begin" Звено...Звено Последнее Опер...Опер "End"
Звено = "First" Цел ","... Цел ! "Second" Перем...Перем
Последнее = Перем ";"... Перем
Опер = Метка ":" Перем "=" Прав.часть
Прав.часть = </"-"/> Часть_1 ...["+"!"-"] Часть_1
Часть_1 = Часть_2 ...["/"!"*"] Часть_2
Часть_2 = Часть_3 ..."^" Часть_3
Часть_3 = </функц..функц/> Часть_5
Часть_5 = Перем ! Цел ! "(" Прав.часть ")"
Перем = Букв </Симв...Симв/>
Симв = Букв ! Цел
Цел = Цифр...Цифр
Букв = "A" ! "B" !...! "Z"
Цифр = "1" ! "2" !...! "7" """

numbers = """1.
2.
3.
4.
5.
6.
7.
8.
9.
10.
11.
12.
13.
14.
15.
16.
17.
18.
19.
20.
21.
22.
23."""
font = ("Arial", 12)

stroke_numbers_area = ttk.Label(text=numbers, font=font)
stroke_numbers_area.place(relx=0.03, rely=0.01)

input_area = Text()
input_area.place(relx=0.05, rely=0.01, relheight=0.6, relwidth=0.52)
input_area.configure(font=font)

bnf_show = ttk.Label(font=font, text=bnf)
bnf_show.place(relx=0.6, rely=0.01)

output_area = ttk.Label(font=font, borderwidth=1, relief="solid")
output_area.place(relx=0.05, rely=0.6, relheight=0.3, relwidth=0.52)

run_button = ttk.Button(text="Выполнить", command=run)
run_button.place(relx=0.7, rely=0.65, relheight=0.2, relwidth=0.2)

root.mainloop()
