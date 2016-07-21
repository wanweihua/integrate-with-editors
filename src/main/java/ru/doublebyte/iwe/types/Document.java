package ru.doublebyte.iwe.types;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "storage_id")
    private String storageId;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private DocumentType type;

    @Column(name = "edit_date")
    private Date editDate;

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
        return String.format("Document[id=%d, storageId=%s, name=%s, type=%s]",
                id, storageId, name, type);
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

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }
}
