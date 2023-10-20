package io.project.invoiceApp.rowmapper;

import io.project.invoiceApp.domain.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * makes a mapping from Role database object to Java object
 */
public class RoleRowMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Role.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .permission(rs.getString("permissions"))
                .build();
    }
}
