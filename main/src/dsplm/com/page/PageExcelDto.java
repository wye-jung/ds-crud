package dsplm.com.page;

import oboe.foundation.excel.model.OboeExcelDto;

public class PageExcelDto extends OboeExcelDto {
    private static final long serialVersionUID = 1L;

    public static final int COLUMN_TYPE_DD = 11;
    public static final int COLUMN_TYPE_IMAGE_FILE = 13;

    public String columnsDDCode;

    public String[] getColumnsDDCode() {
        return columnsDDCode;
    }
    public void setColumnsDDCode(String[] columnsDDCode) {
        this.columnsDDCode = columnsDDCode;
    }
}