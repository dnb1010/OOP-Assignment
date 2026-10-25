/*Đồng Ngọc Bảo 202418849 */
package lab07.exception;

public class DuplicateIdException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String duplicateId;

    public DuplicateIdException(String duplicateId){
        super("Mã thiết bị bị trùng: " + duplicateId);
        this.duplicateId = duplicateId;
    }

    public String getDuplicateId(){
        return duplicateId;
    }

}
