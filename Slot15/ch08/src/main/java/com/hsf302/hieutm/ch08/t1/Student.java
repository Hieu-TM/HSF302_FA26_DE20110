package com.hsf302.hieutm.ch08.t1;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

/**
 * Model Student biểu diễn theo chuẩn JavaBean JavaFX Property.
 * Lưu ý: grade là thuộc tính dẫn xuất (computed property) được bind tự động từ gpa.
 */
public class Student {
    private final StringProperty studentId = new SimpleStringProperty();
    private final StringProperty fullName = new SimpleStringProperty();
    private final StringProperty className = new SimpleStringProperty();
    private final DoubleProperty gpa = new SimpleDoubleProperty();
    private final ReadOnlyStringWrapper grade = new ReadOnlyStringWrapper();

    public Student(String id, String name, String cls, double gpa) {
        setStudentId(id);
        setFullName(name);
        setClassName(cls);
        setGpa(gpa);
        // Tự động phân loại xếp loại học lực khi gpa thay đổi
        grade.bind(Bindings.createStringBinding(() -> classify(this.gpa.get()), this.gpa));
    }

    public static String classify(double g) {
        if (g >= 9.0) return "Xuất sắc";
        if (g >= 8.0) return "Giỏi";
        if (g >= 6.5) return "Khá";
        if (g >= 5.0) return "Trung bình";
        return "Yếu";
    }

    public String getStudentId() { return studentId.get(); }
    public void setStudentId(String v) { studentId.set(v); }
    public StringProperty studentIdProperty() { return studentId; }

    public String getFullName() { return fullName.get(); }
    public void setFullName(String v) { fullName.set(v); }
    public StringProperty fullNameProperty() { return fullName; }

    public String getClassName() { return className.get(); }
    public void setClassName(String v) { className.set(v); }
    public StringProperty classNameProperty() { return className; }

    public double getGpa() { return gpa.get(); }
    public void setGpa(double v) { gpa.set(v); }
    public DoubleProperty gpaProperty() { return gpa; }

    public String getGrade() { return grade.get(); }
    public ReadOnlyStringProperty gradeProperty() { return grade.getReadOnlyProperty(); }
}
