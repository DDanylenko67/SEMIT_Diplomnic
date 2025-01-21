package ntukhpi.ddy.semit_diplomnic.service;

import ntukhpi.ddy.semit_diplomnic.entity.Student;
import ntukhpi.ddy.semit_diplomnic.entity.Theme;

import java.util.List;

public interface ThemeService {
    List<Theme> getAllThemes();
    Theme getThemeByNameUA(String name);
    Theme getThemeByNameEN(String name);
    Theme getThemeByStudent(Student student);
    Theme saveTheme(Theme theme);
    Theme updateTheme(Long id, Theme theme);
    void deleteThemeId(Long Id);
}
