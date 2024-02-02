package dsplm.com.crud;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oboe.com.ddl.DBInfo;
import oboe.com.ddl.DBTableInfo;
import oboe.fundation.core.idgen.util.IdGenUtil;

@Component
public class CrudUtil {

    private static CrudService crudService;

    @Autowired
    public CrudUtil(CrudService cs) {
        CrudUtil.crudService = cs;
    }

    public static int save(Crudable crudable) throws Exception {
        if(StringUtils.isEmpty(crudable.getId())) {
            crudable.setId(IdGenUtil.getNewId());
            return create(crudable);
        } else if (ArrayUtils.isNotEmpty(crudable.getModified_props())) {
            return update(crudable);
        }
        return 0;
    }

    public static int create(Crudable crudable) {
        return crudService.create(crudable);
    }
    public static <T extends Crudable> T read(T t) {
        return crudService.read(t);
    }
    public static <T extends Crudable> T read(Class<T> cls, Object... keyValues) throws Exception {
        T t = getCrudable(cls);
        List<String> keyNames = getKeyPropertyNames(cls);
        if(keyValue.length == keyNames.size()) {
            PropertyAccessor pa = PropertyAccessorFactory.forDirectFieldAccess(t);
            for(int i=0; i<keyValues.length; i++) {
                pa.setPropertyValue(keyNames(get(i), keyValues[i]);
            }
            return crudService.read(t);
        }
    }
    public static int update(Crudable crudable) throws Exception {
        return crudService.update(crudable, crudable.getModified_props());
    }
    public static int update(Crudable crudable, CrudableProp... props) throws Exception {
        String[] names = new String[props.length];
        for(int i=0; i<props.length; i++) {
            names[i] = props[i].name();
        }
        return crudService.update(crudable, names);
    }
    public static int update(Crudable crudable, String... names) throws Exception {
        return crudService.update(crudable, names);
    }
    public static int updateAll(Crudable crudable) throws Exception {
        return crudService.updateAll(crudable);
    }
    public static int delete(Crudable crudable) {
        return crudService.delete(crudable);
    }
    public static <T extends Crudable> void copy(T dest, T orig) throws Exception {
    	for(String prop : getPropertyNames(orig.getClass())) {
    		PropertyAccessor destAccesor = PropertyAccessorFactory.forDirectFieldAccess(dest);
    		PropertyAccessor origAccesor = PropertyAccessorFactory.forDirectFieldAccess(orig);
            if(origAccesor.isReadableProperty(prop) && destAccesor.isWritableProperty(prop))
    		    destAccesor.setPropertyValue(prop, origAccesor.getPropertyValue(prop));
    	}
    }
    public static <T extends Crudable> T getCrudable(Class<T> cls) throws Exception {
        return cls.getConstructor().newInstance();
    }

    public static <T extends Crudable> Class<? extends Enum<?>> getCrudablePropClass(Class<T> cls) throws Exception {
        Class<T> clz = getEntityClass(cls);
        Class<? extends Enum<?>> c = null;
        for(Class<?> dc: clz.getDeclaredClasses()) {
            if(CrudableProp.class.isAssignableFrom(dc) && dc.isEnum()) {
                c = (Class<? extends Enum<?>>)dc;
            }
        }
        return c;
    }

    public static CrudableProp getCrudableProp(Class<? extends Crudable> cls, String propName) {
    	CrudableProp prop = null;
		try {
			Class<? extends Enum<?>> c = getCrudablePropClass(cls);
			Method m = c.getMethod("valueOf", String.class);
			prop = (CrudableProp)m.invoke(null, propName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
    }
    
    public static <T extends Crudable> List<String> getPropertyNames(Class<T> cls) throws Exception {
        Class<? extends Enum<?>> pc = getCrudablePropClass(cls);
        Enum<?>[] enums = pc.getEnumConstants();
        List<String> names = new ArrayList<>(enums.length);
        for(Enum<?> e: enums) {
            names.add(e.name());
        }
        return names;
    }

    public static <T extends Crudable> List<String> getKeyPropertyNames(Class<T> cls) throws Exception {
        Class<? extends Enum<?>> pc = getCrudablePropClass(cls);
        Enum<?>[] enums = pc.getEnumConstants();
        List<String> names = new ArrayList<>();
        for(Enum<?> e: enums) {
            if(((Crudable)e).isPK()) {
                names.add(e.name());
            }
        }
        return names;
    }
    
    public static <T extends Crudable> boolean hasId(T t) throws Exception {
    	PropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(t);
    	for (String key : getKeyPropertyNames(t.getClass())) {
    		if(accessor.getPropertyValue(key) != null) {
    			return true;
    		}
    	}
    	return false;
    }

    public static <T extends Crudable> Map<String, Object> describe(T t, String... names) {
        Map<String, Object> map = new HashMap<>();
        PropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(t);
        try {
            for(String p: names) {
                if(accessor.isReadableProperty(p))
                    map.put(p, accessor.getPropertyValue(p));
            }
            for(String p: getKeyPropertyNames(t.getClass())) {
                map.put(p, accessor.getPropertyValue(p));
            }
            markTargetProps(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static <T extends Crudable> Map<String, Object> describeAll(T t) {
        Map<String, Object> map = new HashMap<>();
        PropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(t);
        try {
            for(String p: getPropertyNames(t.getClass())) {
                map.put(p, accessor.getPropertyValue(p));
            }
            markTargetProps(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private static void markTargetProps(Map<String, Object> map) {
        String[] keys = map.keySet().toArray(new String[0]);
        for(String key: keys) {
            map.put(key+"_", true);
        }
    }

    public static <T extends Crudable> String getTableName(Class<T> cls) {
        DBTableInfo ann = getEntityClass(cls).getAnnotation(DBTableInfo.class);
        return ann.description();
    }

    public static <T extends Crudable> Class<T> getEntityClass(Class<T> cls) {
        Class<T> clz = cls;
        while(dlz.getAnnotation(DBTableInfo.class) == null) {
            clz = (Class<T>)cls.getSuperclass();
        }
        return clz;
    }

    public static <T extends Crudable> String getCrudQueryId(T t, String crud) {
        Class<?> cls = getEntityClass(t.getClass());
        return cls.getName() +"." +crud;
    }

    public static <T extends Crudable> boolean hasReadAuth(T t) {
        CrudableAuth<T> ca = getCrudableAuth(t);
        return ca != null ? ca.hasReadhAuth(t) : true;
    }

    public static <T extends Crudable> CrudableAuth<T> getCrudableAuth(T t) {
        CrudableAuth<T> ca = null;
        String authClassName  = getEntityClass(t.getClass()).getName().replaceAll("Vo$", "Auth");
        try {
            Class<?> authClass = Class.forName(authClassName);
            ca = (CrudableAuth<T>)authClass.getConstructor().newInstance();
        }catch(Exception e) {
            // e.printStackTrace();
        }
        return ca;
    }

    public static Map<String, Object> getCrudableMeta(Class<? extends Crudable> c) {
        Class<? extends Crudable> ec = getEntityClass(c);
        Map<String, Object> map = new HashMap<>();
        for(Field field: ec.getDeclaredFields()) {
            if(field.isAnnotationPresent(DBInfo.class)) {
                DBInfo dbInfo = field.getAnnotation(DBInfo.class);
                if(dbInfo.isDBField()) {
                    Map<String, Object> meta = new HashMap<>();
                    meta.put("required", !dbInfo.isNull());
                    String colType = dbInfo.colType();
                    if("NVARCHAR2".equals(colType)) meta.put("maxlength", Integer.parseInt(dbInfo.size()));
                    else if("CHAR".equals(colType) || "VARCHAR2".equals(colType)) meta.put("maxbyte", Integer.parseInt(dbInfo.size()));
                    if(Number.class.isAssignableFrom(field.getType())) {
                        meta.put("ptype", "number");
                    } else {
                        meta.put("ptype", field.getType().getSimpleName().toLowerCase());
                    }
                    map.put(field.getName(), meta);
                }
            }
        }
        return map;
    }

}
