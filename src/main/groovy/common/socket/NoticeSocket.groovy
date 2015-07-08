package common.socket

import common.socket.event.ISocketEvent
import jesol.SampleVerticle

/**
 * Created by Niney on 2015-06-04.
 */
abstract class NoticeSocket<T> extends BaseSocket<T> implements ISocketEvent<T> {

    public static final String NOTICE = "notice"
    public static final String ACTION = "action"

    public NoticeSocket(SampleVerticle sampleVerticle) {
        this.sampleVerticle = sampleVerticle;
    }

    @Override
    void insertEvent(T instance) {
        sampleVerticle.getIo().sockets().emit(NOTICE, makeJsonObject(instance, ACTION, "insert"));
    }

    @Override
    void updateEvent(T instance) {
        sampleVerticle.getIo().sockets().emit(NOTICE, makeJsonObject(instance, ACTION, "update"));
    }

    @Override
    void deleteEvent(T instance) {
        sampleVerticle.getIo().sockets().emit(NOTICE, makeJsonObject(instance, ACTION, "del"));
    }
}
