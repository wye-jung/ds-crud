package dsplm.com.crud;

import org.apache.commons.lang3.ArrayUtils;

import oboe.foundation.frame.model.OboeVo;

@SuppressWarnings("serial")
public abstract class Crudable implements OboeVo {

    private String[] modified_props;

    public String[] getModified_props() {
        return ArrayUtils.clone(modified_props);
    }

    public void setModified_props(String[] modified_props) {
        this.modified_props = ArrayUtils.clone(modified_props);
    }

	public int create() throws Exception {
        return CrudUtil.create(this);
    }

    public boolean read() throws Exception {
    	Crudable c = CrudUtil.read(this);
    	if (c != null) {
    		CrudUtil.copy(this, c);
    		return true;
    	}
    	return false;
    }
    
    public int update(CrudableProp... props) throws Exception {
        return CrudUtil.update(this, props);
    }

    public int update() throws Exception {
        return CrudUtil.update(this);
    }

    public int delete() {
        return CrudUtil.delete(this);
    }

    public int save() throws Exception {
        return CrudUtil.save(this);
    }
}
