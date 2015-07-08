package common.controller

import common.socket.event.ISocketEvent
import common.socket.NONSocket

/**
 * Created by Niney on 2015-06-04.
 */
class SocketSupportController<T> extends JSONRestfulController<T> {

    protected ISocketEvent<T> iSocketEvent;

    SocketSupportController(Class<T> resource) {
        super(resource)
        prepare(new NONSocket<Void>());
    }

    // Override
    public void prepare(ISocketEvent<T> iSocketEvent) {
        this.iSocketEvent = iSocketEvent;
    }

    // Override
    def save() {
        T instance = super.save();
        iSocketEvent.insertEvent(instance);
    }

    // Override
    def update() {
        T instance = super.update();
        iSocketEvent.updateEvent(instance);
    }

    // Override
    def delete() {
        T instance = super.delete();
        iSocketEvent.deleteEvent(instance);
    }
}
