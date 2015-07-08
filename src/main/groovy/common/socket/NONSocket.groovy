package common.socket

import common.socket.event.ISocketEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by Niney on 2015-06-04.
 */
class NONSocket<T> implements ISocketEvent<T> {

    Logger logger = LoggerFactory.getLogger(NONSocket.class);

    @Override
    void insertEvent(T instance) {
        logger.info("Socket.io not use");
    }

    @Override
    void updateEvent(T instance) {
        logger.info("Socket.io not use");
    }

    @Override
    void deleteEvent(T instance) {
        logger.info("Socket.io not use");
    }
}
