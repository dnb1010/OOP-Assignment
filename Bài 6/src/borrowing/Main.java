/*Đồng Ngọc Bảo - 202418849 */
package src.borrowing;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import src.borrowing.borrower.Lecturers;
import src.borrowing.borrower.TeachingAssistants;
import src.borrowing.exception.BorrowLimitExceededException;
import src.borrowing.exception.BorrowingException;
import src.borrowing.exception.DeviceAlreadyReturnedException;
import src.borrowing.exception.DeviceNotInSlipException;
import src.borrowing.exception.DeviceUnavailableException;
import src.borrowing.exception.DuplicateDeviceException;
import src.borrowing.exception.InvalidBorrowPeriodException;
import src.borrowing.exception.InvalidReturnDateException;
import src.borrowing.model.AirConRemote;
import src.borrowing.model.BorrowItem;
import src.borrowing.model.BorrowingSlip;
import src.borrowing.model.ClassRoom;
import src.borrowing.model.Device;
import src.borrowing.model.DeviceStatus;
import src.borrowing.model.Microphone;
import src.borrowing.model.ProjectorRemote;

public class Main {
    private static final  DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static int passed = 0;
    private static int failed = 0;

    @FunctionalInterface 
    private interface ThrowingAction{
        void run() throws Exception;
    }
    private static String money(BigDecimal value){
        return String.format(Locale.US, "%,.0f đ", value);
    }
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        try {
            runSuccessScenario();        // 11.1
            runExceptionScenarios();     // 11.2
            runClassRoomScenario();      // Kiểm tra ClassRoom + đa hình
        } catch (Exception ex) {
            System.out.println("\nLỗi không mong đợi: " + ex);
            ex.printStackTrace(System.out);
        } finally {
            System.out.println("\n=== Kết quả kiểm thử ngoại lệ: " + passed + " đạt, " + failed + " không đạt ===");
            System.out.println("Kết thúc chương trình.");
        }
    }

    /*Luong thanh cong */
    private static void runSuccessScenario() throws BorrowingException{
        System.out.println("===LUỒNG THÀNH CÔNG===");
        int year = Year.now().getValue();
        LocalDate today = LocalDate.now();

        /*1. Tạo một giảng viên */
        Lecturers lecturer = new Lecturers("GV001", "Vũ Thành Nam");

        /*2. Tạo một điều khiển máy chiếu, một điều khiển điều hòa và một micro không dây */
        ProjectorRemote projector = new ProjectorRemote("PJ01", "Điều khiển máy chiếu Epson", year - 3, new BigDecimal("2000000"));
        AirConRemote airCon = new AirConRemote("AC01", "Điều khiển điều hòa Panasonic", year - 1, new BigDecimal("1200000"));
        Microphone mic = new Microphone("MIC01", "Micro không dây Shure", year - 2, new BigDecimal("3500000"));

        /*3. Phiếu mượn cho một phòng dạy*/
        BorrowingSlip slip = new BorrowingSlip("S001", lecturer, "A101", today);
        System.out.println(slip);

        /*4. Thêm điều khiể máy chiều (3 ngày) và micro (1 ngày)*/
        slip.addDevice(projector, 3);
        slip.addDevice(mic, 1);
        System.out.println("Đã thêm 2 thiết bị. Trạng thái: " + projector.getDeviceId() + "=" + projector.getDeviceStatus() + ", " + mic.getDeviceId() + "=" + mic.getDeviceStatus());

        /*5. Trả điều khiển máy chiếu đúng hạn*/
        BigDecimal fee1 = slip.returnDevice("PJ01", today.plusDays(3));
        System.out.println("Trả " +  projector.getDeviceId() + " đúng hạn -> phí trả muộn: " + money(fee1) + ", trạng thài: " + projector.getDeviceStatus());

        /*6. Trả micro muốn 2 ngày (hạn = ngày mượn + 1 -> trả ngày mượng + 3*/
        BigDecimal fee2 = slip.returnDevice("MIC01", today.plusDays(3));
        System.out.println("Trả" + mic.getDeviceId() + " muộn 2 ngày -> phí trả muộn: " + money(fee2) + ", trạng thái: " + mic.getDeviceStatus());
        System.out.println("Trả " + mic.getDeviceId() + "muộn 2 ngày -> phí trả muộn: " + money(slip.calculateTotalLateFee()));
        
        BorrowingSlip slip2 = new BorrowingSlip("S002", lecturer, "B203", today);
        slip2.addDevice(airCon, 2);

        /*7. Hiển thị các phiếu trả muộn, chưa trả */
        printReport(List.of(slip, slip2));
    }

    private static void printReport(List<BorrowingSlip> slips){
        System.out.println("\n---Báo cáo các phiếu trả muộn / chưa trả ---");
        for (BorrowingSlip slip : slips){
            System.out.println(slip);
            List<BorrowItem> late = slip.getLateReturnedItems();
            List<BorrowItem> unreturned = slip.getUnreturnedItems();

            System.out.println(" Thiêt bị trả muộn: ");
            if (late.isEmpty()){
                System.out.println("    (Không có)");
            }
            for (BorrowItem item : late){
                System.out.println("    " + item + " | muộn" + item.getLateDays() + " ngày | phí " + money(item.calculateLateFee()));
            } 
            System.out.println(" Thiết bị chưa trả");
            if (unreturned.isEmpty()){
                System.out.println("    (Không có)");
            }
            for (BorrowItem item : unreturned){
                System.out.println("    " + item);
            }
        }
    }
