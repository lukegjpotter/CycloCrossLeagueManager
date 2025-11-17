package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import org.springframework.stereotype.Service;

@Service
public class RaceCategoryNameService {

    public String aRace() {
        return "A-Race";
    }

    public String bRace() {
        return "B-Race";
    }

    public String women() {
        return "Women";
    }

    public String u16Male() {
        return "Under 16s Male";
    }

    public String u16Female() {
        return "Under 16s Female";
    }

    public String u14Male() {
        return "Under 14s Male";
    }

    public String u14Female() {
        return "Under 14s Female";
    }

    public String u12Male() {
        return "Under 12s Male";
    }

    public String u12Female() {
        return "Under 12s Female";
    }
}
