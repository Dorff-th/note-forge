package dev.noteforge.knowhub.menu.repository;

import dev.noteforge.knowhub.menu.domain.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void save_and_find_menu() {
        Menu menu = Menu.builder()
                .name("회원관리")
                .path("/admin/members")
                .role(Menu.Role.ADMIN)
                .sortOrder(1)
                .build();

        Menu saved = menuRepository.save(menu);

        assertThat(saved.getId()).isNotNull();

        List<Menu> all = menuRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("회원관리");
    }
}