package ru.doublebyte.iwe.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.doublebyte.iwe.types.Document;

public interface DocumentRepository extends CrudRepository<Document, Long> {

    Document findByStorageId(String storageId);

}
