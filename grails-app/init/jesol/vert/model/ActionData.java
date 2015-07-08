package jesol.vert.model;

import org.vertx.java.core.json.JsonObject;

/**
 * Created by Niney on 2015-06-03.
 */
public class ActionData {

    String id;
    String action;
    String status;

    public ActionData(String id, String action, String status) {
        this.id = id;
        this.action = action;
        this.status = status;
    }

    public ActionData(JsonObject jsonObject) {
        this.id = jsonObject.getValue("id").toString();
        this.action = jsonObject.getValue("action").toString();
        this.status = jsonObject.getValue("status").toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JsonObject getJsonObject() {
        return new JsonObject()
                .putString("id", getId())
                .putString("action", getAction())
                .putString("status", getStatus());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ActionData) {
            return getId().equals(((ActionData) obj).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId().length();
    }
}
