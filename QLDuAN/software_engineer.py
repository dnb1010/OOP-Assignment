"""Nhân sự kỹ sư phần mềm, kế thừa Employee."""

from employee import Employee, _validate_non_empty_text, _validate_number


class SoftwareEngineer(Employee):
    """Nhân sự có ngôn ngữ chính và phụ cấp kỹ thuật."""

    def __init__(
        self,
        id: str,
        fullName: str,
        primaryLanguage: str,
        baseSalary: int | float = 0,
        technicalAllowance: int | float = 0,
    ) -> None:
        super().__init__(id, fullName, baseSalary)
        self._primaryLanguage = _validate_non_empty_text(
            primaryLanguage, "Ngôn ngữ chính"
        )
        self._technicalAllowance = _validate_number(
            technicalAllowance, "Phụ cấp kỹ thuật"
        )

    @property
    def primaryLanguage(self) -> str:
        return self._primaryLanguage

    @property
    def technicalAllowance(self) -> int | float:
        return self._technicalAllowance

    def calculateMonthlyCost(self) -> int | float:
        return self.baseSalary + self._technicalAllowance

    def displayInfo(self) -> None:
        print(
            f"Kỹ sư phần mềm: {self.id} | {self.fullName} "
            f"| Ngôn ngữ: {self._primaryLanguage} "
            f"| Lương cơ bản: {self.baseSalary:,.2f} "
            f"| Phụ cấp: {self._technicalAllowance:,.2f}"
        )

    def close(self) -> None:
        if self._lifecycleClosed:
            return
        print(f"Kết thúc vòng đời SoftwareEngineer: {self.id}")
        super().close()