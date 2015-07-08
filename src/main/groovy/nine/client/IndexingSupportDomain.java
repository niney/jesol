package nine.client;

/**
 * Created by Niney on 2015-06-10.
 */
public interface IndexingSupportDomain {

    public void afterInsertIndexing();
    public void afterUpdateIndexing();
    public void afterDeleteIndexing();

}
