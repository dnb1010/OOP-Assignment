/*Đồng Ngọc Bảo 202418849 */
package lab07.exception;

public class NoSuitableItemException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NoSuitableItemException(){
        super("Khong co doi tuong phu hop");
    }

    public NoSuitableItemException(String message){
        super(message);
    }
}
