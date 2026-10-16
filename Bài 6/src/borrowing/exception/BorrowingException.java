/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing.exception;
public class BorrowingException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public BorrowingException(String message){
        super(message);
    }

    public BorrowingException(String message, Throwable cause){
        super(message, cause);
    }
}
