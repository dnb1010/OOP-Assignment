"""Kiểm thử bất biến cho nhân sự và nhóm dự án."""

import gc
import unittest
import weakref
from contextlib import redirect_stdout
from io import StringIO

from employee import Employee
from project_team import ProjectTeam
from software_engineer import SoftwareEngineer


class EmployeeTests(unittest.TestCase):
    def test_default_and_parameterized_constructors(self) -> None:
        default_employee = Employee()
        two_argument_employee = Employee("E-1", "An")
        full_employee = Employee("E-2", "Binh", 1000)

        self.assertEqual((default_employee.id, default_employee.fullName, default_employee.baseSalary),
                         ("UNKNOWN", "Unnamed employee", 0))
        self.assertEqual(two_argument_employee.calculateMonthlyCost(), 0)
        self.assertEqual(full_employee.calculateMonthlyCost(), 1000)

    def test_invalid_employee_data_is_rejected(self) -> None:
        with self.assertRaises(ValueError):
            Employee("  ", "An", 100)
        with self.assertRaises(ValueError):
            Employee("E-1", "  ", 100)
        with self.assertRaises(ValueError):
            Employee("E-1", "An", -1)

        employee = Employee("E-1", "An", 100)
        with self.assertRaises(AttributeError):
            employee.id = "E-2"  # type: ignore[misc]
        self.assertEqual(employee.id, "E-1")

    def test_salary_increase_fixed_and_percentage(self) -> None:
        employee = Employee("E-1", "An", 1000)
        employee.increaseSalary(200)
        self.assertEqual(employee.baseSalary, 1200)
        employee.increaseSalary(10, byPercentage=True)
        self.assertEqual(employee.baseSalary, 1320)

    def test_non_positive_raise_does_not_change_salary(self) -> None:
        employee = Employee("E-1", "An", 1000)
        for amount in (0, -1):
            with self.subTest(amount=amount), self.assertRaises(ValueError):
                employee.increaseSalary(amount)
        with self.assertRaises(TypeError):
            employee.increaseSalary(10, byPercentage=1)  # type: ignore[arg-type]
        self.assertEqual(employee.baseSalary, 1000)


class SoftwareEngineerTests(unittest.TestCase):
    def test_short_and_full_constructors_and_polymorphic_cost(self) -> None:
        short = SoftwareEngineer("SE-1", "Chi", "Python")
        full = SoftwareEngineer("SE-2", "Dung", "C++", 2000, 300)

        self.assertEqual(short.calculateMonthlyCost(), 0)
        self.assertEqual(full.calculateMonthlyCost(), 2300)
        self.assertEqual(full.primaryLanguage, "C++")

    def test_invalid_engineer_data_is_rejected(self) -> None:
        with self.assertRaises(ValueError):
            SoftwareEngineer("SE-1", "An", " ")
        with self.assertRaises(ValueError):
            SoftwareEngineer("SE-1", "An", "Python", technicalAllowance=-1)


