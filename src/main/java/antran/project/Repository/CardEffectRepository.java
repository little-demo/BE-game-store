package antran.project.Repository;

import antran.project.Entity.CardEffect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardEffectRepository extends JpaRepository<CardEffect, Long> {
}