package apsh.backend.controller;

import apsh.backend.service.ScheduleService;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ScheduleService scheduleService;
    private final LogFormatter logger;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
        this.logger = new LogFormatterImpl(LoggerFactory.getLogger(OrderController.class));
    }

    @GetMapping(value = "/plan-table")
    public List<SchedulePlanTableOrderVo> getPlanTable(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        logger.infoControllerRequest("GET", "/plan-table", "pageSize=" + pageSize + ", pageNum=" + pageNum);
        List<SchedulePlanTableOrderVo> SPTOVOS = scheduleService.getPlanTable();
        int start = pageSize * (pageNum - 1);
        int end = pageSize * pageNum;
        start = Math.max(start, 0);
        end = Math.min(end, SPTOVOS.size());
        logger.infoControllerResponse("GET", "/plan-table", SPTOVOS.subList(start, end));
        return SPTOVOS.subList(start, end);
    }

    @GetMapping(value = "/order-production-table")
    public List<ScheduleOrderProductionTableRelationVo> getOrderProductionTable(@RequestParam Integer pageSize,
                                                                                @RequestParam Integer pageNum) {
        logger.infoControllerRequest("GET", "/order-production-table", "pageSize=" + pageSize + ", pageNum=" + pageNum);
        List<ScheduleOrderProductionTableRelationVo> SPTRVOS = scheduleService.getOrderProductionTable();
        int start = pageSize * (pageNum - 1);
        int end = pageSize * pageNum;
        start = Math.max(start, 0);
        end = Math.min(end, SPTRVOS.size());
        logger.infoControllerResponse("GET", "/order-production-table", SPTRVOS.subList(start, end));
        return SPTRVOS.subList(start, end);
    }

    @GetMapping(value = "/production-table")
    public List<ScheduleProductionTableProductionVo> getProductionTable(@RequestParam Integer pageSize,
                                                                        @RequestParam Integer pageNum) {
        logger.infoControllerRequest("GET", "/production-table", "pageSize=" + pageSize + ", pageNum=" + pageNum);
        List<ScheduleProductionTableProductionVo> SPTPVOList = scheduleService.getProductionTable();
        int start = pageSize * (pageNum - 1);
        int end = pageSize * pageNum;
        start = Math.max(start, 0);
        end = Math.min(end, SPTPVOList.size());
        logger.infoControllerResponse("GET", "/production-table", SPTPVOList.subList(start, end));
        return SPTPVOList.subList(start, end);
    }

    @GetMapping(value = "/production-resource-table")
    public List<ScheduleProductionResourceTableProductionVo> getProductionResourceTable(@RequestParam Integer pageSize,
                                                                                        @RequestParam Integer pageNum) {
        logger.infoControllerRequest("GET", "/production-resource-table", "pageSize=" + pageSize + ", pageNum=" + pageNum);
        List<ScheduleProductionResourceTableProductionVo> SPRTPVOList = scheduleService.getProductionResourceTable();
        int start = pageSize * (pageNum - 1);
        int end = pageSize * pageNum;
        start = Math.max(start, 0);
        end = Math.min(end, SPRTPVOList.size());
        logger.infoControllerResponse("GET", "/production-resource-table", SPRTPVOList.subList(start, end));
        return SPRTPVOList.subList(start, end);
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