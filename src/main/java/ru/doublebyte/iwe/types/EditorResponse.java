package ru.doublebyte.iwe.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response to document editor
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditorResponse {

    @JsonProperty("error")
    private int error;

    ///////////////////////////////////////////////////////////////////////////

    public EditorResponse() {

    }

    public EditorResponse(int error) {
        this.error = error;
    }

    ///////////////////////////////////////////////////////////////////////////

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
