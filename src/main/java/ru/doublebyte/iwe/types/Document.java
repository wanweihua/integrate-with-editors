package ru.doublebyte.iwe.types;

public class Document {

    private Long id;
    private String storageId;
    private String name;
    private DocumentType type;

    ///////////////////////////////////////////////////////////////////////////

    public Document() {

    }

    public Document(String storageId, String name, DocumentType type) {
        this.storageId = storageId;
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Document{" +
                "storageId='" + storageId + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    ///////////////////////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }
}
