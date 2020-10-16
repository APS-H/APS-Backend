package apsh.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsh.backend.vo.ScheduleOrderProductionTableRelationVo;
import apsh.backend.vo.SchedulePlanTableOrderVo;
import apsh.backend.vo.ScheduleProductionResourceTableProductionVo;
import apsh.backend.vo.ScheduleProductionTableProductionVo;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {
    @GetMapping(value = "/plan-table")
    public List<SchedulePlanTableOrderVo> getPlanTable(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        // TODO:
        return null;
    }

    @GetMapping(value = "/order-production-table")
    public List<ScheduleOrderProductionTableRelationVo> getOrderProductionTable(@RequestParam Integer pageSize,
            @RequestParam Integer pageNum) {
        // TODO:
        return null;
    }

    @GetMapping(value = "/production-table")
    public List<ScheduleProductionTableProductionVo> getProductionTable(@RequestParam Integer pageSize,
            @RequestParam Integer pageNum) {
        // TODO:
        return null;
    }

    @GetMapping(value = "/production-resource-table")
    public List<ScheduleProductionResourceTableProductionVo> getProductionResourceTable(@RequestParam Integer pageSize,
            @RequestParam Integer pageNum) {
        // TODO:
        return null;
    }

    @GetMapping(value = "/table-export-url/{type}")
    public String getMethodName(@PathVariable Integer type) {
        // TODO:
        return null;
    }

    @GetMapping(value = "/table-export-url/all")
    public String getMethodName() {
        // TODO:
        return null;
    }
}