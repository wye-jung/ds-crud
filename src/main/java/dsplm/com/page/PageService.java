package dsplm.com.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anyframe.pagination.Page;
import org.apache.commons.lang3.StringUtils;

import dsplm.com.crud.CrudUtil;
import dsplm.com.crud.Crudable;
import dsplm.com.shared.DsplmSimpleService;
import dsplm.com.shared.model.SearchResultDto;
import oboe.foundation.frame.model.OboeObject;
import oboe.foundation.frame.model.OboeVo;

public class PageService<T extends OboeVo, F extends OboeObject> extends DsplmSimpleService {

    private String pageQueryId;

    public PageService() {}

    public PageService(String pageQueryId) {
        this.setPageQueryId(pageQueryId);
    }

    public void setPageQueryId(String pageQueryId) {
        this.pageQueryId = pageQueryId;
    }
    
    public SearchResultDto<Page> getPage(F f) throws Exception {
        return new SearchResultDto<Page>(getPage(pageQueryId, f, f.getPaging().getPageIndex(), f.getPaging().getPageSize(), f.getPaging().getPageUnit()));
    }

    public List<T> getList(F f) throws Exception {
        return (List<T>)getList(pageQueryId, f);
    }

    public T getDetail(T t) throws Exception {
        if(StringUtils.isEmpty(t.getId())) return null;

        Map<String, Object> idParam = new HashMap<>();
        idParam.put("id", t.getId());
        idParam.put("sorting", t.getSorting());
        return (T)getObject(pageQueryId, idParam);
    }

    public int delete(T t, String... keys) throws Exception {
        int r = 0;
        if(t instanceof Crudable) {
            for(String id: keys) {
                t.setId(id);
                r += CrudUtil.delete((Crudable)t);
            }
        }
        return r;
    }

    public T save(T t) throws Exception {
        return null;
    }

}