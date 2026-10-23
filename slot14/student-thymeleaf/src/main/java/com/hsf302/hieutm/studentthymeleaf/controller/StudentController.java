package com.hsf302.hieutm.studentthymeleaf.controller;

import com.hsf302.hieutm.studentthymeleaf.entity.Student;
import com.hsf302.hieutm.studentthymeleaf.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /** Có sẵn trong Model của MỌI request trong controller này → ${majors} */
    @ModelAttribute("majors")
    public Map<String, String> majors() {
        Map<String, String> majors = new LinkedHashMap<>();
        majors.put("SE", "Kỹ thuật phần mềm");
        majors.put("AI", "Trí tuệ nhân tạo");
        majors.put("IA", "An toàn thông tin");
        majors.put("GD", "Thiết kế đồ họa");
        return majors;
    }

    // TODO 4: GET /students?keyword=...
    //  Đưa vào Model:
    //   - "students"      : danh sách sau khi search(keyword)
    //   - "keyword"       : từ khóa (để giữ lại trên ô tìm kiếm)
    //   - "totalStudents" : số sinh viên trong danh sách
    //   - "avgGpa"        : GPA trung bình (studentService.averageGpa)
    //   - "activeMenu"    : "list"  (để tô sáng menu trên navbar)
    //  Trả về view "students/list"
    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        List<Student> students = studentService.search(keyword);

        int totalStudents = students.size();
        double avgGpa = studentService.averageGpa(students);

        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("avgGpa", avgGpa);
        model.addAttribute("activeMenu", "list");

        return "students/list";
    }

    // TODO 5: GET /students/{id}
    //  - Tìm thấy: đưa "student" vào Model, "activeMenu" = "list", trả về "students/detail"
    //  - Không thấy: flash attribute "errorMessage" = "Không tìm thấy sinh viên có id = ..."
    //                rồi redirect:/students
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Student> studentOpt = studentService.findById(id);

        if (studentOpt.isPresent()) {
            model.addAttribute("student", studentOpt.get());
            model.addAttribute("activeMenu", "list");
            return "students/detail";
        }

        ra.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên có id = " + id);
        return "redirect:/students";
    }

    // TODO 6: Hiển thị form
    //  - GET /students/new      : Model "student" = new Student(), "activeMenu" = "new"
    //  - GET /students/{id}/edit: Model "student" = sinh viên tìm được (không thấy → như TODO 5)
    //  Cả 2 trả về view "students/form"
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("activeMenu", "new");
        return "students/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Student> studentOpt = studentService.findById(id);

        if (studentOpt.isPresent()) {
            model.addAttribute("student", studentOpt.get());
            model.addAttribute("activeMenu", "list");
            return "students/form";
        }

        ra.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên có id = " + id);
        return "redirect:/students";
    }

    // TODO 7: POST /students/save
    //  - Thêm @Valid và BindingResult (đặt NGAY SAU tham số student)
    //  - Nếu mã SV bị trùng → result.rejectValue("studentCode", "duplicate", "Mã sinh viên đã tồn tại")
    //  - Có lỗi → trả lại "students/form" (KHÔNG redirect)
    //  - Hợp lệ → lưu, flash "successMessage" = "Thêm/Cập nhật sinh viên <tên> thành công!"
    //             rồi redirect:/students
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("student") Student student,
                       BindingResult result,
                       Model model, RedirectAttributes ra) {

        if (studentService.isStudentCodeDuplicated(student.getStudentCode(), student.getId())) {
            result.rejectValue("studentCode", "duplicate", "Mã sinh viên đã tồn tại");
        }

        if (result.hasErrors()) {
            // Giữ lại trạng thái activeMenu cho navbar tùy thuộc vào hành động thêm mới hay sửa
            model.addAttribute("activeMenu", student.getId() == null ? "new" : "list");
            return "students/form";
        }

        studentService.save(student);

        String action = (student.getId() == null) ? "Thêm" : "Cập nhật";
        ra.addFlashAttribute("successMessage", action + " sinh viên " + student.getFullName() + " thành công!");

        return "redirect:/students";
    }


    // Cho sẵn
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        studentService.deleteById(id);
        ra.addFlashAttribute("successMessage", "Đã xóa sinh viên có id = " + id);
        return "redirect:/students";
    }
}