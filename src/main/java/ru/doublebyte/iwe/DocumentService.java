package ru.doublebyte.iwe;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.doublebyte.iwe.repositories.DocumentRepository;
import ru.doublebyte.iwe.types.Document;
import ru.doublebyte.iwe.types.DocumentType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private DocumentRepository documentRepository;

    @Value("${storage.path}")
    private String storagePath;

    ///////////////////////////////////////////////////////////////////////////

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Handle document upload
     * @param file Document file
     */
    public void upload(MultipartFile file) throws Exception {
        String storageId = UUID.randomUUID().toString();
        Document document = new Document(storageId, file.getOriginalFilename(),
                DocumentType.getByName(file.getOriginalFilename()));

        logger.info("Upload of document {}", document.toString());

        try {
            Document savedDocument = documentRepository.save(document);
            logger.info("Saved document with id {}", savedDocument.getId());
        } catch(Exception e) {
            logger.error("Document save error", e);
            throw new Exception("Document save error", e);
        }

        try {
            save(storageId, file.getBytes());
        } catch(Exception e) {
            logger.error("Document upload error", e);
            throw new Exception("Document upload error", e);
        }
    }

    /**
     * Delete document
     * @param id Document id
     */
    public void delete(Long id) throws Exception {
        logger.info("Delete document with id {}", id);
        try {
            Document document = documentRepository.findOne(id);
            if(document == null) {
                throw new Exception("Document not found: " + id);
            }

            documentRepository.delete(id);
            deleteFile(document.getStorageId());
        } catch(Exception e) {
            logger.error("Document delete error", e);
            throw new Exception("Document delete error", e);
        }
    }

    /**
     * Get all documents
     * @return Documents
     */
    public List<Document> list() {
        List<Document> documents = Lists.newArrayList(documentRepository.findAll());

        Collections.sort(documents, (a, b) -> a.getName().compareTo(b.getName()));

        return documents;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Check whether storage path exists
     */
    protected void ensureStoragePathExists() {
        Path storage = Paths.get(storagePath);
        if(!Files.exists(storage)) {
            try {
                Files.createDirectory(storage);
            } catch(Exception e) {
                logger.error("Failed to create storage directory: " + storagePath, e);
                System.exit(1); //Can't proceed
            }
        }
    }

    /**
     * Get document path by storage id
     * @param storageId Storage id
     * @return Path
     * @throws Exception When storage id is broken
     */
    protected Path getDocumentPath(String storageId) throws Exception {
        ensureStoragePathExists();

        Path storage = Paths.get(storagePath).toAbsolutePath();
        Path documentPath = Paths.get(storagePath, storageId).toAbsolutePath();

        if(!documentPath.startsWith(storage)) {
            throw new Exception("Access violation with storage id " + storageId);
        }

        return documentPath;
    }

    /**
     * Save document in storage
     * @param storageId Id in storage
     * @param data Document data
     */
    protected void save(String storageId, byte[] data) throws Exception {
        Files.write(getDocumentPath(storageId), data);
        logger.info("Saved document with storage id {}", storageId);
    }

    protected void deleteFile(String storageId) throws Exception {
        Files.delete(getDocumentPath(storageId));
        logger.info("Removed document with storage id {}", storageId);
    }

    ///////////////////////////////////////////////////////////////////////////

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }
}
