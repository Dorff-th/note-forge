package dev.noteforge.knowhub.menu.mapper;

import dev.noteforge.knowhub.menu.dto.MenuTreeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<MenuTreeDTO> getMenuHierarchyByRoles(@Param("roles") List<String> roles);
}
