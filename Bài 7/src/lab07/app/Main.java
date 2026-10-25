package lab07.app;

import lab07.exception.DeviceNotReadyException;
import lab07.exception.DuplicateIdException;
import lab07.exception.NoSuitableItemException;
import lab07.model.*;
import lab07.repository.IRepository;
import lab07.repository.InMemoryRepository;
import lab07.service.AllocationService;
import lab07.strategy.FirstAvailableStrategy;
import lab07.strategy.HighestPriorityStrategy;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

/** Chương trình kiểm thử theo mục 9 của đề (không cần thư viện ngoài). */
public class Main {
    private static int passed = 0;
    private static int failed = 0;

    // ---------- dữ liệu mẫu: mỗi lần gọi tạo kho mới để các test độc lập ----------
    static InMemoryRepository<ProjectorRemote> projectorRepo() {
        InMemoryRepository<ProjectorRemote> repo = new InMemoryRepository<>();
        repo.add(new ProjectorRemote("PRJ-02", "Remote máy chiếu Epson", "Kho A", 55));
        repo.add(new ProjectorRemote("PRJ-01", "Remote máy chiếu Sony", "Kho A", 90));
        repo.add(new ProjectorRemote("PRJ-03", "Remote máy chiếu BenQ", "Kho B", 15)); // pin yếu
        return repo;
    }

    static InMemoryRepository<AirConditionerRemote> airConRepo() {
        InMemoryRepository<AirConditionerRemote> repo = new InMemoryRepository<>();
        repo.add(new AirConditionerRemote("AC-01", "Remote điều hòa Daikin", "Kho A", Set.of("A101", "A203")));
        repo.add(new AirConditionerRemote("AC-02", "Remote điều hòa LG", "Kho B", Set.of("B201", "B202")));
        return repo;
    }

    static InMemoryRepository<WirelessMicrophone> micRepo() {
        InMemoryRepository<WirelessMicrophone> repo = new InMemoryRepository<>();
        repo.add(new WirelessMicrophone("MIC-01", "Micro Shure", "Kho A", 80, 50));  // điểm 65
        repo.add(new WirelessMicrophone("MIC-02", "Micro Sennheiser", "Kho A", 25, 90)); // pin thấp
        repo.add(new WirelessMicrophone("MIC-03", "Micro Audio-Technica", "Kho B", 60, 95)); // điểm 77.5
        return repo;
    }

    // ---------- tiện ích kiểm thử ----------
    static void section(String title) {
        System.out.println("\n== " + title + " ==");
    }

