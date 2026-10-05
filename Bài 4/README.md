# Payroll Assignment in Java

This project implements the payroll system described in the lab document for three employee types:

- SalariedEmployee
- HourlyEmployee
- SalesEmployee
- Payroll manager

## Compile

```bash
cd "Bài 4"
javac -d out src/payroll/*.java
```

## Run

```bash
java -cp out payroll.PayrollDemo
```

## Design notes

- `Employee` is declared as an abstract class because each employee type shares common data and behavior but computes gross pay differently.
- `addBonus()` is overloaded to handle fixed bonuses, bonus with a reason, and rate-based bonuses.
- `Payroll` stores `Employee` references so dynamic dispatch preserves the derived class behavior when `calculateGrossPay()` is invoked.
