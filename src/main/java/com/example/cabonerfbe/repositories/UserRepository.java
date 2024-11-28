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

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByFullName(String fullName);

    @Query("select u from Users u where LOWER(u.email) like LOWER(CONCAT('%', :keyword, '%')) or LOWER(u.fullName) like LOWER(CONCAT('%', :keyword, '%'))")
    Page<Users> findByEmailAndFullName(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u " +
            "FROM Users u " +
            "JOIN UserOrganization uo ON u.id = uo.user.id " +
            "WHERE uo.organization.id = :organizationId AND uo.role.name like 'Organization Manager'")
    Users getOwnerOrganization(@Param("organizationId") UUID organizationId);

    @Query("SELECT u FROM Users u WHERE u.email in :userEmail AND u.status = true")
    List<Users> findAllByEmail(@Param("userEmail") List<String> userEmail);

    @Query("SELECT u FROM Users u WHERE u.id = :userId AND u.status = true")
    Optional<Users> findByIdWithStatus(@Param("userId") UUID userId);

    @Query("SELECT u FROM Users u WHERE u.role.name like 'Organization Manager' OR u.role.name like 'LCA Staff'")
    Page<Users> findToInvite(Pageable pageable);

    @Query("SELECT u FROM Users u WHERE (LOWER(u.email) like LOWER(CONCAT('%', :keyword, '%')) or LOWER(u.fullName) like LOWER(CONCAT('%', :keyword, '%')) ) " +
            "AND  u.role.name like 'Organization Manager' " +
            "OR u.role.name like 'LCA Staff'")
    Page<Users> findToInviteByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(u) FROM Users u WHERE MONTH(u.createdAt) = :month AND YEAR(u.createdAt) = :year")
    int countByCreatedAtMonthAndYear(@Param("month") int monthNumber,@Param("year") int currentYear);
}
