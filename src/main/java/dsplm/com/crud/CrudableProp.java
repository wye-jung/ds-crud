package dsplm.com.crud;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public interface CrudableProp {
    Strinig name();
    String cname();
    boolean isPK();
}