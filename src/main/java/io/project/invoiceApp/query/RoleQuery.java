package io.project.invoiceApp.query;

/**
 * contains list of SQL queries static strings for UserRoles DB
 */
public class RoleQuery {
    public static final String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM roles WHERE name = :name";
    public static final String INSERT_ROLE_TO_USER_QUERY = "INSERT INTO userRoles (user_id, role_id) VALUES (:userId, :roleId)";
    public static final String SELECT_ROLE_BY_ID_QUERY = "SELECT r.id, r.name, r.permissions FROM roles r " +
            "JOIN userroles ur ON ur.role_id = r.id JOIN users u ON u.id = ur.user_id WHERE u.id = :userId";
}
