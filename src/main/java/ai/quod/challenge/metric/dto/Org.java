
package ai.quod.challenge.metric.dto;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Org implements Serializable {

    private final static long serialVersionUID = 1543137462601627501L;

    @SerializedName("id")
    public Integer id;

    @SerializedName("login")
    public String login;

    @SerializedName("gravatar_id")
    public String gravatarId;

    @SerializedName("url")
    public String url;

    @SerializedName("avatar_url")
    public String avatarUrl;

}
