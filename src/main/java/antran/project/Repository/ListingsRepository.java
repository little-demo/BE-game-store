package antran.project.Repository;

import antran.project.Entity.Listings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingsRepository extends JpaRepository<Listings, Long> {
}