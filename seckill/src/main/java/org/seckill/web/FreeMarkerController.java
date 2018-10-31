package org.seckill.web;

import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class FreeMarkerController {

    @Autowired
    private SeckillService seckillService;

    @RequestMapping("list")
    public String list(ModelMap modelMap){
        List<Seckill> seckills = seckillService.getSeckillList();
        modelMap.put("seckills",seckills);
        return "list";
    }
}
