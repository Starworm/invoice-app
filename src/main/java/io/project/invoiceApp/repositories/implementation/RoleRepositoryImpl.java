package io.project.invoiceApp.repositories.implementation;

import io.project.invoiceApp.domain.Role;
import io.project.invoiceApp.exception.ApiException;
import io.project.invoiceApp.repositories.RoleRepository;
import io.project.invoiceApp.rowmapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

import static io.project.invoiceApp.enumeration.RoleType.ROLE_USER;
import static io.project.invoiceApp.query.RoleQuery.*;
import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
@Slf4j
@Repository
public class RoleRepositoryImpl implements RoleRepository<Role> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Role create(Role data) {
        return null;
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        return null;
    }

    @Override
    public Role get(Long id) {
        return null;
    }

    @Override
    public Role update(Role data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("Adding role {} to user id {}", roleName, userId);

        try {
            // getting role from DB
            Role role = jdbcTemplate.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("name", roleName), new RoleRowMapper());
            // assigning role to user
            jdbcTemplate.update(INSERT_ROLE_TO_USER_QUERY, Map.of("userId", userId, "roleId", requireNonNull(role).getId()));
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No role found by name: " + ROLE_USER.name());
        } catch (Exception exception) {
            throw new ApiException("Something went wrong... :/ Please try again.");
        }
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        log.info("Adding role for user id {}", userId);

        try {
            // getting role from DB
            return jdbcTemplate.queryForObject(SELECT_ROLE_BY_ID_QUERY, Map.of("userId", userId), new RoleRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No role found by name: " + ROLE_USER.name());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Something went wrong... :/ Please try again.");
        }
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        return null;
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {

    }
}
