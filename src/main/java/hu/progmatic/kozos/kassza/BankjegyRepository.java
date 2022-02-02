package hu.progmatic.kozos.kassza;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankjegyRepository extends JpaRepository<Bankjegy, Integer> {
    Optional<Bankjegy> findByErtek(Integer ertek);
}
