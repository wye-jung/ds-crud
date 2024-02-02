package dsplm.com.page;

import oboe.foundation.excel.model.OboeExcelDto;

public class PageExcelView extends OboeExcelView { // 실제 OboeExcelView 의 메소드들은  private이므로 상속하여 구현할 수 없음.

    public int createHeader(SXSSFWorkbook workbook, Sheet sheet, OboeExcelDto dto, int rowNumber) throws Exception {
        CellStyle headerStyle = OboeExcelStyle.getHeaderStyle(workbook);
        int cellNo = dto.getStartCellIndex();
        List<String[]> headerTitleList = dto.getHeaderTitleList();

        int i = 0;
        for(String[] headerTitle: headerTitleList) {
            if(ArrayUtils.isEmpty(headerTitle)) continue;

            Row row = sheet.createRow(rowNumber + i++);
            for (int j = 0; j < headerTitle.length; j++) {
                Cell cell = row.createCell(j + cellNo);
                // 텍스트 줄바꿈 Cell 확인
                boolean getWrapTextCelStyle = getWrapTextCellStyle(dto, cell);
                if (getWrapTextCellStyle) {
                    headerStyle.setWrapText(true);
                }
                cell.setCellStyle(headerStyle);
                cell.setCellValue(headerTitle[j]);
            }
        }
        return i;
    }

    public void createTitle(SXSSFWorkbook workbook, Sheet sheet, OboeExcelDto dto, int rowNumber) throws Exception {
        
        int cellNo = dto.getStartCellIndex();
        if (dto.getFields().length > 1) 
            sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, cellNo, cellNo + dto.getFields().length - 1));

        Row row = sheet.createRow(rowNumber);
        Cell cell = row.createCell(cellNo);
        cell.setCellStyle(OboeExcelStyler.getTitleStyle(workbook));
        cell.setCellValue(dto.getTitle());
    }

    public void setCellData(OboeExcelDto dto, Cell cell, Object cellData) throws Exception {
        //....
        int[] types = dto.getColumnType();
        int index = cell.getColumnIndex();
        int type = types[index];
        //....
        if(type == PageExcelDto.COLUMN_TYPE_DD) {
            DataDictionaryValueVo ddValueVo = DataDictionaryUtil.getDdValue(((PageExcelDto)dto).getColumnsDDCode()[index], cellData.toString());
            if(ddValueVO != null)
                cellData = Locale.KOREAN.getLanguage().equals(LoginInfoUtil.getLoginUserLanguage()) ? ddValueVo.getNameKo() : ddValueVO.getNameEn();
            cell.setCellValue((String)cellData);
        }
        //...
    }

    public void mergeCellAuto(Sheet sheet, OboeExcelDto dto, int rowNumber) throws Exception {
        //...
        // 값이 있다면 머지대상인지 체크
        //if
        //  for
        //      if(headerTitle[k].length() > 0) { -> if(!cellValue.equals(headerTitle[k])) { 변경
        //....

        //if (mergingCellCount> 0) {
        //  sheet.addMerged....
        //  j += mergingCellCount; 추가
        }
    }

    public void setSheetContent(SXSSFWorkbook workbook, OboeExcelDto dto, int sheetIndex) throws Exception {
        //...
        // int contentRowNo = .... -> 주석
        // ...
        // this.createHeader... -> int hlen = this.createHeader... 변경 
        //...
    }

    public void renderMergedOutputModel(Map<String, Object> mode, HttpServletRequest request, HttpServletResonse response) throws Exception {
        //..
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileNameWithExtension, "UTF-8"));
        //...
    }

    public void render(Object excelDto, HttpServletRequest request, HttpServletResonse response) throws Exception {
        Map<String, Obejct> map = new HashMap<>();
        map.put("excel", excelDto);
        renderMergedOutputModel(map, request, response);
    }
}