package jesol.vert.handler;

import com.nhncorp.mods.socket.io.SocketIOSocket;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;

/**
 * Created by Niney on 2015-06-02.
 */
public class NoticeUpdating implements Handler {

    private SocketIOSocket socket;
    private JsonObject jsonObject;

    public NoticeUpdating(SocketIOSocket socket, JsonObject jsonObject) {
        this.socket = socket;
        this.jsonObject = jsonObject;
    }

    @Override
    public void handle(Object o) {
        socket.broadcast();
        socket.emit("updating", jsonObject);
    }
}
