package ru.doublebyte.iwe.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request from document editor
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditorRequest {

    @JsonProperty("key")
    private String key;

    @JsonProperty("status")
    private int status;

    @JsonProperty("url")
    private String url;

    ///////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "EditorRequest{" +
                "key='" + key + '\'' +
                ", status=" + status +
                ", url='" + url + '\'' +
                '}';
    }

    ///////////////////////////////////////////////////////////////////////////

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
