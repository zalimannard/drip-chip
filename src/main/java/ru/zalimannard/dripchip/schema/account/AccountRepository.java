package ru.zalimannard.dripchip.schema.account;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByEmail(String email);

    Account findByPassword(String password);

    @Query("""
            SELECT
                a
            FROM
                Account a
            WHERE
                (:firstName IS NULL OR lower(a.firstName) LIKE lower(concat('%', :firstName, '%')))
            AND
                (:lastName IS NULL OR lower(a.lastName) LIKE lower(concat('%', :lastName, '%')))
            AND
                (:email IS NULL OR lower(a.email) LIKE lower(concat('%', :email, '%')))
            ORDER BY
                a.id
            """)
    List<Account> search(@Param("lastName") String lastName,
                         @Param("firstName") String firstName,
                         @Param("email") String email,
                         Pageable pageable);

}
