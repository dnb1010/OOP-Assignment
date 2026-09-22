import math
import sys

if hasattr(sys.stdout, "reconfigure"):
    sys.stdout.reconfigure(encoding="utf-8")

class Student:
    __total_students = 0
    def __init__(self, name, GPA):
        # Private
        self.__name = name
        self.__GPA = 0.0

        # Setter
        self.gpa = GPA

        Student.__total_students += 1

    @classmethod
    def print_total_students(cls):
        print(f"The total number of students: {cls.__total_students}")

    # Đóng gói
    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, value):
        self.__name = value

    @property
    def gpa(self):
        return self.__GPA

    @gpa.setter
    def gpa(self, value):
        if 0.0 <= value <= 10.0:
            self.__GPA = value
        else:
            raise ValueError("GPA phải nằm trong khoảng từ 0.0 đến 10.0")

class Calculator:
    @staticmethod
    def add(a, b, c = None):
        if c is not None:
            return a + b + c
        return a + b


Caculator = Calculator


class Circle:
    def __init__(self, radius):
        self.__radius = 0.0
        self.radius = radius

    @property
    def radius(self):
        return self.__radius

    @radius.setter
    def radius(self, value):
        if value > 0:
            self.__radius = value
        else:
            raise ValueError("Bán kính phải là một số dương")

    def get_area(self):
        return math.pi * (self.radius **2)

    def get_parameter(self):
        return 2 * math.pi * self.radius

def main():

    print("====Kiểm tra lớp Student====")

    sv1 = Student("Bảo", 9.0)
    sv2 = Student("An", 3.2)
    try:
        sv3 = Student("Anh", 11)
    except ValueError as error:
        print(f"Không tạo được sinh viên: {error}")

    Student.print_total_students()

    print("====Kiểm tra lớp Calculator====")
    print(f"Add(5, 10) = {Calculator.add(5, 10)}")
    print(f"Add(2.5, 2.5) = {Calculator.add(2.5, 2.5)}")
    print(f"Add(1, 2, 3) = {Calculator.add(1, 2, 3)}")

    print("====Kiểm tra lớp Circle====")
    radius1 = Circle(2)
    try:
        radius2 = Circle(-1)
    except ValueError as error:
        print(f"Không tạo được hình tròn: {error}")
    print(f"Diện tích của radius1 = {radius1.get_area()}")
    print(f"Chu vi của radius1 = {radius1.get_parameter()}")

if __name__ == "__main__":
    main()
