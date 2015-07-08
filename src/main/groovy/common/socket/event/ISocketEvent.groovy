package common.socket.event

import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Component

/**
 * Created by Niney on 2015-06-02.
 */
interface ISocketEvent<T> {

    public void insertEvent(T instance);
    public void updateEvent(T instance);
    public void deleteEvent(T instance);

}