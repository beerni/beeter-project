package edu.upc.eetac.dsa.beeter.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.beeter.BeeterMediaType;
import edu.upc.eetac.dsa.beeter.BeeterRootAPIResource;
import edu.upc.eetac.dsa.beeter.LoginResource;
import edu.upc.eetac.dsa.beeter.StingResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bernat on 10/10/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StingCollection {
    @InjectLinks({
            @InjectLink(resource = BeeterRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "Beeter Root API"),
            @InjectLink(resource = StingResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-stings", title = "Current stings", type = BeeterMediaType.BEETER_STING_COLLECTION),
            @InjectLink(resource = StingResource.class, method = "getStings", style = InjectLink.Style.ABSOLUTE, rel = "next", title = "Newer stings", type = BeeterMediaType.BEETER_STING_COLLECTION, bindings = {@Binding(name = "timestamp", value = "${instance.newestTimestamp}"), @Binding(name = "before", value = "false")}),
            @InjectLink(resource = StingResource.class, method = "getStings", style = InjectLink.Style.ABSOLUTE, rel = "previous", title = "Older stings", type = BeeterMediaType.BEETER_STING_COLLECTION, bindings = {@Binding(name = "timestamp", value = "${instance.oldestTimestamp}"), @Binding(name = "before", value = "true")}),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout")
    })
    private List<Link> links;
    private long newestTimestamp;
    private long oldestTimestamp;
    private List<Sting> stings = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public long getNewestTimestamp() {
        return newestTimestamp;
    }

    public void setNewestTimestamp(long newestTimestamp) {
        this.newestTimestamp = newestTimestamp;
    }

    public long getOldestTimestamp() {
        return oldestTimestamp;
    }

    public void setOldestTimestamp(long oldestTimestamp) {
        this.oldestTimestamp = oldestTimestamp;
    }

    public List<Sting> getStings() {
        return stings;
    }

    public void setStings(List<Sting> stings) {
        this.stings = stings;
    }
}