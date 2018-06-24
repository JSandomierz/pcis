package pl.sanszo.pcis;


public interface ActorAction<T,K> {
    public void commenceOperation(T me, K him);
}