class ProjectTeamTests(unittest.TestCase):
    def test_empty_team_has_no_leader_or_cost(self) -> None:
        team = ProjectTeam("P-1", "Dự án rỗng")
        self.assertIsNone(team.leader)
        self.assertEqual(team.members, ())
        self.assertEqual(team.calculateTotalMonthlyCost(), 0)

    def test_constructor_adds_initial_leader_once(self) -> None:
        leader = Employee("E-1", "An", 100)
        team = ProjectTeam("P-1", "Dự án", leader)
        self.assertIs(team.leader, leader)
        self.assertEqual(team.members, (leader,))

    def test_duplicate_same_object_can_be_promoted_without_duplication(self) -> None:
        employee = Employee("E-1", "An", 100)
        team = ProjectTeam("P-1", "Dự án")
        self.assertTrue(team.addMember(employee))
        self.assertFalse(team.addMember(employee, makeLeader=True))
        self.assertIs(team.leader, employee)
        self.assertEqual(team.members, (employee,))

    def test_different_object_with_same_id_is_rejected_transactionally(self) -> None:
        first = Employee("E-1", "An", 100)
        second = Employee("E-1", "Binh", 200)
        team = ProjectTeam("P-1", "Dự án", first)

        with self.assertRaises(ValueError):
            team.addMember(second, makeLeader=True)
        self.assertIs(team.leader, first)
        self.assertEqual(team.members, (first,))

        with self.assertRaises(ValueError):
            team.changeLeader(second)
        self.assertIs(team.leader, first)

    def test_cannot_remove_leader_until_replaced(self) -> None:
        former_leader = Employee("E-1", "An")
        new_leader = Employee("E-2", "Binh")
        team = ProjectTeam("P-1", "Dự án", former_leader)
        team.addMember(new_leader)

        with self.assertRaises(ValueError):
            team.removeMember(former_leader.id)
        self.assertEqual(team.members, (former_leader, new_leader))
        team.changeLeader(new_leader)
        self.assertTrue(team.removeMember(former_leader.id))
        self.assertIs(team.leader, new_leader)

    def test_change_leader_adds_new_person_and_keeps_old_leader(self) -> None:
        former_leader = Employee("E-1", "An")
        new_leader = Employee("E-2", "Binh")
        team = ProjectTeam("P-1", "Dự án", former_leader)

        team.changeLeader(new_leader)
        self.assertIs(team.leader, new_leader)
        self.assertEqual(team.members, (former_leader, new_leader))

    def test_invalid_input_does_not_change_team(self) -> None:
        leader = Employee("E-1", "An")
        team = ProjectTeam("P-1", "Dự án", leader)
        with self.assertRaises(TypeError):
            team.addMember(Employee("E-2", "Binh"), makeLeader=1)  # type: ignore[arg-type]
        self.assertIs(team.leader, leader)
        self.assertEqual(team.members, (leader,))

    def test_cost_is_polymorphic_and_leader_is_counted_once(self) -> None:
        employee = Employee("E-1", "An", 1000)
        engineer = SoftwareEngineer("SE-1", "Binh", "Python", 2000, 250)
        team = ProjectTeam("P-1", "Dự án", engineer)
        team.addMember(employee)

        self.assertEqual(engineer.calculateMonthlyCost(), 2250)
        self.assertEqual(team.calculateTotalMonthlyCost(), 3250)

    def test_employee_can_belong_to_multiple_teams_and_outlive_team(self) -> None:
        employee = Employee("E-1", "An", 100)
        first_team = ProjectTeam("P-1", "Một")
        first_team.addMember(employee)

        with ProjectTeam("P-2", "Hai") as second_team:
            second_team.addMember(employee, makeLeader=True)
            self.assertTrue(first_team.contains(employee.id))
            self.assertTrue(second_team.contains(employee.id))

        with self.assertRaises(RuntimeError):
            second_team.contains(employee.id)
        self.assertEqual(employee.id, "E-1")
        self.assertTrue(first_team.contains(employee.id))

    def test_expired_weak_references_are_removed_and_leader_cleared(self) -> None:
        team = ProjectTeam("P-1", "Dự án")
        employee = Employee("E-1", "An")
        employee_reference = weakref.ref(employee)
        team.addMember(employee, makeLeader=True)
        del employee
        gc.collect()

        self.assertIsNone(employee_reference())
        self.assertIsNone(team.leader)
        self.assertEqual(team.members, ())
        self.assertFalse(team.contains("E-1"))

    def test_display_handles_empty_team(self) -> None:
        output = StringIO()
        with redirect_stdout(output):
            ProjectTeam("P-1", "Dự án rỗng").displayTeam()
        self.assertIn("Chưa có", output.getvalue())

    def test_close_is_explicit_idempotent_and_does_not_close_employee(self) -> None:
        employee = Employee("E-1", "An")
        team = ProjectTeam("P-1", "Dự án", employee)
        output = StringIO()
        with redirect_stdout(output):
            team.close()
            team.close()
        self.assertEqual(output.getvalue().count("Kết thúc vòng đời ProjectTeam"), 1)
        self.assertEqual(employee.calculateMonthlyCost(), 0)
        with self.assertRaises(RuntimeError):
            team.contains(employee.id)


if __name__ == "__main__":
    unittest.main()