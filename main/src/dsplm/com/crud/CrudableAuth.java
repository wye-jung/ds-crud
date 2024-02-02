package dsplm.com.crud;

public interface CrudableAuth<T extends Crudable> {
    public boolean hasCreateAuth();
    public boolean hasReadAuth(T t);
    public boolean hasUpdateAuth(T t);
    public boolean hasDeleteAuth(T t);
}