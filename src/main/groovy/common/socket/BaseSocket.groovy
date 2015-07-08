package common.socket

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import grails.converters.JSON
import jesol.SampleVerticle
import org.vertx.java.core.json.JsonObject

/**
 * Created by Niney on 2015-06-04.
 */
abstract class BaseSocket<T> {

    public static final String SOCKET_DATA = '$socketData'

    SampleVerticle sampleVerticle

    public JsonObject makeJsonObject(T instance, String action, String actionValue) {
        if(instance == null)
            return new JsonObject()
        JsonObject actionObj = new JsonObject().putString(action, actionValue);
        JsonObject jsonObject = new JsonObject();
        jsonObject.putObject(SOCKET_DATA, actionObj);
        jsonObject.putNumber('id', (long)instance.id);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,String> idxDataMap = objectMapper.readValue((instance as JSON).toString(), new TypeReference<HashMap<String,Object>>(){});
        mapToJsonObject(idxDataMap, jsonObject);

        return jsonObject;
    }

    protected mapToJsonObject(Map<String, String> map, JsonObject jsonObject) {
        map.each { k, v ->
            if(v instanceof Number) {
                jsonObject.putNumber(k, v)
            } else if(v instanceof String) {
                jsonObject.putString(k, v)
            } else if(v instanceof Map) {
                JsonObject j = mapToJsonObject(v, new JsonObject())
                jsonObject.putObject(k, j)
            }
        }
        return jsonObject
    }
}
