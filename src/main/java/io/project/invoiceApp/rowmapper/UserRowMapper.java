package io.project.invoiceApp.rowmapper;

import io.project.invoiceApp.domain.Role;
import io.project.invoiceApp.domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * makes a mapping from Role database object to Java object
 */
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .address(rs.getString("address"))
                .phone(rs.getString("phone"))
                .title(rs.getString("title"))
                .bio(rs.getString("bio"))
                .enabled(rs.getBoolean("enable"))
                .isNotLocked(rs.getBoolean("non_locked"))
                .isUsingMfa(rs.getBoolean("using_mfa"))
                .createdAt(rs.getTimestamp("crated_date").toLocalDateTime())
                .build();
    }
}
