package jesol;

import com.hazelcast.util.ConcurrentHashSet;
import com.nhncorp.mods.socket.io.SocketIOServer;
import com.nhncorp.mods.socket.io.SocketIOSocket;
import com.nhncorp.mods.socket.io.impl.DefaultSocketIOServer;
import groovy.util.logging.Slf4j;
import jesol.vert.DefaultEmbeddableVerticle;
import jesol.vert.model.ActionData;
import org.springframework.stereotype.Component;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Keesun Baik
 */
@Component
public class SampleVerticle extends DefaultEmbeddableVerticle {

	Logger logger = LoggerFactory.getLogger(SampleVerticle.class);

	private SocketIOServer io;
	private ConcurrentHashMap<SocketIOSocket, HashSet<ActionData>> hashMap;

	private Handler<JsonObject> handler;

	public void start(Vertx vertx) {
		HttpServer server = vertx.createHttpServer();
		hashMap = new ConcurrentHashMap<SocketIOSocket, HashSet<ActionData>>();
		io = new DefaultSocketIOServer(vertx, server);
		io.sockets().onConnection(new Handler<SocketIOSocket>() {
			public void handle(final SocketIOSocket socket) {

				// client join
				socket.on("joinRoom", new Handler<JsonObject>() {
					@Override
					public void handle(JsonObject jsonObject) {
						logger.info("joinRoom : name=" + jsonObject.getString("room"));
						String room = jsonObject.getString("room");
						socket.join(room);
						// action
						for(SocketIOSocket key : hashMap.keySet()) {
							HashSet<ActionData> storeActionData =  hashMap.get(key);
							for(ActionData data : storeActionData)
								socket.emit("action", data.getJsonObject());

							logger.info("joinRoom storeAction send socket=" + socket.getId());
						}
					}
				});

				// client leave
				socket.on("leaveRoom", new Handler<JsonObject>() {
					@Override
					public void handle(JsonObject jsonObject) {
						if (hashMap.containsKey(socket)) {
							HashSet<ActionData> storeActionData = hashMap.get(socket);
							for (ActionData data : storeActionData) {
								data.setStatus("end");
								JsonObject storeJson = data.getJsonObject();
								socket.broadcast();
								socket.emit("action", storeJson);
							}
							storeActionData.clear();
							hashMap.remove(socket);
						}
						logger.info("leaveRoom : name=" + jsonObject.getString("room"));
					}
				});

				// client action
				socket.on("action", new Handler<JsonObject>() {
					public void handle(JsonObject msg) {
						ActionData actionData = new ActionData(msg);
						String action = actionData.getAction();
						String status = actionData.getStatus();

						HashSet<ActionData> storeActionData = hashMap.get(socket);

						if (!"".equals(action)) {
							if ("start".equals(status)) {
								if (storeActionData == null) {
									storeActionData = new HashSet<ActionData>();
									storeActionData.add(actionData);
								} else
									storeActionData.add(actionData);
								hashMap.put(socket, storeActionData);
								logger.info("action start : name=" + action);
							} else if ("end".equals(status)) {
								storeActionData.remove(actionData);
								if(storeActionData.size() == 0) {
									hashMap.remove(socket);
								}
								logger.info("action end : name=" + action);
							}
							socket.broadcast();
							socket.emit("action", msg);

							System.out.println("store data size: " + hashMap.size());
						}
					}
				});

				// client disconnection
				socket.onDisconnect(new Handler<JsonObject>() {
					@Override
					public void handle(JsonObject jsonObject) {
						if (hashMap.containsKey(socket)) {
							HashSet<ActionData> storeActionData = hashMap.get(socket);
							for (ActionData data : storeActionData) {
								data.setStatus("end");
								JsonObject storeJson = data.getJsonObject();
								socket.broadcast();
								socket.emit("action", storeJson);
							}
							storeActionData.clear();
							hashMap.remove(socket);
						}
					}
				});
			}
		});

		server.listen(8699);
	}

	public SocketIOServer getIo() {
		return io;
	}
}