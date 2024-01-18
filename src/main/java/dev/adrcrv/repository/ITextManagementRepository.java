package dev.adrcrv.repository;

import dev.adrcrv.entity.TextManagement;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface ITextManagementRepository extends PanacheRepository<TextManagement> {
}
