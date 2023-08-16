package common.controller;

import griding.service.GridingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class GridingController {

    @Autowired
    GridingService gridingService;

    @PostMapping("griding")
    public String gridRace(@RequestBody String signups) {

        gridingService.gridSignups(signups);
        return "";
    }
}
