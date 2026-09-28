from employee import Employee, check_number, check_text


class SoftwareEngineer(Employee):
    def __init__(
        self,
        employeeId,
        fullName,
        primaryLanguage: str,
        baseSalary=0,
        technicalAllowance: int | float = 0,
    ):
        super().__init__(employeeId, fullName, baseSalary)
        self._primaryLanguage = check_text(primaryLanguage, "Ngôn ngữ chính")
        self._technicalAllowance = check_number(technicalAllowance, "Phụ cấp kĩ thuật")

    @property
    def primaryLanguage(self) -> str:
        return self._primaryLanguage

    @property
    def technicalAllowance(self) -> int | float:
        return self._technicalAllowance

    def calculateMonthlyCost(self):
        return self.baseSalary + self.technicalAllowance

    def calculateMontlyCost(self):
        return self.calculateMonthlyCost()

    def displayInfo(self) -> None:
        print(
            f"Kỹ sư phần mền: {self.employeeId} | {self.fullName}"
            f"| Ngôn ngữ: {self.primaryLanguage}"
            f"| Lương cơ bản: {self.baseSalary}"
            f"| Phụ cấp: {self.technicalAllowance}"
            f"| Chi phí: {self.calculateMonthlyCost():,.0f}"
        )

    def __del__(self):
        employee_id = getattr(self, "_Employee__employeeId", "?")
        print(f"[Hủy SoftwareEngineer] {employee_id}")

        super().__del__()