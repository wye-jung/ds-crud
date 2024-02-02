package dsplm.com.page.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax servlet.http.HttpServletResponse;

import org.anyframe.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dsplm.com.shared.model.SearchResultDto;
import dsplm.com.page.view.ModuleExcelView;
import dsplm.com.page.model.ModuleExcelDto;
import dsplm.com.page.service.ModulePageService;

import oboe.foundation.frame.controller.OboeController;
import oboe.foundation.frame.model.OboeObject;
import oboe.foundation.frame.model.OboeVo;

public class ModulePageController<T extends OboeVo, F extends OboeObject> extends OboeController {
}