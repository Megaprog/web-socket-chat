package wsc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class Index {

    @RequestMapping("/")
    public String index() throws IOException {
        return "index.html";
    }
}
