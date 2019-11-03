
package ai.quod.challenge.metric.dto;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Commit implements Serializable
{

    @SerializedName("sha")
    @Expose
    private String sha;
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("distinct")
    @Expose
    private Boolean distinct;
    @SerializedName("url")
    @Expose
    private String url;
    private final static long serialVersionUID = 5949371072223876349L;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
