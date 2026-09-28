from employee import Employee
from project_team import ProjectTeam
from software_engineer import SoftwareEngineer
import math
def demo():
    # 1. Hai Employee bằng hai cách khởi tạo khác nhau.
    e1 = Employee("E01", "Bảo")
    e2 = Employee("E02", "An", 10_000_000)

    # 2. Hai SoftwareEngineer bằng hai cách khởi tạo.
    s1 = SoftwareEngineer("S01", "Linh", "C++")

    s2 = SoftwareEngineer(
        "S02", "Minh", "Python",
        baseSalary=15_000_000,
        technicalAllowance=2_000_000
    )

    # 3. Tăng lương cố định.
    e1.increaseSalary(1_000_000)
    assert e1.baseSalary == 1_000_000

    # 4. Tăng lương theo phần trăm.
    e2.increaseSalary(10, True)
    assert math.isclose(e2.baseSalary, 11_000_000)

    # 5. Tạo nhóm chưa có trưởng nhóm.
    team1 = ProjectTeam("P01", "Hệ thống quản lý nhân sự")
    assert team1.leader is None

    # 6. Thêm nhân sự.
    assert team1.addMember(e1)
    assert team1.addMember(e2)
    assert team1.addMember(s1)

    # 7. Thêm kỹ sư và đặt làm trưởng nhóm.
    assert team1.addMember(s2, True)
    assert team1.leader is s2

    # 8. Thử thêm trùng.
    result = team1.addMember(e1)
    print("Thêm lại E01:", result)
    assert result is False
    assert len(team1.members) == 4

    # 9. Hiển thị bằng lời gọi đa hình.
    team1.displayTeam()

    # 10. Tính tổng chi phí.
    assert math.isclose(
        team1.calculateTotalMonthlyCost(),
        29_000_000
    )

    # 11. Không được xóa trưởng nhóm.
    result = team1.removeMember("S02")
    print("\nXóa trưởng nhóm S02:", result)
    assert result is False

    # 12. Đổi trưởng nhóm rồi xóa trưởng nhóm cũ.
    assert team1.changeLeader(e2)
    assert team1.contains("S02")  # Người cũ vẫn còn trong nhóm.
    assert team1.removeMember("S02")

    assert team1.leader is e2
    assert math.isclose(
        team1.calculateTotalMonthlyCost(),
        12_000_000
    )

    team1.displayTeam()

    # 13–14. Python không có phạm vi biến riêng cho khối if/for.
    # Dùng hàm để tạo phạm vi cục bộ cho nhóm thứ hai.
    def second_team_demo():
        team2 = ProjectTeam("P02", "Dự án AI", e1)

        assert team1.contains("E01")
        assert team2.contains("E01")
        assert team2.leader is e1

        team2.displayTeam()
        print("\nKết thúc phạm vi của nhóm thứ hai.")

    second_team_demo()

    # 15. Nhân sự vẫn tồn tại sau khi nhóm thứ hai hết phạm vi.
    print("\nE01 vẫn tồn tại:")
    e1.displayInfo()
    assert team1.contains("E01")

    # Kiểm thử các trường hợp biên.
    empty_team = ProjectTeam("P03", "Nhóm rỗng")
    assert empty_team.calculateTotalMonthlyCost() == 0
    assert empty_team.removeMember("NOT_FOUND") is False

    # Người chưa thuộc nhóm được tự thêm khi đổi trưởng nhóm.
    assert empty_team.changeLeader(s1)
    assert empty_team.contains("S01")
    assert empty_team.leader is s1

    # Đối tượng khác nhưng cùng mã bị từ chối.
    duplicate = Employee("E01", "Người khác")
    assert team1.addMember(duplicate) is False
    assert team1.changeLeader(duplicate) is False

    invalid_cases = [
        ("Mã rỗng", lambda: Employee("", "A")),
        ("Tên rỗng", lambda: Employee("X01", " ")),
        ("Lương âm", lambda: Employee("X02", "A", -1)),
        ("Mức tăng bằng 0", lambda: e1.increaseSalary(0)),
        ("Mức tăng âm", lambda: e1.increaseSalary(-100)),
        (
            "Ngôn ngữ rỗng",
            lambda: SoftwareEngineer("X03", "A", "")
        ),
        (
            "Phụ cấp âm",
            lambda: SoftwareEngineer(
                "X04", "A", "Python", technicalAllowance=-1
            )
        ),
    ]

    for label, operation in invalid_cases:
        try:
            operation()
        except ValueError as error:
            print(f"[Đúng: {label}] {error}")
        else:
            raise AssertionError(f"Chưa chặn trường hợp: {label}")

    print("\nTất cả kiểm thử đã vượt qua.")

    # Bỏ các nhóm trước khi kết thúc phạm vi chứa nhân sự.
    del empty_team
    del team1


def read_number(prompt):
    while True:
        try:
            value = float(input(prompt))
            if value < 0:
                print("Vui lòng nhập số không âm.")
                continue
            return value
        except ValueError:
            print("Vui lòng nhập một số hợp lệ.")


def main():
    project_code = input("Mã dự án: ").strip()
    project_name = input("Tên dự án: ").strip()
    team = ProjectTeam(project_code, project_name)

    while True:
        try:
            member_count = int(input("Số lượng nhân sự: "))
            if member_count < 0:
                print("Số lượng nhân sự không được âm.")
                continue
            break
        except ValueError:
            print("Vui lòng nhập số nguyên hợp lệ.")

    members = []
    for index in range(member_count):
        print(f"\nNhập thông tin nhân sự thứ {index + 1}:")
        while True:
            member_type = input("Loại (1: Nhân viên, 2: Kỹ sư phần mềm): ").strip()
            if member_type in ("1", "2"):
                break
            print("Vui lòng chọn 1 hoặc 2.")

        employee_id = input("Mã nhân sự: ").strip()
        full_name = input("Họ và tên: ").strip()
        base_salary = read_number("Lương cơ bản: ")

        if member_type == "2":
            language = input("Ngôn ngữ lập trình chính: ").strip()
            allowance = read_number("Phụ cấp kỹ thuật: ")
            employee = SoftwareEngineer(
                employee_id, full_name, language, base_salary, allowance
            )
        else:
            employee = Employee(employee_id, full_name, base_salary)

        if team.addMember(employee):
            members.append(employee)
        else:
            print("Mã nhân sự đã tồn tại; nhân sự này không được thêm.")

    if members:
        leader_id = input("Nhập mã trưởng nhóm (Enter để bỏ qua): ").strip()
        if leader_id:
            leader = next(
                (member for member in members if member.employeeId == leader_id),
                None,
            )
            if leader is None:
                print("Không tìm thấy mã nhân sự đó; nhóm chưa có trưởng nhóm.")
            else:
                team.changeLeader(leader)

    team.displayTeam()


if __name__ == "__main__":
    demo()