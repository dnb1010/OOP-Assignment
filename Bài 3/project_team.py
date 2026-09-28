import weakref

from employee import Employee, check_text


class ProjectTeam:
    def __init__(
        self,
        projectCode: str,
        projectName: str,
        leader: Employee | None = None,
    ) -> None:
        self.__projectCode = check_text(projectCode, "Mã dự án")
        self.__projectName = check_text(projectName, "Tên dự án")

        self.__members = {}
        self.__leaderId = None
        if leader is not None:
            self.addMember(leader, True)

    def _getMember(self, employeeId):
        reference = self.__members.get(employeeId)
        if reference is None:
            return None

        employee = reference()
        if employee is None:
            self.__members.pop(employeeId, None)
            return None

        return employee

    @property
    def leader(self):
        if self.__leaderId is None:
            return None
        return self._getMember(self.__leaderId)

    @property
    def members(self):
        return tuple(
            self._getMember(employeeId)
            for employeeId in list(self.__members.keys())
        )

    def contains(self, employeeId):
        return self._getMember(employeeId) is not None

    def addMember(self, employee, makeLeader=False):
        if not isinstance(employee, Employee):
            raise TypeError("Thành viên phải là một Employee")
        if not isinstance(makeLeader, bool):
            raise TypeError("makeLeader phải là True hoặc False")

        existing = self._getMember(employee.employeeId)
        if existing is not None:
            if existing is not employee:
                return False
            if makeLeader:
                self.__leaderId = employee.employeeId
                return True
            return False

        self.__members[employee.employeeId] = weakref.ref(employee)
        if makeLeader:
            self.__leaderId = employee.employeeId
        return True

    def removeMember(self, employeeId):
        if not self.contains(employeeId):
            return False

        if employeeId == self.__leaderId:
            return False

        self.__members.pop(employeeId, None)
        return True

    def changeLeader(self, employee):
        existing = self._getMember(employee.employeeId)
        if existing is not None and existing is not employee:
            return False

        if existing is None:
            self.addMember(employee)

        self.__leaderId = employee.employeeId
        return True

    def calculateTotalMonthlyCost(self):
        return sum(
            employee.calculateMonthlyCost()
            for employee in self.members
        )

    def displayTeam(self):
        print(f"\nDự án: {self.__projectCode} - {self.__projectName}")

        leader = self.leader
        if leader is None:
            print("Trưởng nhóm: Chưa có")
        else:
            print(f"Trưởng nhóm: {leader.fullName} ({leader.id})")

        print("Danh sách nhân sự:")

        for employee in self.members:
            employee.displayInfo()

        print(
            "Tổng chi phí hằng tháng: "
            f"{self.calculateTotalMonthlyCost():,.0f}"
        )

    def __del__(self):
        project_code = getattr(self, "_ProjectTeam__projectCode", "?")

        members = getattr(self, "_ProjectTeam__members", None)
        if members is not None:
            members.clear()

        print(f"[Hủy ProjectTeam] {project_code}")