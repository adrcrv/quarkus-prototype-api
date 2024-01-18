package dev.adrcrv.repository;

import dev.adrcrv.entity.TextManagement;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TextManagementRepository implements PanacheRepository<TextManagement> {
}
