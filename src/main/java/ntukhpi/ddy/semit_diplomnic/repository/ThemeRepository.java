package ntukhpi.ddy.semit_diplomnic.repository;

import ntukhpi.ddy.semit_diplomnic.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Theme findThemeById(Long id);
    Theme findByThemeNameUA(String themeNameUA);
    Theme findByThemeNameENG(String themeNameUA);
    List<Theme> findThemesBySupervisorId(long supervisorId);
}
