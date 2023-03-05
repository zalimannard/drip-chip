package ru.zalimannard.dripchip.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByEmail(String email);

    Account findByPassword(String password);

    List<Account> findAllByEmailLikeIgnoreCaseOrderById(String email);

}