    static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  [PASS] " + name);
        } else {
            failed++;
            System.out.println("  [FAIL] " + name);
        }
    }

    /** Chạy action, trả về ngoại lệ thuộc kiểu mong đợi hoặc null nếu không ném đúng kiểu. */
    static <E extends Throwable> E expectThrows(Class<E> type, Runnable action) {
        try {
            action.run();
        } catch (Throwable t) {
            return type.isInstance(t) ? type.cast(t) : null;
        }
        return null;
    }

    static List<String> ids(List<? extends IIdentifiable> items) {
        return items.stream().map(IIdentifiable::getId).toList();
    }

    // ---------- các test ----------
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        section("1. Thêm thiết bị vào repository");
        InMemoryRepository<ProjectorRemote> projectors = projectorRepo();
        InMemoryRepository<AirConditionerRemote> airCons = airConRepo();
        InMemoryRepository<WirelessMicrophone> mics = micRepo();
        check("3 điều khiển máy chiếu", projectors.getAll().size() == 3);
        check("2 điều khiển điều hòa", airCons.getAll().size() == 2);
        check("3 micro", mics.getAll().size() == 3);
        check("getAll giữ thứ tự thêm vào", ids(projectors.getAll()).equals(List.of("PRJ-02", "PRJ-01", "PRJ-03")));
        check("getAll không lộ collection nội bộ",
                expectThrows(UnsupportedOperationException.class, () -> projectors.getAll().clear()) != null);
        check("không chấp nhận null",
                expectThrows(IllegalArgumentException.class, () -> projectors.add(null)) != null);

        section("2. Thêm hai thiết bị trùng mã");
        DuplicateIdException dup = expectThrows(DuplicateIdException.class,
                () -> projectors.add(new ProjectorRemote("PRJ-01", "Bản sao", "Kho C", 70)));
        check("ném DuplicateIdException", dup != null);
        check("DuplicateId = PRJ-01", dup != null && dup.getDuplicateId().equals("PRJ-01"));
        check("kho không bị thêm phần tử trùng", projectors.getAll().size() == 3);

        section("3. Tìm thiết bị theo mã");
        check("FindById(PRJ-01) có kết quả", projectors.findById("PRJ-01") != null);
        check("FindById(không tồn tại) = null", projectors.findById("PRJ-99") == null);
        check("Remove(không tồn tại) = false", !projectors.remove("PRJ-99"));
        InMemoryRepository<ProjectorRemote> tmp = projectorRepo();
        check("Remove(PRJ-03) = true và FindById = null", tmp.remove("PRJ-03") && tmp.findById("PRJ-03") == null);

        section("4. Lọc các thiết bị sẵn sàng");
        List<ProjectorRemote> readyProjectors = projectors.findAll(TeachingDevice::isReadyForUse);
        check("máy chiếu sẵn sàng: PRJ-02, PRJ-01 (PRJ-03 pin 15% bị loại)",
                ids(readyProjectors).equals(List.of("PRJ-02", "PRJ-01")));
        List<WirelessMicrophone> readyMics = mics.findAll(TeachingDevice::isReadyForUse);
        check("micro sẵn sàng: MIC-01, MIC-03 (MIC-02 pin 25% bị loại)",
                ids(readyMics).equals(List.of("MIC-01", "MIC-03")));

        section("5. Chọn điều khiển máy chiếu có điểm cao nhất");
        AllocationService<ProjectorRemote> projectorService = new AllocationService<>(
                projectorRepo(), new HighestPriorityStrategy<>(ProjectorRemote::getDefaultPriorityScore));
        ProjectorRemote bestProjector = projectorService.allocate(d -> true);
        check("chọn PRJ-01 (pin 90)", bestProjector.getId().equals("PRJ-01"));

        section("6. Chọn điều khiển điều hòa tương thích với một phòng");
        AllocationService<AirConditionerRemote> acService = new AllocationService<>(
                airConRepo(), new HighestPriorityStrategy<>(AirConditionerRemote::getDefaultPriorityScore));
        AirConditionerRemote ac = acService.allocate(d -> d.isCompatibleWith("A203"));
        check("phòng A203 -> AC-01", ac.getId().equals("AC-01"));
        check("phòng không ai tương thích -> NoSuitableItemException",
                expectThrows(NoSuitableItemException.class, () -> acService.allocate(d -> d.isCompatibleWith("C999"))) != null);

        section("7. Chọn micro đầu tiên sẵn sàng");
        AllocationService<WirelessMicrophone> micService = new AllocationService<>(
                micRepo(), new FirstAvailableStrategy<>());
        check("chọn MIC-01", micService.allocate(null).getId().equals("MIC-01"));

        section("8. Đổi chiến lược trong thời gian chạy");
        AllocationService<ProjectorRemote> swap = new AllocationService<>(
                projectorRepo(), new FirstAvailableStrategy<>());
        ProjectorRemote byFirst = swap.allocate(null);
        check("FirstAvailable -> PRJ-02 (đứng đầu danh sách sẵn sàng)", byFirst.getId().equals("PRJ-02"));
        swap.changeStrategy(new HighestPriorityStrategy<>(ProjectorRemote::getDefaultPriorityScore));
        ProjectorRemote byHighest = swap.allocate(null);
        check("đổi sang HighestPriority -> PRJ-01 (pin 90)", byHighest.getId().equals("PRJ-01"));
        check("đổi chiến lược sang null bị từ chối",
                expectThrows(IllegalArgumentException.class, () -> swap.changeStrategy(null)) != null);

        section("9. Cấp phát khi không có ứng viên phù hợp");
        AllocationService<ProjectorRemote> empty = new AllocationService<>(
                projectorRepo(), new FirstAvailableStrategy<>());
        empty.allocate(null);
        empty.allocate(null);
        check("hết máy chiếu sẵn sàng -> NoSuitableItemException",
                expectThrows(NoSuitableItemException.class, () -> empty.allocate(null)) != null);
        check("kho rỗng -> NoSuitableItemException",
                expectThrows(NoSuitableItemException.class, () -> new AllocationService<>(
                        new InMemoryRepository<WirelessMicrophone>(), new HighestPriorityStrategy<WirelessMicrophone>(
                        TeachingDevice::getDefaultPriorityScore)).allocate(null)) != null);

        section("10. Thiết bị được chọn chuyển sang Borrowed");
        InMemoryRepository<WirelessMicrophone> micRepo2 = micRepo();
        AllocationService<WirelessMicrophone> micService2 = new AllocationService<>(
                micRepo2, new HighestPriorityStrategy<>(TeachingDevice::getDefaultPriorityScore));
        WirelessMicrophone picked = micService2.allocate(null);
        check("chọn MIC-03 (điểm 77.5)", picked.getId().equals("MIC-03") && picked.getDefaultPriorityScore() == 77.5);
        check("trạng thái = BORROWED", picked.getStatus() == DeviceStatus.BORROWED);
        check("mượn lại thiết bị đang mượn -> DeviceNotReadyException",
                expectThrows(DeviceNotReadyException.class, picked::borrow) != null);
        DeviceNotReadyException notReady = expectThrows(DeviceNotReadyException.class, picked::borrow);
        check("ngoại lệ chứa DeviceId và CurrentStatus",
                notReady != null && notReady.getDeviceId().equals("MIC-03")
                        && notReady.getCurrentStatus() == DeviceStatus.BORROWED);
        picked.returnDevice();
        check("trả về kho -> AVAILABLE", picked.getStatus() == DeviceStatus.AVAILABLE);
        check("trả khi không phải Borrowed -> IllegalStateException",
                expectThrows(IllegalStateException.class, picked::returnDevice) != null);
        picked.sendToMaintenance();
        check("thiết bị đang bảo trì không sẵn sàng", !picked.isReadyForUse());
        check("Allocate bỏ qua thiết bị bảo trì -> MIC-01",
                micService2.allocate(null).getId().equals("MIC-01"));

        section("11. Một repository generic dùng cho nhiều loại thiết bị");
        IRepository<ProjectorRemote> r1 = new InMemoryRepository<>();
        IRepository<AirConditionerRemote> r2 = new InMemoryRepository<>();
        IRepository<WirelessMicrophone> r3 = new InMemoryRepository<>();
        r1.add(new ProjectorRemote("X-1", "P", "Kho", 50));
        r2.add(new AirConditionerRemote("X-1", "A", "Kho", List.of("A1")));
        r3.add(new WirelessMicrophone("X-1", "M", "Kho", 50, 50));
        check("cùng InMemoryRepository<T>, mỗi kiểu một kho độc lập (cùng Id X-1 không xung đột)",
                r1.findById("X-1") != null && r2.findById("X-1") != null && r3.findById("X-1") != null);

        section("Bổ sung: kiểm tra quy tắc nghiệp vụ và hợp lệ dữ liệu");
        check("máy chiếu pin đúng 20% sẵn sàng", new ProjectorRemote("P", "n", "l", 20).isReadyForUse());
        check("máy chiếu pin 19% chưa sẵn sàng", !new ProjectorRemote("P", "n", "l", 19).isReadyForUse());
        check("micro pin 30% & tín hiệu 40% sẵn sàng", new WirelessMicrophone("M", "n", "l", 30, 40).isReadyForUse());
        check("micro tín hiệu 39% chưa sẵn sàng", !new WirelessMicrophone("M", "n", "l", 30, 39).isReadyForUse());
        check("pin ngoài 0..100 bị từ chối",
                expectThrows(IllegalArgumentException.class, () -> new ProjectorRemote("P", "n", "l", 101)) != null);
        check("Id rỗng bị từ chối",
                expectThrows(IllegalArgumentException.class, () -> new ProjectorRemote(" ", "n", "l", 50)) != null);
        check("điều hòa: điểm theo phòng 100/0",
                airConRepo().findById("AC-01").getPriorityScoreFor("A101") == 100
                        && airConRepo().findById("AC-01").getPriorityScoreFor("B201") == 0);
        check("CompatibleRooms không sửa được từ bên ngoài",
                expectThrows(UnsupportedOperationException.class,
                        () -> airConRepo().findById("AC-01").getCompatibleRooms().add("Z")) != null);
        check("chiến lược không thay đổi danh sách đầu vào", unchangedInput());

        System.out.println("\n==============================");
        System.out.println("Kết quả: " + passed + " đạt, " + failed + " không đạt.");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean unchangedInput() {
        List<ProjectorRemote> input = projectorRepo().getAll();
        List<String> before = ids(input);
        new HighestPriorityStrategy<ProjectorRemote>(TeachingDevice::getDefaultPriorityScore).select(input);
        new FirstAvailableStrategy<ProjectorRemote>().select(input);
        return ids(input).equals(before);
    }
}
