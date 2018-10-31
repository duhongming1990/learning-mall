package org.seckill.web;


import org.seckill.dto.Exposer;
import org.seckill.dto.ResultBean;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

   @Autowired
   private SeckillService seckillService;



    @RequestMapping(path = "/list",method = RequestMethod.GET)
    public String list(ModelMap modelMap){
        List<Seckill> seckills = seckillService.getSeckillList();
        modelMap.put("seckills",seckills);
        return "list";
    }



    @RequestMapping(path = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, ModelMap modelMap){
        if(seckillId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if(seckill == null){
            return "forward:seckill/list";
        }
        modelMap.put("seckill",seckill);
        return "detail";
    }



    @RequestMapping(path = "/{seckillId}/exposer",
                method = RequestMethod.GET,
                produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResultBean<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
        return new ResultBean<Exposer>(seckillService.exportSeckillUrl(seckillId));
    }



    @RequestMapping(path = "/{seckillId}/{md5}/execution",
                    method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResultBean<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                @CookieValue(value = "userPhone",required = false) Long phone,
                                                @PathVariable("md5") String md5){
        return new ResultBean<SeckillExecution>(seckillService.executeSeckillProcedure(seckillId,phone,md5));
    }



    @RequestMapping(path = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<Long> time(){
        return new ResultBean<Long>(new Date().getTime());
    }
}
