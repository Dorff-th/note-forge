package dev.noteforge.knowhub.menu.repository;



import dev.noteforge.knowhub.common.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.noteforge.knowhub.menu.domain.Menu;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

}

