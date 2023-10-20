package io.project.invoiceApp.repositories.implementation;

import io.project.invoiceApp.domain.Role;
import io.project.invoiceApp.domain.User;
import io.project.invoiceApp.domain.UserPrincipal;
import io.project.invoiceApp.exception.ApiException;
import io.project.invoiceApp.repositories.RoleRepository;
import io.project.invoiceApp.repositories.UserRepository;
import io.project.invoiceApp.rowmapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static io.project.invoiceApp.enumeration.RoleType.ROLE_USER;
import static io.project.invoiceApp.enumeration.VerificationType.ACCOUNT;
import static io.project.invoiceApp.query.UserQuery.*;
import static java.util.Objects.requireNonNull;

/**
 * class for all SQL queries
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User>, UserDetailsService {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public User create(User user) {
        // 1. check if email is unique
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) {
            throw new ApiException("Email is already used.");
        }

        // 2. save new user
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbcTemplate.update(INSERT_USER_QUERY, parameters, holder, new String[]{"id"});

            // generates key and assign it to new user for using this ID on step 3
            user.setId(requireNonNull(holder.getKey()).longValue());

            // 3. add role to the user
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());

            // 4. send verification URL
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());

            // 5. save URL in verification table
            jdbcTemplate.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of("userId", user.getId(), "url", verificationUrl));

            // 6. send email to user with verification URL
            //emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            user.setEnabled(false);
            user.setNotLocked(true);

            // 7. return the newly created user
            return user;

            // if any errors, throw exception with proper message
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Something went wrong... :/ Please try again.");
        }
    }

    @Override
    public Collection<User> list(int page, int pageSize) {
        return null;
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    /**
     * counts amount of emails
     *
     * @param email - email for counting
     * @return
     */
    private Integer getEmailCount(String email) {
        return jdbcTemplate.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    /**
     * creates SQL source for saving to DB
     *
     * @param user - user object
     * @return - SQL parameters
     */
    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", encoder.encode(user.getPassword()));
    }

    /**
     * creates a URL-string for user
     *
     * @param key  - UUID random number
     * @param type - verification type (account or password)
     * @return - URL-string
     */
    private String getVerificationUrl(String key, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify" + type + "/" + key).toUriString();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if (user == null) {
            log.info("User not found in DB");
            throw new UsernameNotFoundException("User not found in DB");
        } else {
            log.info("User found in DB: {}", email);
        }
        return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()).getPermission());
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            User user = jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL_QUERY, Map.of("email", email), new UserRowMapper());
            return user;
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            throw new ApiException("No user found by email: " + email);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Something went wrong... :/ Please try again.");
        }
    }
}
