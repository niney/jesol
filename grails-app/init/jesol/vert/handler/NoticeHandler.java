package jesol.vert.handler;

import com.hazelcast.util.ConcurrentHashSet;
import com.nhncorp.mods.socket.io.SocketIOSocket;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Niney on 2015-06-02.
 */
public class NoticeHandler implements Handler<JsonObject> {

    private SocketIOSocket socket;
    private Handler<SocketIOSocket> handler;
    Class<? extends Handler> cls;

    private static ConcurrentHashSet<SocketIOSocket> hashSet = new ConcurrentHashSet<SocketIOSocket>();

    public NoticeHandler(SocketIOSocket socket, Class<? extends Handler> cls) throws IllegalAccessException, InstantiationException {
        this.socket = socket;
        this.cls = cls;
    }

    @Override
    public void handle(JsonObject jsonObject) {
//        Class<?>[] constructorType = new Class[] {SocketIOSocket.class, JsonObject.class};
//        Constructor<?> constructor = null;
//        Handler handler = null;
//        try {
//            constructor = cls.getConstructor(constructorType);
//            Object[] constructorParams = { socket, jsonObject };
//            handler = (Handler) constructor.newInstance(constructorParams);
//        } catch (Exception e) { }
//
//        handler.handle(hashSet);
    }
}
