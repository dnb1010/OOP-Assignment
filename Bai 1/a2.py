number1 = int(input("Enter the first number: "))
number2 = int(input("Enter the second number: "))
number3 = int(input("Enter the third number: "))

max = number1
if number2 > max:
    max = number2
    if number3 > max:
        max = number3

print(f"The largest number is: {max}")