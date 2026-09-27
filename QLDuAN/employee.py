"""Nhân sự cơ sở dùng chung cho các nhóm dự án."""

from math import isfinite


def _validate_non_empty_text(value: str, field_name: str) -> str:
    if not isinstance(value, str):
        raise TypeError(f"{field_name} phải là chuỗi.")
    normalized = value.strip()
    if not normalized:
        raise ValueError(f"{field_name} không được để trống.")
    return normalized


def _validate_number(value: int | float, field_name: str, *, positive: bool = False) -> int | float:
    if isinstance(value, bool) or not isinstance(value, (int, float)):
        raise TypeError(f"{field_name} phải là số nguyên hoặc số thực.")
    try:
        finite = isfinite(value)
    except OverflowError:
        finite = False
    if not finite:
        raise ValueError(f"{field_name} phải là số hữu hạn.")
    if (value <= 0 if positive else value < 0):
        qualifier = "lớn hơn 0" if positive else "không âm"
        raise ValueError(f"{field_name} phải {qualifier}.")
    return value


class Employee:
    """Nhân sự có mã, họ tên và lương cơ bản."""

    def __init__(
        self,
        id: str = "UNKNOWN",
        fullName: str = "Unnamed employee",
        baseSalary: int | float = 0,
    ) -> None:
        self._id = _validate_non_empty_text(id, "Mã nhân sự")
        self._fullName = _validate_non_empty_text(fullName, "Họ tên")
        self._baseSalary = _validate_number(baseSalary, "Lương cơ bản")
        self._lifecycleClosed = False

    @property
    def id(self) -> str:
        return self._id

    @property
    def fullName(self) -> str:
        return self._fullName

    @property
    def baseSalary(self) -> int | float:
        return self._baseSalary

    def increaseSalary(self, value: int | float, byPercentage: bool = False) -> None:
        """Tăng lương cố định hoặc theo phần trăm bằng một API Python."""
        amount = _validate_number(value, "Giá trị tăng lương", positive=True)
        if not isinstance(byPercentage, bool):
            raise TypeError("byPercentage phải là bool.")

        new_salary = (
            self._baseSalary * (1 + amount / 100)
            if byPercentage
            else self._baseSalary + amount
        )
        self._baseSalary = _validate_number(new_salary, "Lương sau khi tăng")

    def calculateMonthlyCost(self) -> int | float:
        return self._baseSalary

    def displayInfo(self) -> None:
        print(
            f"Nhân sự: {self._id} | {self._fullName} "
            f"| Lương cơ bản: {self._baseSalary:,.2f}"
        )

    def close(self) -> None:
        """In thông báo vòng đời tường minh, không phụ thuộc vào __del__."""
        if self._lifecycleClosed:
            return
        print(f"Kết thúc vòng đời Employee: {self._id}")
        self._lifecycleClosed = True