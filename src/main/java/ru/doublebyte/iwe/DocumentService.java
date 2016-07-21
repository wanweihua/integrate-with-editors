package ru.doublebyte.iwe;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.doublebyte.iwe.repositories.DocumentRepository;
import ru.doublebyte.iwe.types.Document;
import ru.doublebyte.iwe.types.DocumentType;
import ru.doublebyte.iwe.types.DocumentWithData;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            save(storageId, file.getBytes());
        } catch(Exception e) {
            logger.error("Document upload error", e);
            throw new Exception("Document upload error", e);
        }

        try {
            Document savedDocument = documentRepository.save(document);
            logger.info("Saved document with id {}", savedDocument.getId());
        } catch(Exception e) {
            logger.error("Document save error", e);
            throw new Exception("Document save error", e);
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
     * Get document by id
     * @param id Document id
     * @return Document
     */
    public Document get(Long id) {
        try {
            return documentRepository.findOne(id);
        } catch(Exception e) {
            logger.error("Document get error", e);
            return null;
        }
    }

    /**
     * Get document file data
     * @param id Document id
     * @return Document with data
     */
    public DocumentWithData getFile(Long id) {
        try {
            Document document = documentRepository.findOne(id);
            if(document == null) {
                logger.warn("Document not found: {}", id);
                return null;
            }

            Path documentPath = getDocumentPath(document.getStorageId());
            if(!Files.exists(documentPath)) {
                logger.warn("Document file not exist: {}", id);
                return null;
            }

            return new DocumentWithData(document, Files.newInputStream(documentPath));
        } catch(Exception e) {
            logger.error("Document get file error", e);
            return null;
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

    /**
     * Download edited document by URL
     * @param id Document id
     * @param key Editing key (same as storage id)
     * @param url Download URL
     */
    public void download(Long id, String key, String url) throws Exception {
        logger.info("Downloading document: {}", id);

        try {
            Document document = documentRepository.findOne(id);
            if(document == null) {
                throw new Exception("Document not found: " + id);
            }

            if(!document.getStorageId().equals(key)) {
                throw new Exception(String.format("Key %s not equals to storage id %s of document %d",
                        key, document.getStorageId(), id));
            }

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));

            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, request, byte[].class);

            if(!response.getStatusCode().equals(HttpStatus.OK)) {
                throw new Exception("Failed to download document: " + id);
            }

            Path documentPath = getDocumentPath(document.getStorageId());
            Files.write(documentPath, response.getBody());

            logger.info("Document downloaded and saved: {}", id);

            //TODO by document name in URL rename document in DB (e.g. odt -> docx)
        } catch(Exception e) {
            logger.error("Document download error", e);
            throw new Error("Document download error", e);
        }
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
