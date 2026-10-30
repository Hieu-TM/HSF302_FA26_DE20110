package com.hsf302.hieutm.ch08.t1;

import com.hsf302.hieutm.ch08.common.AlertUtil;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StudentController {

    @FXML private TreeView<String> tvFaculty;
    @FXML private TextField tfSearch, tfId, tfName, tfGpa;
    @FXML private TableView<Student> tblStudents;
    @FXML private TableColumn<Student, String> colId, colName, colClass, colGrade;
    @FXML private TableColumn<Student, Double> colGpa;
    @FXML private ComboBox<String> cbClass;
    @FXML private Label lblCount;
    @FXML private Button btnUpdate, btnDelete;

    // Khoa → Danh sách lớp tương ứng
    private static final Map<String, List<String>> FACULTIES = new LinkedHashMap<>();
    static {
        FACULTIES.put("Kỹ thuật phần mềm", List.of("SE1801", "SE1802", "SE1803"));
        FACULTIES.put("Trí tuệ nhân tạo", List.of("AI1801", "AI1802"));
    }

    // Extractor: Báo hiệu cho FilteredList lọc lại khi sửa studentId, fullName hoặc className của 1 sinh viên
    private final ObservableList<Student> students = FXCollections.observableArrayList(
            s -> new Observable[]{s.studentIdProperty(), s.fullNameProperty(), s.classNameProperty(), s.gpaProperty()});
    {
        students.addAll(
                new Student("SE180001", "Nguyễn Văn An", "SE1801", 8.2),
                new Student("SE180002", "Trần Thị Bình", "SE1801", 9.1),
                new Student("SE180010", "Lê Văn Cường", "SE1802", 6.4),
                new Student("AI180003", "Phạm Thu Dung", "AI1801", 7.5));
    }

    private final FilteredList<Student> filtered = new FilteredList<>(students);

    @FXML
    public void initialize() {
        // TODO 3: Dựng cây phân cấp FPT University -> Khoa -> Lớp
        TreeItem<String> root = new TreeItem<>("FPT University");
        FACULTIES.forEach((fac, classes) -> {
            TreeItem<String> facItem = new TreeItem<>(fac);
            classes.forEach(c -> facItem.getChildren().add(new TreeItem<>(c)));
            facItem.setExpanded(true);
            root.getChildren().add(facItem);
        });
        root.setExpanded(true);
        tvFaculty.setRoot(root);
        cbClass.getItems().setAll(FACULTIES.values().stream().flatMap(List::stream).toList());

        // TODO 4: Cấu hình TableColumn với PropertyValueFactory
        // LƯU Ý: Yêu cầu module-info phải 'opens com.hsf302.hieutm.ch08.t1 to javafx.base'
        colId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("className"));
        colGpa.setCellValueFactory(new PropertyValueFactory<>("gpa"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Chuỗi bọc SortedList(FilteredList) đảm bảo sắp xếp click cột vẫn hoạt động khi đang lọc
        SortedList<Student> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tblStudents.comparatorProperty());
        tblStudents.setItems(sorted);
        tblStudents.setPlaceholder(new Label("Không có sinh viên nào"));
        lblCount.textProperty().bind(Bindings.size(filtered).asString("Tổng số: %d sinh viên"));

        // TODO 5: Predicate kết hợp: Node cây đang chọn + Từ khóa tìm kiếm
        filtered.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            TreeItem<String> node = tvFaculty.getSelectionModel().getSelectedItem();
            String kw = tfSearch.getText() == null ? "" : tfSearch.getText().trim().toLowerCase();
            Set<String> allowedClasses = classesOf(node);
            return (Predicate<Student>) s ->
                    (allowedClasses == null || allowedClasses.contains(s.getClassName()))
                    && (kw.isEmpty() || s.getStudentId().toLowerCase().contains(kw) || s.getFullName().toLowerCase().contains(kw));
        }, tvFaculty.getSelectionModel().selectedItemProperty(), tfSearch.textProperty()));

        // TODO 6: Click chọn dòng trong bảng → Điền thông tin vào form; Vô hiệu hóa nút khi chưa chọn dòng
        tblStudents.getSelectionModel().selectedItemProperty().addListener((o, a, s) -> {
            if (s == null) return;
            tfId.setText(s.getStudentId());
            tfName.setText(s.getFullName());
            cbClass.setValue(s.getClassName());
            tfGpa.setText(String.valueOf(s.getGpa()));
        });

        BooleanBinding noSel = tblStudents.getSelectionModel().selectedItemProperty().isNull();
        btnUpdate.disableProperty().bind(noSel);
        btnDelete.disableProperty().bind(noSel);
    }

    /** Trả về danh sách các lớp được phép hiển thị dựa trên node cây được chọn (null = chọn gốc hoặc chưa chọn) */
    private Set<String> classesOf(TreeItem<String> node) {
        if (node == null || node.getParent() == null) return null;      // Đang chọn Gốc FPT University
        if (node.isLeaf()) return Set.of(node.getValue());             // Đang chọn một Lớp cụ thể
        return node.getChildren().stream().map(TreeItem::getValue).collect(Collectors.toSet()); // Đang chọn một Khoa
    }

    // TODO 7: Đọc và Validate dữ liệu form nhập liệu
    private Student readForm(Student editing) {
        String id = tfId.getText().trim().toUpperCase();
        String name = tfName.getText().trim();
        String cls = cbClass.getValue();

        if (!id.matches("(SE|AI)\\d{6}")) {
            AlertUtil.warn("MSSV phải bắt đầu bằng SE hoặc AI kèm 6 số!");
            return null;
        }
        if (name.length() < 3) {
            AlertUtil.warn("Họ tên phải từ 3 ký tự trở lên!");
            return null;
        }
        if (cls == null) {
            AlertUtil.warn("Vui lòng chọn lớp học!");
            return null;
        }
        if (!id.startsWith(cls.substring(0, 2))) {
            AlertUtil.warn("MSSV (%s) và lớp (%s) không cùng ngành học!".formatted(id.substring(0, 2), cls));
            return null;
        }

        double gpa;
        try {
            gpa = Double.parseDouble(tfGpa.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            AlertUtil.warn("Điểm GPA phải là định dạng số hợp lệ!");
            return null;
        }

        if (gpa < 0.0 || gpa > 10.0) {
            AlertUtil.warn("Điểm GPA phải nằm trong khoảng từ 0.0 đến 10.0!");
            return null;
        }

        // Chặn trùng lặp MSSV với các sinh viên khác (bỏ qua sinh viên đang được sửa)
        boolean dup = students.stream().anyMatch(s -> s != editing && s.getStudentId().equalsIgnoreCase(id));
        if (dup) {
            AlertUtil.warn("MSSV '" + id + "' đã tồn tại trong hệ thống!");
            return null;
        }

        return new Student(id, name, cls, gpa);
    }

    // TODO 8: Các thao tác CRUD
    @FXML
    private void handleAdd() {
        Student s = readForm(null);
        if (s != null) {
            students.add(s);
            handleClear();
            AlertUtil.info("Thêm sinh viên thành công!");
        }
    }

    @FXML
    private void handleUpdate() {
        Student sel = tblStudents.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        Student s = readForm(sel);
        if (s == null) return;

        // Cập nhật giá trị vào các JavaFX Properties -> TableView tự động cập nhật hiển thị tức thì
        sel.setStudentId(s.getStudentId());
        sel.setFullName(s.getFullName());
        sel.setClassName(s.getClassName());
        sel.setGpa(s.getGpa());
        AlertUtil.info("Cập nhật thông tin sinh viên thành công!");
    }

    @FXML
    private void handleDelete() {
        Student sel = tblStudents.getSelectionModel().getSelectedItem();
        if (sel != null && AlertUtil.confirm("Bạn có chắc chắn muốn xóa sinh viên: " + sel.getFullName() + "?")) {
            students.remove(sel);
            handleClear();
        }
    }

    @FXML
    private void handleClear() {
        tfId.clear();
        tfName.clear();
        tfGpa.clear();
        cbClass.setValue(null);
        tblStudents.getSelectionModel().clearSelection();
    }

    // TODO 9: Import CSV (studentId,fullName,className,gpa)
    @FXML
    private void handleImport() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Import danh sách sinh viên từ file CSV");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
        File f = fc.showOpenDialog(tblStudents.getScene().getWindow());
        if (f == null) return;

        int ok = 0;
        List<String> errors = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) { // Bỏ qua dòng tiêu đề đầu tiên
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",");
                try {
                    if (p.length != 4) throw new IllegalArgumentException("Dòng không đủ 4 cột");
                    String id = p[0].trim().toUpperCase();
                    String name = p[1].trim();
                    String cls = p[2].trim();
                    double gpa = Double.parseDouble(p[3].trim());

                    if (!id.matches("(SE|AI)\\d{6}") || gpa < 0 || gpa > 10) {
                        throw new IllegalArgumentException("Dữ liệu định dạng sai");
                    }
                    if (students.stream().anyMatch(s -> s.getStudentId().equalsIgnoreCase(id))) {
                        throw new IllegalArgumentException("Trùng lặp MSSV '" + id + "'");
                    }

                    students.add(new Student(id, name, cls, gpa));
                    ok++;
                } catch (Exception ex) {
                    errors.add("Dòng " + (i + 1) + ": " + ex.getMessage());
                }
            }
            AlertUtil.info("Import hoàn tất: %d dòng thành công, %d dòng lỗi.%s".formatted(
                    ok, errors.size(),
                    errors.isEmpty() ? "" : "\nChi tiết lỗi:\n" + String.join("\n", errors.subList(0, Math.min(10, errors.size())))));
        } catch (IOException e) {
            AlertUtil.error("Không thể đọc file CSV: " + e.getMessage());
        }
    }

    // TODO 10: Export CSV những sinh viên đang hiển thị (theo bộ lọc hiện tại)
    @FXML
    private void handleExport() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Export danh sách sinh viên ra file CSV");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
        fc.setInitialFileName("students_export.csv");
        File f = fc.showSaveDialog(tblStudents.getScene().getWindow());
        if (f == null) return;

        List<String> lines = new ArrayList<>();
        lines.add("studentId,fullName,className,gpa");
        tblStudents.getItems().forEach(s -> lines.add(
                String.join(",", s.getStudentId(), s.getFullName(), s.getClassName(), String.valueOf(s.getGpa()))));

        try {
            Files.write(f.toPath(), lines, StandardCharsets.UTF_8);
            AlertUtil.info("Đã xuất thành công %d sinh viên ra file!".formatted(lines.size() - 1));
        } catch (IOException e) {
            AlertUtil.error("Lỗi khi ghi file CSV: " + e.getMessage());
        }
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }
}
