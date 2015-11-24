package edu.upc.eetac.dsa.beeter.entity;
import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.beeter.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;
/**
 * Created by bernat on 10/10/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sting {
    @InjectLinks({
            @InjectLink(resource = BeeterRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "Beeter Root API"),
            @InjectLink(resource = StingResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-stings", title = "Current stings"),
            @InjectLink(resource = StingResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-sting", title = "Create sting", type = BeeterMediaType.BEETER_STING),
            @InjectLink(resource = StingResource.class, method = "getSting", style = InjectLink.Style.ABSOLUTE, rel = "self sting", title = "Sting", type = BeeterMediaType.BEETER_STING, bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
            @InjectLink(resource = UserResource.class, method = "getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", type = BeeterMediaType.BEETER_USER, bindings = @Binding(name = "id", value = "${instance.userid}")),
            @InjectLink(resource = StingResource.class, method = "getStings", style = InjectLink.Style.ABSOLUTE, rel = "next", title = "Newer stings", type = BeeterMediaType.BEETER_STING_COLLECTION, bindings = {@Binding(name = "timestamp", value = "${instance.creationTimestamp}"), @Binding(name = "before", value = "false")}),
            @InjectLink(resource = StingResource.class, method = "getStings", style = InjectLink.Style.ABSOLUTE, rel = "previous", title = "Older stings", type = BeeterMediaType.BEETER_STING_COLLECTION, bindings = {@Binding(name = "timestamp", value = "${instance.creationTimestamp}"), @Binding(name = "before", value = "true")}),
    })



    private List<Link> links;
    private String id;
    private String userid;
    private String creator;
    private String subject;
    private String content;
    private long creationTimestamp;
    private long lastModified;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}