private static void runExceptionScenarios() throws BorrowingException {
        System.out.println("\n=== 11.2. LUỒNG PHÁT SINH NGOẠI LỆ ===");
        int year = Year.now().getValue();
        LocalDate today = LocalDate.now();

        // 1. Mượn điều khiển điều hòa đang UNDER_MAINTENANCE.
        {
            BorrowingSlip slip = new BorrowingSlip("S101", new Lecturers("GV001", "Nguyễn Văn An"), "A101", today);
            AirConRemote airCon = new AirConRemote("AC02", "Điều khiển điều hòa LG", year - 1, new BigDecimal("1500000"));
            airCon.sendToMaintenance();

            expect(DeviceUnavailableException.class,
                    "1. Mượn điều khiển điều hòa đang UNDER_MAINTENANCE",
                    () -> slip.addDevice(airCon, 1),
                    () -> airCon.getDeviceStatus() == DeviceStatus.Undermaintenance && slip.getItems().isEmpty());
        }

        // 2. Trợ giảng mượn thiết bị thứ ba.
        {
            BorrowingSlip slip = new BorrowingSlip("S102", new TeachingAssistants("TG001", "Trần Thị Bình"), "A102", today);
            ProjectorRemote projector = new ProjectorRemote("PJ02", "Điều khiển máy chiếu Sony", year - 2, new BigDecimal("2200000"));
            Microphone mic = new Microphone("MIC02", "Micro không dây Sennheiser", year - 2, new BigDecimal("3000000"));
            AirConRemote airCon = new AirConRemote("AC03", "Điều khiển điều hòa Panasonic", year - 1, new BigDecimal("1300000"));
            slip.addDevice(projector, 3);
            slip.addDevice(mic, 1);

            expect(BorrowLimitExceededException.class,
                    "2. Trợ giảng mượn thiết bị thứ ba",
                    () -> slip.addDevice(airCon, 1),
                    () -> airCon.getDeviceStatus() == DeviceStatus.Available && slip.getItems().size() == 2);
        }

        // 3. Thêm cùng một micro hai lần.
        {
            BorrowingSlip slip = new BorrowingSlip("S103", new Lecturers("GV002", "Lê Văn Cường"), "A103", today);
            Microphone mic = new Microphone("MIC03", "Micro không dây Shure", year - 2, new BigDecimal("3500000"));
            slip.addDevice(mic, 1);

            expect(DuplicateDeviceException.class,
                    "3. Thêm cùng một micro hai lần",
                    () -> slip.addDevice(mic, 1),
                    () -> mic.getDeviceStatus() == DeviceStatus.Borrowed && slip.getItems().size() == 1);
        }

        // 4. Mượn micro 3 ngày dù giới hạn là 1 ngày.
        {
            BorrowingSlip slip = new BorrowingSlip("S104", new Lecturers("GV002", "Lê Văn Cường"), "A103", today);
            Microphone mic = new Microphone("MIC04", "Micro không dây Shure", year - 2, new BigDecimal("3500000"));

            expect(InvalidBorrowPeriodException.class,
                    "4. Mượn micro 3 ngày dù giới hạn là 1 ngày",
                    () -> slip.addDevice(mic, 3),
                    () -> mic.getDeviceStatus() == DeviceStatus.Available && slip.getItems().isEmpty());
        }

        // 5. Trả một điều khiển máy chiếu không thuộc phiếu.
        {
            BorrowingSlip slip = new BorrowingSlip("S105", new Lecturers("GV003", "Phạm Thị Dung"), "A104", today);
            ProjectorRemote inSlip = new ProjectorRemote("PJ03", "Điều khiển máy chiếu Epson", year - 2, new BigDecimal("2000000"));
            ProjectorRemote outside = new ProjectorRemote("PJ99", "Điều khiển máy chiếu BenQ", year - 2, new BigDecimal("2000000"));
            slip.addDevice(inSlip, 2);

            expect(DeviceNotInSlipException.class,
                    "5. Trả điều khiển máy chiếu không thuộc phiếu",
                    () -> slip.returnDevice(outside.getDeviceId(), today.plusDays(1)),
                    () -> inSlip.getDeviceStatus() == DeviceStatus.Borrowed
                            && outside.getDeviceStatus() == DeviceStatus.Available
                            && !slip.getItems().get(0).isReturned());
        }

        // 6. Trả cùng một thiết bị hai lần.
        {
            BorrowingSlip slip = new BorrowingSlip("S106", new Lecturers("GV003", "Phạm Thị Dung"), "A104", today);
            ProjectorRemote projector = new ProjectorRemote("PJ04", "Điều khiển máy chiếu Epson", year - 2, new BigDecimal("2000000"));
            slip.addDevice(projector, 2);
            LocalDate firstReturn = today.plusDays(1);
            slip.returnDevice("PJ04", firstReturn);

            expect(DeviceAlreadyReturnedException.class,
                    "6. Trả cùng một thiết bị hai lần",
                    () -> slip.returnDevice("PJ04", today.plusDays(2)),
                    () -> projector.getDeviceStatus() == DeviceStatus.Available
                            && firstReturn.equals(slip.getItems().get(0).getReturnedDate()));
        }

        // 7. Trả thiết bị với ngày trả trước ngày mượn.
        {
            BorrowingSlip slip = new BorrowingSlip("S107", new Lecturers("GV004", "Hoàng Văn Em"), "A105", today);
            ProjectorRemote projector = new ProjectorRemote("PJ05", "Điều khiển máy chiếu Epson", year - 2, new BigDecimal("2000000"));
            slip.addDevice(projector, 2);

            expect(InvalidReturnDateException.class,
                    "7. Trả thiết bị với ngày trả trước ngày mượn",
                    () -> slip.returnDevice("PJ05", today.minusDays(1)),
                    () -> projector.getDeviceStatus() == DeviceStatus.Borrowed && !slip.getItems().get(0).isReturned());
        }

        // Bổ sung: số ngày mượn không dương.
        {
            BorrowingSlip slip = new BorrowingSlip("S108", new Lecturers("GV004", "Hoàng Văn Em"), "A105", today);
            AirConRemote airCon = new AirConRemote("AC04", "Điều khiển điều hòa Daikin", year - 1, new BigDecimal("1200000"));

            expect(InvalidBorrowPeriodException.class,
                    "8. (Bổ sung) Số ngày mượn bằng 0",
                    () -> slip.addDevice(airCon, 0),
                    () -> airCon.getDeviceStatus() == DeviceStatus.Available && slip.getItems().isEmpty());
        }
    }

    /**
     * Chạy một thao tác, bắt ngoại lệ ở tầng ứng dụng, rồi kiểm tra
     * đúng loại ngoại lệ và trạng thái vẫn nhất quán sau khi thất bại.
     */
    private static void expect(Class<? extends BorrowingException> expectedType, String title,
                               ThrowingAction action, BooleanSupplier isConsistent) {
        System.out.println("\n[" + title + "]");
        BorrowingException caught = null;

        try {
            action.run();
            System.out.println("  !!! Không có ngoại lệ nào được ném.");
        } catch (DeviceUnavailableException ex) {
            caught = ex;
            System.out.println("  Bắt DeviceUnavailableException: " + ex.getMessage()
                    + " [DeviceId=" + ex.getDeviceId() + ", CurrentStatus=" + ex.getCurrentStatus() + "]");
        } catch (BorrowLimitExceededException ex) {
            caught = ex;
            System.out.println("  Bắt BorrowLimitExceededException: " + ex.getMessage()
                    + " [BorrowerId=" + ex.getBorrowerId() + ", MaximumDevices=" + ex.getMaximumDevices() + "]");
        } catch (InvalidBorrowPeriodException ex) {
            caught = ex;
            System.out.println("  Bắt InvalidBorrowPeriodException: " + ex.getMessage()
                    + " [DeviceId=" + ex.getDeviceId() + ", RequestedDays=" + ex.getRequestedDays()
                    + ", MaximumDays=" + ex.getMaximumDays() + "]");
        } catch (DuplicateDeviceException ex) {
            caught = ex;
            System.out.println("  Bắt DuplicateDeviceException: " + ex.getMessage());
        } catch (DeviceNotInSlipException ex) {
            caught = ex;
            System.out.println("  Bắt DeviceNotInSlipException: " + ex.getMessage());
        } catch (DeviceAlreadyReturnedException ex) {
            caught = ex;
            System.out.println("  Bắt DeviceAlreadyReturnedException: " + ex.getMessage());
        } catch (InvalidReturnDateException ex) {
            caught = ex;
            System.out.println("  Bắt InvalidReturnDateException: " + ex.getMessage());
        } catch (BorrowingException ex) {
            caught = ex;
            System.out.println("  Bắt BorrowingException: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("  Lỗi hệ thống (" + ex.getClass().getSimpleName() + "): " + ex.getMessage());
        } finally {
            System.out.println("  (finally) Kết thúc xử lý tình huống.");
        }

        boolean rightType = expectedType.isInstance(caught);
        boolean consistent = isConsistent.getAsBoolean();
        System.out.println("  Đúng loại ngoại lệ (" + expectedType.getSimpleName() + "): " + (rightType ? "CÓ" : "KHÔNG"));
        System.out.println("  Trạng thái nhất quán sau lỗi: " + (consistent ? "CÓ" : "KHÔNG"));

        if (rightType && consistent) passed++; else failed++;
    }

    // ------------------------------------------------------------------
    // Kiểm tra ClassRoom, đa hình và quy tắc thêm thiết bị
    // ------------------------------------------------------------------
    private static void runClassRoomScenario() throws BorrowingException {
        System.out.println("\n=== KIỂM TRA CLASSROOM ===");
        int year = Year.now().getValue();

        ClassRoom room = new ClassRoom("R01", "Phòng A101", 60);
        AirConRemote airCon = new AirConRemote("AC10", "Điều khiển điều hòa Daikin", year - 1, new BigDecimal("1200000"));
        ProjectorRemote projector = new ProjectorRemote("PJ10", "Điều khiển máy chiếu Epson", year - 3, new BigDecimal("2000000"));
        Microphone oldMic = new Microphone("MIC10", "Micro không dây cũ", year - 6, new BigDecimal("3000000")); // dùng trên 5 năm
        AirConRemote brokenAc = new AirConRemote("AC11", "Điều khiển điều hòa hỏng", year - 1, new BigDecimal("1000000"));
        brokenAc.sendToMaintenance(); // đang bảo trì

        for (Device d : new Device[]{airCon, projector, oldMic, brokenAc}) {
            room.addDevice(d);
        }

        System.out.println(room);
        for (Device d : room.getDevices()) {
            System.out.println("  " + d + " | bảo trì/năm: " + money(d.calculateAnnualMaintenanceCost()));
        }

        System.out.println("Tổng chi phí bảo trì dự kiến/năm: " + money(room.calculateAnnualMaintenanceCost()));

        System.out.println("Thiết bị cần bảo trì (UNDER_MAINTENANCE hoặc dùng trên 5 năm):");
        for (Device d : room.getDevicesRequiringMaintenance()) {
            System.out.println("  " + d.getDeviceId() + " - " + d.getDeviceName());
        }

        try {
            room.addDevice(airCon);
        } catch (DuplicateDeviceException ex) {
            System.out.println("Thêm trùng mã bị từ chối: " + ex.getMessage());
        }

        try {
            room.addDevice(null);
        } catch (IllegalArgumentException ex) {
            System.out.println("Thêm thiết bị null bị từ chối: " + ex.getMessage());
        }

        Device found = room.findDevice("PJ10");
        System.out.println("findDevice(\"PJ10\") -> " + (found != null ? found.getDeviceName() : "không thấy"));
        boolean first = room.removeDevice("PJ10");
        boolean second = room.removeDevice("PJ10");
        System.out.println("removeDevice(\"PJ10\") -> " + first + "; removeDevice(\"PJ10\") lần nữa -> " + second);
        System.out.println("Thiết bị vẫn tồn tại độc lập: " + projector.getDeviceName() + " (" + projector.getDeviceStatus() + ")");
    }
}
