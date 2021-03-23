package edu.hufe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SystemController {

    @RequestMapping(value = {"/","index"})
    public String index(){
        return "index";
    }

    @RequestMapping(value = {"/admin","/manage"})
    public String adminHome(){
        return "admin/login";
    }

}
