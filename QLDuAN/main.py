"""Chạy tuần tự 15 bước kiểm thử trong đề bài."""

from employee import Employee
from project_team import ProjectTeam
from software_engineer import SoftwareEngineer


def main() -> None:
    print("=== 1. Tạo hai Employee bằng các dạng constructor ===")
    employee_one = Employee("E001", "Nguyen An")
    employee_two = Employee("E002", "Tran Binh", 1200)

    print("=== 2. Tạo hai SoftwareEngineer bằng các dạng constructor ===")
    engineer_one = SoftwareEngineer("SE001", "Le Chi", "Python", 2000, 300)
    engineer_two = SoftwareEngineer("SE002", "Pham Dung", "C++")

    print("=== 3. Tăng lương cố định ===")
    employee_one.increaseSalary(50)
    print(f"Lương mới của {employee_one.id}: {employee_one.baseSalary:,.2f}")

    print("=== 4. Tăng lương theo phần trăm ===")
    employee_two.increaseSalary(5, byPercentage=True)
    print(f"Lương mới của {employee_two.id}: {employee_two.baseSalary:,.2f}")

    print("=== 5. Tạo nhóm chưa có trưởng nhóm ===")
    first_team = ProjectTeam("P001", "He thong quan ly")
    print(f"Trưởng nhóm ban đầu: {first_team.leader}")

    print("=== 6. Thêm nhân sự thông thường ===")
    assert first_team.addMember(employee_one)

    print("=== 7. Thêm kỹ sư và đặt làm trưởng nhóm ===")
    assert first_team.addMember(engineer_one, makeLeader=True)
    assert first_team.leader is engineer_one

    print("=== 8. Thử thêm lại thành viên đã có ===")
    assert not first_team.addMember(engineer_one)
    print("Từ chối thêm trùng: OK")

    print("=== 9. Hiển thị thành viên bằng lời gọi đa hình ===")
    first_team.displayTeam()

    print("=== 10. Tính tổng chi phí hằng tháng ===")
    print(f"Tổng chi phí: {first_team.calculateTotalMonthlyCost():,.2f}")

    print("=== 11. Thử xóa trưởng nhóm hiện tại ===")
    try:
        first_team.removeMember(engineer_one.id)
    except ValueError as error:
        print(f"Thao tác bị từ chối: {error}")
    else:
        raise AssertionError("Không được phép xóa trưởng nhóm hiện tại.")

    print("=== 12. Đổi trưởng nhóm rồi xóa người từng giữ vai trò ===")
    first_team.changeLeader(employee_one)
    assert first_team.removeMember(engineer_one.id)
    print(f"Trưởng nhóm mới: {first_team.leader.fullName}")

    print("=== 13. Dùng lại nhân sự trong nhóm thứ hai ===")
    with ProjectTeam("P002", "Ung dung di dong") as second_team:
        assert second_team.addMember(employee_one, makeLeader=True)
        assert first_team.contains(employee_one.id)
        assert second_team.contains(employee_one.id)
        print("Cùng một nhân sự thuộc cả hai nhóm: OK")

        print("=== 14. Kết thúc khối with để đóng nhóm thứ hai ===")

    print("=== 15. Xác nhận nhóm đóng không hủy nhân sự ===")
    assert employee_one.id == "E001"
    assert first_team.contains(employee_one.id)
    employee_one.displayInfo()
    print("Nhân sự vẫn tồn tại sau khi nhóm thứ hai đóng: OK")

    first_team.close()
    employee_one.close()
    employee_two.close()
    engineer_one.close()
    engineer_two.close()


if __name__ == "__main__":
    main()