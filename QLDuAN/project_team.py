"""Nhóm dự án giữ các liên kết không sở hữu đến nhân sự."""

from weakref import ReferenceType, ref

from employee import Employee, _validate_non_empty_text


class ProjectTeam:
    """Nhóm dự án; bên ngoài nhóm chịu trách nhiệm giữ Employee tồn tại."""

    def __init__(
        self,
        projectCode: str,
        projectName: str,
        leader: Employee | None = None,
    ) -> None:
        self._projectCode = _validate_non_empty_text(projectCode, "Mã dự án")
        self._projectName = _validate_non_empty_text(projectName, "Tên dự án")
        if leader is not None and not isinstance(leader, Employee):
            raise TypeError("Trưởng nhóm phải là Employee.")

        self._members: dict[str, ReferenceType[Employee]] = {}
        self._leader_ref: ReferenceType[Employee] | None = None
        self._closed = False
        if leader is not None:
            self.addMember(leader, makeLeader=True)

    @property
    def projectCode(self) -> str:
        return self._projectCode

    @property
    def projectName(self) -> str:
        return self._projectName

    @property
    def leader(self) -> Employee | None:
        self._ensure_open()
        self._remove_expired_references()
        return self._leader_ref() if self._leader_ref is not None else None

    @property
    def members(self) -> tuple[Employee, ...]:
        self._ensure_open()
        self._remove_expired_references()
        live_members = []
        for member_ref in self._members.values():
            employee = member_ref()
            if employee is not None:
                live_members.append(employee)
        return tuple(live_members)

    def _ensure_open(self) -> None:
        if self._closed:
            raise RuntimeError("Nhóm dự án đã được đóng.")

    def _remove_expired_references(self) -> None:
        expired_ids = [
            employee_id
            for employee_id, employee_ref in self._members.items()
            if employee_ref() is None
        ]
        for employee_id in expired_ids:
            del self._members[employee_id]
        if self._leader_ref is not None and self._leader_ref() is None:
            self._leader_ref = None

    @staticmethod
    def _validated_employee(employee: Employee) -> tuple[str, ReferenceType[Employee]]:
        if not isinstance(employee, Employee):
            raise TypeError("Thành viên phải là Employee.")
        return employee.id, ref(employee)

    def addMember(self, employee: Employee, makeLeader: bool = False) -> bool:
        """Thêm thành viên; trả về True nếu danh sách được bổ sung."""
        employee_id, employee_ref = self._validated_employee(employee)
        if not isinstance(makeLeader, bool):
            raise TypeError("makeLeader phải là bool.")

        self._ensure_open()
        existing_ref = self._members.get(employee_id)
        existing_employee = existing_ref() if existing_ref is not None else None
        if existing_employee is not None and existing_employee is not employee:
            raise ValueError(f"Mã nhân sự {employee_id} đã thuộc về đối tượng khác.")
        self._remove_expired_references()
        existing_ref = self._members.get(employee_id)
        if existing_ref is not None:
            if existing_ref() is not employee:
                raise ValueError(f"Mã nhân sự {employee_id} đã thuộc về đối tượng khác.")
            if makeLeader:
                self._leader_ref = employee_ref
            return False

        self._members[employee_id] = employee_ref
        if makeLeader:
            self._leader_ref = employee_ref
        return True

    def removeMember(self, employeeId: str) -> bool:
        employee_id = _validate_non_empty_text(employeeId, "Mã nhân sự")
        self._ensure_open()
        current_leader = self._leader_ref() if self._leader_ref is not None else None
        if current_leader is not None and current_leader.id == employee_id:
            raise ValueError("Không thể xóa trưởng nhóm trước khi chọn người thay thế.")
        self._remove_expired_references()
        if employee_id not in self._members:
            return False
        del self._members[employee_id]
        return True

    def changeLeader(self, employee: Employee) -> None:
        employee_id, employee_ref = self._validated_employee(employee)
        self._ensure_open()
        existing_ref = self._members.get(employee_id)
        existing_employee = existing_ref() if existing_ref is not None else None
        if existing_employee is not None and existing_employee is not employee:
            raise ValueError(f"Mã nhân sự {employee_id} đã thuộc về đối tượng khác.")
        self._remove_expired_references()
        existing_ref = self._members.get(employee_id)
        if existing_ref is None:
            self._members[employee_id] = employee_ref
        self._leader_ref = employee_ref

    def contains(self, employeeId: str) -> bool:
        employee_id = _validate_non_empty_text(employeeId, "Mã nhân sự")
        self._ensure_open()
        self._remove_expired_references()
        return employee_id in self._members

    def calculateTotalMonthlyCost(self) -> int | float:
        self._ensure_open()
        self._remove_expired_references()
        # Danh sách tạm giữ tham chiếu mạnh trong suốt phép tính đa hình.
        live_members = [member_ref() for member_ref in self._members.values()]
        return sum(
            employee.calculateMonthlyCost()
            for employee in live_members
            if employee is not None
        )

    def displayTeam(self) -> None:
        self._ensure_open()
        self._remove_expired_references()
        leader = self._leader_ref() if self._leader_ref is not None else None
        print(f"Dự án {self._projectCode}: {self._projectName}")
        print(f"Trưởng nhóm: {leader.fullName if leader is not None else 'Chưa có'}")
        print("Thành viên:")
        for employee_ref in self._members.values():
            employee = employee_ref()
            if employee is not None:
                role = " [Trưởng nhóm]" if employee is leader else ""
                print(f"- {employee.id}{role}")
                employee.displayInfo()

    def close(self) -> None:
        """Đóng tất định cấu trúc nhóm mà không đóng các đối tượng nhân sự."""
        if self._closed:
            return
        self._members.clear()
        self._leader_ref = None
        self._closed = True
        print(f"Kết thúc vòng đời ProjectTeam: {self._projectCode}")

    def __enter__(self) -> "ProjectTeam":
        self._ensure_open()
        return self

    def __exit__(self, exc_type: object, exc_value: object, traceback: object) -> bool:
        self.close()
        return False