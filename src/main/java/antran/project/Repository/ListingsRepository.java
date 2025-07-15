package antran.project.Repository;

import antran.project.Entity.Listings;
import antran.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListingsRepository extends JpaRepository<Listings, Long> {
    List<Listings> findBySeller(User seller);
}