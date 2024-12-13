package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface User repository.
 *
 * @author SonPHH.
 */
@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    /**
     * Find by email method.
     *
     * @param email the email
     * @return the optional
     */
    Optional<Users> findByEmail(String email);

    /**
     * Find by full name method.
     *
     * @param fullName the full name
     * @return the optional
     */
    Optional<Users> findByFullName(String fullName);

    /**
     * Find by email and full name method.
     *
     * @param keyword  the keyword
     * @param pageable the pageable
     * @return the page
     */
    @Query("select u from Users u where (u.role.name not like 'System Admin' AND u.role.name not like 'Manager') AND LOWER(u.email) like LOWER(CONCAT('%', :keyword, '%')) or LOWER(u.fullName) like LOWER(CONCAT('%', :keyword, '%'))")
    Page<Users> findByEmailAndFullName(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Gets owner organization.
     *
     * @param organizationId the organization id
     * @return the owner organization
     */
    @Query("SELECT u " +
            "FROM Users u " +
            "JOIN UserOrganization uo ON u.id = uo.user.id " +
            "WHERE uo.organization.id = :organizationId AND uo.role.name like 'Organization Manager'")
    Users getOwnerOrganization(@Param("organizationId") UUID organizationId);

    /**
     * Find all by email method.
     *
     * @param userIds the user ids
     * @return the list
     */
    @Query("SELECT u FROM Users u WHERE u.id in :userIds AND u.status = true")
    List<Users> findAllByEmail(@Param("userIds") List<UUID> userIds);

    /**
     * Find by id with status method.
     *
     * @param userId the user id
     * @return the optional
     */
    @Query("SELECT u FROM Users u WHERE u.id = :userId AND u.status = true")
    Optional<Users> findByIdWithStatus(@Param("userId") UUID userId);

    /**
     * Find to invite method.
     *
     * @param pageable the pageable
     * @return the page
     */
    @Query("SELECT u FROM Users u WHERE u.role.name like 'Organization Manager' OR u.role.name like 'LCA Staff' AND u.status = true")
    Page<Users> findToInvite(Pageable pageable);

    /**
     * Find to invite by keyword method.
     *
     * @param keyword  the keyword
     * @param pageable the pageable
     * @return the page
     */
    @Query("SELECT u FROM Users u WHERE (LOWER(u.email) like LOWER(CONCAT('%', :keyword, '%')) or LOWER(u.fullName) like LOWER(CONCAT('%', :keyword, '%')) ) " +
            "AND u.status = true AND u.role.name like 'Organization Manager' " +
            "OR u.role.name like 'LCA Staff'")
    Page<Users> findToInviteByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Count by created at month and year method.
     *
     * @param monthNumber the month number
     * @param currentYear the current year
     * @return the int
     */
    @Query("SELECT COUNT(u) FROM Users u WHERE MONTH(u.createdAt) = :month AND YEAR(u.createdAt) = :year")
    int countByCreatedAtMonthAndYear(@Param("month") int monthNumber, @Param("year") int currentYear);

    /**
     * Find to invite method.
     *
     * @param pageable the pageable
     * @return the page
     */
    @Query("select u from Users u where (u.role.name not like 'System Admin' AND u.role.name not like 'Manager')")
    Page<Users> findAllExceptAdminAndManager(Pageable pageable);
}
