# ĐỒNG NGỌC BẢO 202418849

import math

# Hàm kiểm tra dữ liệu đầu vào
def check_text(value, field_name):
    if not isinstance(value, str) or not value.strip():
        raise ValueError(f"{field_name} không được rỗng.")
    return value.strip()


def check_number(value, field_name, positive=False):
    if isinstance(value, bool) or not isinstance(value, (int, float)):
        raise ValueError(f"{field_name} phải là số.")

    if not math.isfinite(value):
        raise ValueError(f"{field_name} phải là số hữu hạn.")

    if positive and value <= 0:
        raise ValueError(f"{field_name} phải lớn hơn 0.")

    if not positive and value < 0:
        raise ValueError(f"{field_name} không được âm.")

    return float(value)


class Employee:
    def __init__(
        self,
        employeeId: str = "UNKNOWN",
        fullName: str = "Unnamed employee",
        baseSalary: int | float = 0,
    ) -> None:
        self.__employeeId = check_text(employeeId, "Mã nhân sự")
        self.__fullName = check_text(fullName, "Họ và tên")
        self.__baseSalary = check_number(baseSalary, "Lương cơ bản")

    @property
    def employeeId(self) -> str:
        return self.__employeeId

    @property
    def id(self) -> str:
        return self.__employeeId

    @property
    def fullName(self) -> str:
        return self.__fullName

    @property
    def baseSalary(self) -> int | float:
        return self.__baseSalary

    def increaseSalary(self, value: int | float, byPercentage: bool = False) -> None:
        amount = check_number(value, "Giá trị tăng lương", positive=True)
        if not isinstance(byPercentage, bool):
            raise TypeError("byPercentage phải là bool")

        if byPercentage:
            new_salary = self.__baseSalary * (1 + amount / 100)
        else:
            new_salary = self.__baseSalary + amount

        self.__baseSalary = check_number(new_salary, "Lương sau khi tăng")

    def calculateMonthlyCost(self) -> int | float:
        return self.__baseSalary

    def calculateMontlyCost(self) -> int | float:
        return self.calculateMonthlyCost()

    def displayInfo(self) -> None:
        print(f"Nhân viên: {self.employeeId} | {self.fullName} | {self.baseSalary}")

    def __del__(self):
        employee_id = getattr(self, "_Employee__employeeId", "?")
        print(f"[Hủy Employee] {employee_id}")
