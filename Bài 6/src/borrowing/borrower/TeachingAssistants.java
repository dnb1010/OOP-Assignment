/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.borrower;

public class TeachingAssistants implements IBorrower{
    public static int MAX_DEVICES_PER_SLIP = 2;
    private final String borrowerId;
    private final String fullName;

    public TeachingAssistants(String borrowerId, String fullName){
        if(borrowerId == null || borrowerId.isBlank()){
            throw new IllegalArgumentException("Ma tro giang khong duoc de trong");
        }
        if (fullName == null || fullName.isBlank()){
            throw new IllegalArgumentException("Ho va ten tro giang khong duoc de trong");
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
        return "Tro giang " + fullName + " (" + borrowerId + ") ";
    }
}
