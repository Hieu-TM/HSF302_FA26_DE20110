package com.hsf302.hieutm.ch08.t2;

import com.hsf302.hieutm.ch08.common.AlertUtil;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ShapeEditorApp extends Application {

    enum Tool { SELECT, RECT, CIRCLE, LINE, TRIANGLE }

    private final Pane drawPane = new Pane();
    private final ObjectProperty<Tool> tool = new SimpleObjectProperty<>(Tool.SELECT);
    private final ObjectProperty<Shape> selected = new SimpleObjectProperty<>();
    private final ColorPicker cpFill = new ColorPicker(Color.web("#64b5f6"));
    private final ColorPicker cpStroke = new ColorPicker(Color.web("#0d47a1"));
    private final Slider slStroke = new Slider(0, 10, 2);
    private final Slider slRotate = new Slider(0, 360, 0);
    private final Slider slScale = new Slider(0.2, 3, 1);
    private final Slider slOpacity = new Slider(0, 1, 1);
    private final CheckBox chkBlur = new CheckBox("Blur (làm mờ)");
    private final Label lblStatus = new Label();

    private double startX, startY, dragOffsetX, dragOffsetY;
    private Shape drawing; // Shape đang được vẽ trong lúc kéo chuột

    @Override
    public void start(Stage stage) {
        // TODO 1: Toolbar chọn công cụ vẽ
        ToggleGroup tg = new ToggleGroup();
        HBox modeBox = new HBox(4);
        for (Tool t : Tool.values()) {
            ToggleButton b = new ToggleButton(switch (t) {
                case SELECT -> "🖱 Chọn";
                case RECT -> "▭ Chữ nhật";
                case CIRCLE -> "◯ Tròn";
                case LINE -> "╱ Đường thẳng";
                case TRIANGLE -> "△ Tam giác";
            });
            b.setToggleGroup(tg);
            b.setUserData(t);
            if (t == Tool.SELECT) b.setSelected(true);
            modeBox.getChildren().add(b);
        }

        // Chặn không cho người dùng bỏ chọn tất cả nút trong ToggleGroup
        tg.selectedToggleProperty().addListener((o, a, n) -> {
            if (n == null) a.setSelected(true);
            else tool.set((Tool) n.getUserData());
        });

        Button btnClear = new Button("Xóa tất cả");
        btnClear.setOnAction(e -> {
            if (AlertUtil.confirm("Bạn có chắc chắn muốn xóa toàn bộ các hình?")) {
                drawPane.getChildren().clear();
                selected.set(null);
            }
        });

        ToolBar toolbar = new ToolBar(
                modeBox, new Separator(),
                new Label("Tô:"), cpFill,
                new Label("Viền:"), cpStroke,
                new Label("Độ dày:"), slStroke,
                new Separator(), btnClear);

        // TODO 2: Vùng vẽ Pane — Kỹ thuật Clip theo kích thước khung vẽ
        drawPane.setStyle("-fx-background-color: white;");
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(drawPane.widthProperty());
        clip.heightProperty().bind(drawPane.heightProperty());
        drawPane.setClip(clip); // Chặn tuyệt đối không cho shape tràn ra ngoài vùng vẽ

        drawPane.setOnMousePressed(this::onPressed);
        drawPane.setOnMouseDragged(this::onDragged);
        drawPane.setOnMouseReleased(this::onReleased);
        drawPane.setOnMouseMoved(e -> updateStatus(e.getX(), e.getY()));

        // TODO 3: Panel thuộc tính & Animation bên phải
        Button btnSpin = new Button("Xoay 360°");
        Button btnBlink = new Button("Nhấp nháy");
        Button btnColor = new Button("Đổi màu liên tục");
        btnSpin.setMaxWidth(Double.MAX_VALUE);
        btnBlink.setMaxWidth(Double.MAX_VALUE);
        btnColor.setMaxWidth(Double.MAX_VALUE);

        btnSpin.setOnAction(e -> spin());
        btnBlink.setOnAction(e -> blink());
        btnColor.setOnAction(e -> colorCycle());

        VBox right = new VBox(8,
                new Label("Xoay góc:"), slRotate,
                new Label("Phóng to (Scale):"), slScale,
                new Label("Độ mờ (Opacity):"), slOpacity,
                chkBlur, new Separator(),
                btnSpin, btnBlink, btnColor);
        right.setPadding(new Insets(10));
        right.setPrefWidth(200);
        right.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");
        right.disableProperty().bind(selected.isNull()); // Chưa chọn hình nào thì vô hiệu hóa cả panel

        // TODO 4: Lắng nghe khi thay đổi shape được chọn → Chuyển binding
        selected.addListener((o, oldS, newS) -> onSelectionChanged(oldS, newS));

        BorderPane root = new BorderPane(drawPane, toolbar, right, new HBox(lblStatus), null);
        Scene scene = new Scene(root, 1100, 700);

        // TODO 5: Xử lý phím tắt
        scene.setOnKeyPressed(e -> {
            Shape s = selected.get();
            if (s == null) return;
            switch (e.getCode()) {
                case DELETE, BACK_SPACE -> {
                    drawPane.getChildren().remove(s);
                    selected.set(null);
                }
                case ESCAPE -> selected.set(null);
                case LEFT -> s.setTranslateX(s.getTranslateX() - 5);
                case RIGHT -> s.setTranslateX(s.getTranslateX() + 5);
                case UP -> s.setTranslateY(s.getTranslateY() - 5);
                case DOWN -> s.setTranslateY(s.getTranslateY() + 5);
                default -> {}
            }
            updateStatus(0, 0);
        });

        stage.setTitle("T2 - Shape Editor (2D Graphics & Animation)");
        stage.setScene(scene);
        stage.show();
    }

    // TODO 6: Sự kiện Mouse Pressed
    private void onPressed(MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY) return;
        startX = e.getX();
        startY = e.getY();

        if (tool.get() == Tool.SELECT) {
            // Kiểm tra click trúng shape nằm trực tiếp trong drawPane
            if (e.getTarget() instanceof Shape s && s.getParent() == drawPane) {
                selected.set(s);
                dragOffsetX = e.getX() - s.getTranslateX();
                dragOffsetY = e.getY() - s.getTranslateY();
            } else {
                selected.set(null); // Click vào khoảng trống -> Hủy chọn
            }
            return;
        }

        // Tạo đối tượng Shape mới theo công cụ đang chọn (Pattern matching switch của Java 21)
        drawing = switch (tool.get()) {
            case RECT -> new Rectangle(startX, startY, 0, 0);
            case CIRCLE -> new Circle(startX, startY, 0);
            case LINE -> new Line(startX, startY, startX, startY);
            case TRIANGLE -> new Polygon(startX, startY, startX, startY, startX, startY);
            default -> null;
        };

        if (drawing != null) {
            if (!(drawing instanceof Line)) drawing.setFill(cpFill.getValue());
            drawing.setStroke(cpStroke.getValue());
            drawing.setStrokeWidth(Math.max(1, slStroke.getValue()));
            installContextMenu(drawing);
            drawPane.getChildren().add(drawing);
        }
    }

    // TODO 7: Sự kiện Mouse Dragged
    private void onDragged(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        updateStatus(x, y);

        if (tool.get() == Tool.SELECT) {
            Shape s = selected.get();
            if (s != null) {
                s.setTranslateX(x - dragOffsetX);
                s.setTranslateY(y - dragOffsetY);
            }
            return;
        }

        if (drawing == null) return;

        // Cập nhật kích thước shape theo vị trí chuột kéo
        switch (drawing) {
            case Rectangle r -> {
                // Kéo ngược lên trên/sang trái vẫn vẽ đúng hình chữ nhật
                r.setX(Math.min(startX, x));
                r.setY(Math.min(startY, y));
                r.setWidth(Math.abs(x - startX));
                r.setHeight(Math.abs(y - startY));
            }
            case Circle c -> c.setRadius(Math.hypot(x - startX, y - startY));
            case Line l -> {
                l.setEndX(x);
                l.setEndY(y);
            }
            case Polygon p -> {
                // Vẽ tam giác cân dựa trên bounding box chuột kéo
                p.getPoints().setAll(
                        startX + (x - startX) / 2, startY, // đỉnh
                        startX, y,                         // góc trái
                        x, y                               // góc phải
                );
            }
            default -> {}
        }
    }

    // TODO 8: Sự kiện Mouse Released — Tự động loại bỏ shape quá nhỏ do click nhầm
    private void onReleased(MouseEvent e) {
        if (drawing == null) return;
        Bounds b = drawing.getLayoutBounds();
        if (b.getWidth() < 5 && b.getHeight() < 5) {
            drawPane.getChildren().remove(drawing);
        }
        drawing = null;
        updateStatus(e.getX(), e.getY());
    }

    // TODO 9: Chuyển đổi Binding sang Shape mới
    private final DropShadow selectionEffect = new DropShadow(14, Color.ORANGE);

    private void onSelectionChanged(Shape oldS, Shape newS) {
        // Gỡ bỏ binding của shape cũ để không bị slider điều khiển ngoài ý muốn
        if (oldS != null) {
            oldS.rotateProperty().unbind();
            oldS.scaleXProperty().unbind();
            oldS.scaleYProperty().unbind();
            oldS.opacityProperty().unbind();
            oldS.getStrokeDashArray().clear();
            oldS.setEffect(Boolean.TRUE.equals(oldS.getProperties().get("blur")) ? new GaussianBlur(8) : null);
        }

        if (newS != null) {
            // Đưa giá trị các slider về trạng thái hiện tại của shape TRƯỚC KHI BIND
            slRotate.setValue(newS.getRotate());
            slScale.setValue(newS.getScaleX());
            slOpacity.setValue(newS.getOpacity());
            chkBlur.setSelected(Boolean.TRUE.equals(newS.getProperties().get("blur")));

            // Bind một chiều từ Slider sang Shape thuộc tính
            newS.rotateProperty().bind(slRotate.valueProperty());
            newS.scaleXProperty().bind(slScale.valueProperty());
            newS.scaleYProperty().bind(slScale.valueProperty());
            newS.opacityProperty().bind(slOpacity.valueProperty());

            // Đánh dấu shape đang chọn bằng viền nét đứt và bóng đổ màu cam rực rỡ
            newS.getStrokeDashArray().setAll(8.0, 4.0);
            newS.setEffect(selectionEffect);
        }
        updateStatus(0, 0);
    }

    {
        chkBlur.selectedProperty().addListener((o, a, on) -> {
            if (selected.get() != null) {
                selected.get().getProperties().put("blur", on);
            }
        });
    }

    // TODO 10: Menu ngữ cảnh (ContextMenu) cho từng hình
    private void installContextMenu(Shape s) {
        MenuItem front = new MenuItem("Đưa lên lớp trên cùng");
        MenuItem back = new MenuItem("Đưa xuống lớp dưới cùng");
        MenuItem dup = new MenuItem("Nhân bản hình (Duplicate)");
        MenuItem del = new MenuItem("Xóa hình");

        front.setOnAction(e -> s.toFront());
        back.setOnAction(e -> s.toBack());
        del.setOnAction(e -> {
            drawPane.getChildren().remove(s);
            if (selected.get() == s) selected.set(null);
        });
        dup.setOnAction(e -> duplicate(s));

        ContextMenu cm = new ContextMenu(front, back, dup, del);
        s.setOnContextMenuRequested(e -> cm.show(s, e.getScreenX(), e.getScreenY()));
    }

    private void duplicate(Shape s) {
        Shape copy = switch (s) {
            case Rectangle r -> new Rectangle(r.getX(), r.getY(), r.getWidth(), r.getHeight());
            case Circle c -> new Circle(c.getCenterX(), c.getCenterY(), c.getRadius());
            case Line l -> new Line(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY());
            case Polygon p -> {
                Polygon q = new Polygon();
                q.getPoints().setAll(p.getPoints());
                yield q;
            }
            default -> null;
        };
        if (copy == null) return;

        copy.setFill(s.getFill());
        copy.setStroke(s.getStroke());
        copy.setStrokeWidth(s.getStrokeWidth());
        copy.setTranslateX(s.getTranslateX() + 25);
        copy.setTranslateY(s.getTranslateY() + 25);
        copy.setRotate(s.getRotate());
        copy.setScaleX(s.getScaleX());
        copy.setScaleY(s.getScaleY());

        installContextMenu(copy);
        drawPane.getChildren().add(copy);
    }

    // TODO 11: Animations
    // 11a. Animation xoay: Do rotateProperty của shape đang bind với slRotate, ta animate chính Slider!
    private void spin() {
        Timeline t = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(slRotate.valueProperty(), 0)),
                new KeyFrame(Duration.seconds(1.2), new KeyValue(slRotate.valueProperty(), 360, Interpolator.EASE_BOTH)));
        t.play();
    }

    // 11b. Animation nhấp nháy: FadeTransition tác động trực tiếp lên opacity của node -> phải unbind trước
    private void blink() {
        Shape s = selected.get();
        if (s == null) return;
        s.opacityProperty().unbind();
        FadeTransition ft = new FadeTransition(Duration.millis(250), s);
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setCycleCount(6);
        ft.setAutoReverse(true);
        // Hoàn thành animation -> bind trở lại với slider
        ft.setOnFinished(e -> {
            if (selected.get() == s) s.opacityProperty().bind(slOpacity.valueProperty());
        });
        ft.play();
    }

    // 11c. Animation đổi màu tuần tự bằng Timeline
    private void colorCycle() {
        Shape s = selected.get();
        if (s == null) return;
        Paint original = s.getFill();
        Timeline t = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(s.fillProperty(), Color.RED)),
                new KeyFrame(Duration.seconds(1.0), new KeyValue(s.fillProperty(), Color.YELLOW)),
                new KeyFrame(Duration.seconds(2.0), new KeyValue(s.fillProperty(), Color.LIMEGREEN)));
        t.setCycleCount(2);
        t.setAutoReverse(true);
        t.setOnFinished(e -> s.setFill(original));
        t.play();
    }

    private void updateStatus(double x, double y) {
        Shape s = selected.get();
        lblStatus.setText("  Tọa độ chuột: (%.0f, %.0f) | Tổng số hình: %d | Đang chọn: %s".formatted(
                x, y, drawPane.getChildren().size(), s == null ? "—" : s.getClass().getSimpleName()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
