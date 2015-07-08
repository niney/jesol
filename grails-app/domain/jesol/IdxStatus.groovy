package jesol

/**
 * Created by Niney on 2015-06-10.
 */
public enum IdxStatus {
    INSERT('i'),
    UPDATE('u'),
    DEL('d'),
    SET('s'),
    RESET('r')

    String id

    private IdxStatus(String id) {
        this.id = id
    }

    String toString() {id}
}