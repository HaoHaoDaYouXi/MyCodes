package com.haohaodayouxi.cloud.controller;

import com.haohaodayouxi.cloud.dubbo_service.DemoDubboService;
import com.haohaodayouxi.cloud.feign_service.service.DemoFeignService;
import com.haohaodayouxi.cloud.model.db.DemoId;
import com.haohaodayouxi.cloud.service.DemoService;
import com.haohaodayouxi.cloud.utils.I18nMessageUtils;
import com.haohaodayouxi.common.core.enums.OkResponse;
import com.haohaodayouxi.common.core.model.req.page.PageBaseReq;
import com.haohaodayouxi.common.core.model.res.Response;
import com.haohaodayouxi.common.core.model.vo.page.PageBaseVO;
import com.haohaodayouxi.common.log.annotation.AutoLog;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * HealthController
 *
 * @author TONE
 * @date 2024/8/24
 */
@Slf4j
@RestController
@RequestMapping("/health")
public class HealthController {
    /**
     * 项目名称
     */
    @Value("${spring.application.name:cloud}")
    private String applicationName;

    @Resource
    private DemoService demoService;

    @Resource
    private DemoFeignService demoFeignService;
    @DubboReference
    private DemoDubboService demoDubboService;


    @GetMapping("/h-open/check")
    public String healthCheck() {
        log.info("healthCheck");
        return String.format(applicationName + " is health! %s", LocalDateTime.now());
    }

    @GetMapping("/demoFeignService")
    public String demoFeignService() {
        log.info("demoFeignService");
        return demoFeignService.check();
    }

    @GetMapping("/demoDubboService")
    public String demoDubboService() {
        log.info("demoDubboService");
        return demoDubboService.get();
    }

    @AutoLog(value = "测试日志切面", keepLogServiceName = "TmpKeepLogServiceImpl")
    @PostMapping("/testLog")
    public String testLog(@RequestBody String a) {
        log.info("testLog a:{}", a);
        return String.format(applicationName + " is health! %s", LocalDateTime.now());
    }

    @AutoLog(value = "分页查询DemoId", keepLogServiceName = "TmpKeepLogServiceImpl")
    @PostMapping("/getList")
    public Response<PageBaseVO<DemoId>> getList(@RequestBody PageBaseReq req) {
        log.info("getList req:{}", req);
        return OkResponse.QUERY.toResponse(demoService.getList(req));
    }

    @AutoLog(value = "i18n", keepLogServiceName = "TmpKeepLogServiceImpl")
    @PostMapping("/i18n")
    public Response<String> i18n() {
        String s = I18nMessageUtils.message("test");
        return OkResponse.QUERY.toResponse(s);
    }
}
