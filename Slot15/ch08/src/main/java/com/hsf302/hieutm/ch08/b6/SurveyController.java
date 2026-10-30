package com.hsf302.hieutm.ch08.b6;

import com.hsf302.hieutm.ch08.common.AlertUtil;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SurveyController {

    @FXML private TextField tfStudentId;
    @FXML private TextField tfFullName;
    @FXML private ChoiceBox<String> cbMajor;
    @FXML private Spinner<Integer> spSemester;
    @FXML private ToggleGroup tgGender;
    @FXML private DatePicker dpDate;
    @FXML private Slider slSatisfaction;
    @FXML private Label lblSatisfaction;
    @FXML private Label lblCharCount;
    @FXML private CheckBox chkSpring;
    @FXML private CheckBox chkJpa;
    @FXML private CheckBox chkFx;
    @FXML private CheckBox chkTesting;
    @FXML private ColorPicker cpColor;
    @FXML private ComboBox<String> cbLanguage;
    @FXML private ListView<String> lvLanguages;
    @FXML private TextArea taComment;
    @FXML private ProgressBar pbComplete;

    private static final String[] LEVELS = {
            "", "Rất không hài lòng", "Không hài lòng", "Bình thường", "Hài lòng", "Rất hài lòng"
    };

    @FXML
    public void initialize() {
        // TODO 1: Nạp items & Cấu hình các controls lựa chọn
        cbMajor.getItems().addAll("SE", "AI", "IA", "GD");
        spSemester.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 5));
        cbLanguage.getItems().addAll("Java", "C#", "Python", "JavaScript", "Kotlin");
        cbLanguage.setEditable(true);

        dpDate.setValue(LocalDate.now());
        // Chặn người dùng chọn ngày ở tương lai bằng DateCellFactory
        dpDate.setDayCellFactory(p -> new DateCell() {
            @Override
            public void updateItem(LocalDate d, boolean empty) {
                super.updateItem(d, empty);
                setDisable(empty || d.isAfter(LocalDate.now()));
            }
        });

        // TODO 2: Cấu hình Slider mức độ hài lòng từ 1..5 và bind text mô tả
        slSatisfaction.setMin(1);
        slSatisfaction.setMax(5);
        slSatisfaction.setValue(3);
        slSatisfaction.setMajorTickUnit(1);
        slSatisfaction.setMinorTickCount(0);
        slSatisfaction.setSnapToTicks(true);       // Chỉ cho phép trỏ vào các nấc nguyên
        slSatisfaction.setShowTickLabels(true);
        slSatisfaction.setShowTickMarks(true);
        lblSatisfaction.textProperty().bind(Bindings.createStringBinding(
                () -> LEVELS[(int) Math.round(slSatisfaction.getValue())],
                slSatisfaction.valueProperty()));

        // TODO 3: Giới hạn TextArea tối đa 300 ký tự qua TextFormatter & bind nhãn đếm
        taComment.setTextFormatter(new TextFormatter<String>(c ->
                c.getControlNewText().length() <= 300 ? c : null));
        lblCharCount.textProperty().bind(taComment.textProperty().length().asString("%d/300 ký tự"));

        // TODO 4: Cấu hình ListView — ContextMenu & phím Delete
        MenuItem miDelete = new MenuItem("Xóa");
        miDelete.setOnAction(e -> removeSelectedLanguage());
        lvLanguages.setContextMenu(new ContextMenu(miDelete));
        lvLanguages.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE) removeSelectedLanguage();
        });
        lvLanguages.setPlaceholder(new Label("Chưa có ngôn ngữ nào được thêm"));

        // TODO 5: Tooltip định dạng MSSV
        tfStudentId.setTooltip(new Tooltip("Định dạng: SE + 6 chữ số, VD: SE123456"));

        // TODO 6: Binding tiến độ hoàn thành dựa trên 6 nhóm trường bắt buộc
        pbComplete.progressProperty().bind(Bindings.createDoubleBinding(() -> {
            int done = 0;
            if (!tfStudentId.getText().isBlank()) done++;
            if (!tfFullName.getText().isBlank()) done++;
            if (cbMajor.getValue() != null) done++;
            if (tgGender.getSelectedToggle() != null) done++;
            if (dpDate.getValue() != null) done++;
            if (chkSpring.isSelected() || chkJpa.isSelected() || chkFx.isSelected() || chkTesting.isSelected()) done++;
            return done / 6.0;
        }, tfStudentId.textProperty(), tfFullName.textProperty(), cbMajor.valueProperty(),
           tgGender.selectedToggleProperty(), dpDate.valueProperty(),
           chkSpring.selectedProperty(), chkJpa.selectedProperty(), chkFx.selectedProperty(), chkTesting.selectedProperty()));
    }

    // TODO 7: Thêm ngôn ngữ từ editable ComboBox
    @FXML
    private void handleAddLanguage() {
        // Với ComboBox editable: Giá trị gõ tay nằm ở getEditor().getText()
        String lang = cbLanguage.getEditor().getText().trim();
        if (lang.isEmpty()) {
            AlertUtil.warn("Vui lòng nhập hoặc chọn một ngôn ngữ!");
            return;
        }
        boolean dup = lvLanguages.getItems().stream().anyMatch(l -> l.equalsIgnoreCase(lang));
        if (dup) {
            AlertUtil.warn("Ngôn ngữ '" + lang + "' đã có trong danh sách!");
            return;
        }
        lvLanguages.getItems().add(lang);
        cbLanguage.getEditor().clear();
        cbLanguage.setValue(null);
    }

    private void removeSelectedLanguage() {
        String sel = lvLanguages.getSelectionModel().getSelectedItem();
        if (sel != null) lvLanguages.getItems().remove(sel);
    }

    // TODO 8: Submit khảo sát
    @FXML
    private void handleSubmit() {
        List<String> errors = new ArrayList<>();
        if (!tfStudentId.getText().trim().toUpperCase().matches("SE\\d{6}")) {
            errors.add("MSSV phải có dạng SE + 6 số (VD: SE123456)");
        }
        if (tfFullName.getText().trim().length() < 3) {
            errors.add("Họ tên phải từ 3 ký tự trở lên");
        }
        if (cbMajor.getValue() == null) {
            errors.add("Vui lòng chọn ngành học");
        }
        if (tgGender.getSelectedToggle() == null) {
            errors.add("Vui lòng chọn giới tính");
        }
        if (dpDate.getValue() == null || dpDate.getValue().isAfter(LocalDate.now())) {
            errors.add("Ngày khảo sát không hợp lệ (không được ở tương lai)");
        }
        List<String> liked = Stream.of(chkSpring, chkJpa, chkFx, chkTesting)
                .filter(CheckBox::isSelected).map(CheckBox::getText).toList();
        if (liked.isEmpty()) {
            errors.add("Vui lòng chọn ít nhất 1 phần yêu thích nhất môn học");
        }

        if (!errors.isEmpty()) {
            AlertUtil.warn("Vui lòng sửa các lỗi sau:\n• " + String.join("\n• ", errors));
            return;
        }

        String summary = """
                MSSV: %s | Họ tên: %s
                Ngành: %s | Kỳ học: %d | Giới tính: %s
                Ngày khảo sát: %s
                Mức hài lòng: %s (Mức %d)
                Phần thích nhất: %s
                Màu giao diện yêu thích: %s
                Ngôn ngữ đã biết: %s
                Góp ý: %s""".formatted(
                tfStudentId.getText().trim().toUpperCase(),
                tfFullName.getText().trim(),
                cbMajor.getValue(),
                spSemester.getValue(),
                ((RadioButton) tgGender.getSelectedToggle()).getText(),
                dpDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                lblSatisfaction.getText(),
                (int) Math.round(slSatisfaction.getValue()),
                String.join(", ", liked),
                cpColor.getValue() == null ? "Mặc định" : cpColor.getValue().toString(),
                lvLanguages.getItems().isEmpty() ? "(không)" : String.join(", ", lvLanguages.getItems()),
                taComment.getText().isBlank() ? "(không)" : taComment.getText());

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Khảo sát thành công");
        a.setHeaderText("Cảm ơn bạn đã gửi phiếu khảo sát!");
        TextArea taResult = new TextArea(summary);
        taResult.setEditable(false);
        taResult.setWrapText(true);
        taResult.setPrefRowCount(10);
        a.getDialogPane().setContent(taResult);
        a.showAndWait();
    }

    @FXML
    private void handleReset() {
        tfStudentId.clear();
        tfFullName.clear();
        cbMajor.setValue(null);
        spSemester.getValueFactory().setValue(5);
        tgGender.selectToggle(null);
        dpDate.setValue(LocalDate.now());
        slSatisfaction.setValue(3);
        Stream.of(chkSpring, chkJpa, chkFx, chkTesting).forEach(c -> c.setSelected(false));
        cpColor.setValue(Color.WHITE);
        lvLanguages.getItems().clear();
        taComment.clear();
    }

    @FXML
    private void handleSyllabus() {
        AlertUtil.info("Đề cương HSF302:\n• Slot 1–5: Spring Core & IoC\n• Slot 6–10: Spring Data JPA\n• Slot 11–15: JavaFX Desktop GUI & Concurrency\n• Slot 16–20: Automated Testing & Practice Exam");
    }
}
