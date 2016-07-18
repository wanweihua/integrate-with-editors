package ru.doublebyte.iwe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.doublebyte.iwe.types.Document;
import ru.doublebyte.iwe.types.DocumentType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Value("${storage.path}")
    private String storagePath;

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

        //TODO save document record

        try {
            save(storageId, file.getBytes());
        } catch(Exception e) {
            logger.error("Document upload error", e);
            throw new Exception("Document upload error", e);
        }
    }

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
     * Save document in storage
     * @param storageId Id in storage
     * @param data Document data
     */
    protected void save(String storageId, byte[] data) throws Exception {
        ensureStoragePathExists();

        Path storage = Paths.get(storagePath).toAbsolutePath();
        Path documentPath = Paths.get(storagePath, storageId).toAbsolutePath();

        if(!documentPath.startsWith(storage)) {
            throw new Exception("Access violation while saving file with storage id " + storageId);
        }

        Files.write(documentPath, data);

        logger.info("Saved document with storage id {}", storageId);
    }

    ///////////////////////////////////////////////////////////////////////////

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }
}
