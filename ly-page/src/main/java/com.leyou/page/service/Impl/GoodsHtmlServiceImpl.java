package com.leyou.page.service.Impl;

import com.leyou.page.client.GoodsClient;
import com.leyou.page.service.GoodsHtmlService;
import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

@Service
public class GoodsHtmlServiceImpl implements GoodsHtmlService {

    @Autowired
    private PageService pageService;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 创建html静态页
     * @param id
     */
    @Override
    public void createHtml(Long id) {
        PrintWriter printWriter = null;
        try {
            //获取页面数据
            Map<String, Object> spuMap = pageService.getSpuById(id);
            //创建页面上下文
            Context context = new Context();
            //将数据添加入上下文中
            context.setVariables(spuMap);
            //创建输出流
            File file = new File("E:\\leyou-project\\staticHtml\\"+id+".html");
            printWriter = new PrintWriter(file);
            //生成静态页的方法
            templateEngine.process("item",context,printWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            printWriter.close();
        }
    }

    /**
     * 删除静态页面
     * @param id
     */
    @Override
    public void deleteHtml(Long id) {
        File file = new File("E:\\leyou-project\\staticHtml\\"+id+".html");
        file.deleteOnExit();
    }
}
