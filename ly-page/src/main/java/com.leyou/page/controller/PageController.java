package com.leyou.page.controller;

import com.leyou.page.service.GoodsHtmlService;
import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable(value = "id")Long id, Model model){
        Map<String,Object> map= pageService.getSpuById(id);
        //生成静态页面
        goodsHtmlService.createHtml(id);
        model.addAllAttributes(map);
        return "item";
    }
}
