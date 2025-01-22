package ntukhpi.ddy.semit_diplomnic.service.impl;

import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.Theme;
import ntukhpi.ddy.semit_diplomnic.repository.StudentRepository;
import ntukhpi.ddy.semit_diplomnic.repository.ThemeRepository;
import ntukhpi.ddy.semit_diplomnic.service.ThemeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeImpl implements ThemeService {

    private ThemeRepository themeRepository;
    private StudentRepository studentRepository;

    public ThemeImpl(ThemeRepository themeRepository, StudentRepository studentRepository) {
        this.themeRepository = themeRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    @Override
    public Theme getThemeByNameUA(String name) {
        return themeRepository.findByThemeNameUA(name);
    }

    @Override
    public Theme getThemeByNameEN(String name) {
        return themeRepository.findByThemeNameENG(name);
    }

    @Override
    public Theme getThemeByStudent(Student student) {
        Student FindToStudent = studentRepository.findById(student.getId()).orElse(null);
        return FindToStudent.getTheme();
    }

    @Override
    public Theme saveTheme(Theme theme) {
        themeRepository.save(theme);
        Student student =  studentRepository.findByEmail(themeRepository.findByThemeNameUA(theme.getThemeNameUA()).getStudent().getEmail());
        student.setTheme(theme);
        studentRepository.save(student);
        return theme;
    }

    @Override
    public Theme updateTheme(Long id, Theme theme) {
        theme.setId(id);
        return themeRepository.save(theme);
    }

    @Override
    public void deleteThemeId(Long Id) {
        themeRepository.deleteById(Id);
    }
}
