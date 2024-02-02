package dsplm.com.crud;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import oboe.foundation.authentication.util.LoginInfoUtil;
import oboe.foundation.frame.service.OboeSimpleService;

@Service
public class CrudService extends OboeSimpleService {

    public <T extends Crudable> int create(T t) throws Exception {
        setValueIfEmpty(t, "createUser", StringUtils.defaultIfEmpty(LoginInfoUtil.getLoginUserId(), "SYSTEM"));
        setValueIfEmpty(t, "divisionCode", LoginInfoUtil.getLoginDivisionCode());
        return oboeDao.insert(CrudUtil.getCrudQueryId(t, "create"), t);
    }
    
    public <T extends Crudable> T read(T t) {
        return (T)oboeDao.getObject(CrudUtil.getCrudQueryId(t, "read"), t);
    }
    
    public <T extends Crudable> int update(T t, String...names) throws Exception {
        Set<String> nameSet = new HashSet<>();
        if(names != null) {
            nameSet.addAll(Arrays.asList(names));
        }

        setValueIfEmpty(t, "updateUser", StringUtils.defaultIfEmpty(LoginInfoUtil.getLoginUserId(), "SYSTEM"));
        nameSet.add("updateUser");
        return oboeDao.update(CrudUtil.getCrudQueryId(t, "update"), CrudUtil.describe(t, nameSet.toArray(new String[nameSet.size()])));
    }
    
    public <T extends Crudable> int updateAll(T t) throws Exception {
        setValueIfEmpty(t, "updateUser", StringUtils.defaultIfEmpty(LoginInfoUtil.getLoginUserId(), "SYSTEM"));
    	return oboeDao.update(CrudUtil.getCrudQueryId(t, "update"), CrudUtil.describeAll(t));
    }

    public <T extends Crudable> int delete(T t) {
        return oboeDao.delete(CrudUtil.getCrudQueryId(t, "delete"), t);
    }

    private <T extends Crudable> void setValueIfEmpty(T t, String name, String value) throws Exception {
        if(PropertyUtils.getProperty(t, name) == null) {
            PropertyUtils.setProperty(t, name, value);
        }
    }

}
