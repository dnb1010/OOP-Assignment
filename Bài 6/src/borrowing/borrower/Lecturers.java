/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.borrower;

public class Lecturers implements IBorrower{
    public static final int MAX_DEVICES_PER_SLIP = 3;

    private final String borrowerId;
    private final String fullName;

    public Lecturers(String borrowerId, String fullName){
        if (borrowerId == null || borrowerId.isBlank()){
            throw new IllegalArgumentException("Ma giang vien khong duoc de rong");
        }
        if (fullName == null || fullName.isBlank()){
            throw new IllegalArgumentException("Ho ten giang vien khong duoc de rong");
        }
        this.borrowerId = borrowerId;
        this.fullName = fullName;
    }
    @Override 
    public String getBorrowerId(){
        return borrowerId;
    }
    @Override 
    public String getFullName(){
        return fullName;
    }
    @Override 
    public int getMaximumDevices(){
        return MAX_DEVICES_PER_SLIP;
    }
    @Override 
    public String toString(){
        return "Giang vien " + fullName + " (" + borrowerId + ")"; 
    }
}
