package wtf.n1zamu.database;

public interface IDataBase {
    void connect();

    void disconnect();

    void wipe();

    void clear();
}
