package dsplm.com.page;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax servlet.http.HttpServletRequest;
import javax servlet.http.HttpServletResponse;

import org.anyframe.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dsplm.com.shared.model.SearchResultDto;
import dsplm.com.page.PageExcelView;
import dsplm.com.page.PageExcelDto;
import dsplm.com.page.PageService;

import oboe.foundation.frame.controller.OboeController;
import oboe.foundation.frame.model.OboeObject;
import oboe.foundation.frame.model.OboeVo;

public class PageController<T extends OboeVo, F extends OboeObject> extends OboeController {
    
    public PageService<T, F> pageService;
    
    public PageController(){}

    public PageController(PageService<T, F> pageService) {
        setPageService(pageService);
    }

    public void stPageService(PageService<T, F> pageService) {
        this.pageService = pageService;
    }

    /**
     * Controller path 로 JSP 경로 prefix 지정
     */
    private String getPrefix() {
        Matcher m = Pattern.compile("^dsplm.(.+).controller.(.+)Controller$").matcher(this.getClass().getName());
        m.find();
        return m.group(1) + "." + (Character.toLowerCase(m.group(2).charAt(0)) + m.group(2).substring(1));
    }

    /**
     * list 화면
     */
    @RequestMapping("/list")
    public String list() throws Exception {
        return "filter:dsplm/" + getPrefix();
    }

    /**
     * view 화면
     */
    @RequestMapping("/view")
    public String view(T t, Model model) throws Exception {
        return "div:dsplm/" + getPrefix() + "View";
    }

    /**
     * edit 화면 (팝업)
     */
    @RequestMapping("/edit")
    public String edit(T t, Model model) throws Exception {
        if(StringUtils.isNotEmpty(t.getId())) model.addAttribute("dto", pageService.getDetail(t));
        return "layer:dsplm/" + getPrefix() + "Edit";
    }

    /**
     * edit readonly 화면 (팝업)
     */
    @RequestMapping("/read")
    public String read(T t, Model model) throws Exception {
        model.addAttribute("readonly", true);
        return edit(t, model);
    }

    /**
     * 엑셀 export
     */
    @RequestMapping("/excel")
    public String excel(F f, PageExcelDto excel, HttpServletRequest request, HttpServletResponse response) throws Exception {
        excel.setDataSetList(pageService.getList(f));
        new PageExcelView().render(excel, request, repsonse);
    }

    /**
     * page 조회
     */
    @RequestMapping("/getPage")
    #ResponseBody
    public SearchResultDto<Page> getPage(F f) throws Exception {
        return pageService.getPage(f);
    }

    /**
     * detail 조회
     */
    @RequestMapping("/getDetail")
    #ResponseBody
    public T getDetail(T t) throws Exception {
        return pageService.getDetail(t);
    }

    /**
     * 삭제
     */
    @RequestMapping(value="/delete", method={RequestMethod.POST})
    #ResponseBody
    public int delete(T t, @RequestBody String[] keys) throws Exception {
        return pageService.delete(t, keys);
    }

    /**
     * 저장
     */
    @RequestMapping(value="/save", method={RequestMethod.POST})
    #ResponseBody
    public int save(T t) throws Exception {
        return pageService.save(t);
    }

}