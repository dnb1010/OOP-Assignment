class Person():
    def __init__(self, ID, full_name, email):
        self.ID = ID
        self.full_name = full_name
        self.email = email

    def display_info(self):
        print(f"Mã định danh: {self.ID}")
        print(f"Họ tên: {self.full_name}")
        print(f"Email: {self.email}")

class Student(Person):
    def __init__(self, ID, full_name, email, major, accumulated_credits):
        super().__init__(ID, full_name, email)
        if type(accumulated_credits) is not int or accumulated_credits < 0:
            raise ValueError("Số tín chỉ tích lũy phải là số nguyên dương!")
        self.major = major
        self.accumulated_credits = accumulated_credits

    def display_info(self):
        super().display_info()
        print(f"Chương trình đào tạo: {self.major}")
        print(f"Số tín chỉ tích lũy: {self.accumulated_credits}")

class Lecturer(Person):
    def __init__(self, ID, full_name, email, department, experience):
        super().__init__(ID, full_name, email)
        if type(experience) is not int or experience < 0:
            raise ValueError("Số năm kinh nghiệm phải là số nguyên dương")
        self.department = department
        self.experience = experience

    def display_info(self):
        super().display_info()
        print(f"Đơn vị công tác: {self.department}")
        print(f"Số năm kinh nghiệm: {self.experience}")


class Course():
    def __init__(self, course_code, course_name, credits, number_of_students=0):
        self.course_code = course_code
        self.course_name = course_name
        self.credits = credits
        if type(number_of_students) is not int or number_of_students < 0:
            raise ValueError("Số sinh viên phải là số nguyên dương")
        self._number_of_students = number_of_students

    @property
    def credits(self):
        return self._credits

    @credits.setter
    def credits(self, value):
        if type(value) is not int or not 1 <= value <=6:
            raise ValueError("Số tín phải là số nguyên từ 1 đến 6")
        self._credits = value

    @property
    def number_of_students(self):
        return self._number_of_students

    @number_of_students.setter
    def number_of_students(self, value):
        if type(value) is not int or value < 0:
            raise ValueError("Số sinh viên phải là số nguyên dương")
        self._number_of_students = value

    def register_student(self):
        self._number_of_students += 1

    def cancel_registration(self):
        if self._number_of_students == 0:
            print("Không thể hủy: chưa có sinh viên nào đăng ký")
            return False
        self._number_of_students -= 1
        return True

    def calculate_theory_hours(self):
        return self.credits * 15

    def calculate_theory_hourse(self):
        return self.calculate_theory_hours()

    def display_info(self):
        print(f"Mã học phần: {self.course_code}")
        print(f"Tên học phần: {self.course_name}")
        print(f"Số tín chỉ: {self.credits}")
        print(f"Số sinh viên đã đăng kí: {self.number_of_students}")
    
def main():
    # Tạo hai học phần 
    course1 = Course("MI4024", "Phân tích số liệu", 2)
    course2 = Course("MI3042", "Phương pháp số", 2)

    # Hiển thị thông tin 
    print("Thông tin khóa học")
    for course in (course1, course2):
        course.display_info()
        print()

    # Thao tác đăng kí
    for _ in range(3):
        course1.register_student()

    # Hủy
    course1.cancel_registration()

    # Số giờ lý thuyết của từng học phần
    print("Số giờ lý thuyết")
    for course in (course1, course2):
        print(f"{course.course_name}: {course.calculate_theory_hours()} giờ")

    # Hiển thị lại trạng thái của các đối tượng
    print("Thông tin sau cập nhật")
    for course in (course1, course2):
        course.display_info()
        print()


    # Minh họa hai nhóm người 
    print("=== NGƯỜI THAM GIA (DỮ LIỆU MINH HỌA) ===")
    people = [
        Student("SV001", "Nguyễn Văn An", "an@example.com", "Toán - Tin", 60),
        Lecturer("GV001", "Trần Thị Bình", "binh@example.com", "Khoa Toán - Tin", 8),
    ]
    for person in people:
        person.display_info()
        print()

    print("=== HỦY KHI CHƯA CÓ SINH VIÊN ===")
    course2.cancel_registration()


if __name__ == "__main__":
    main()
