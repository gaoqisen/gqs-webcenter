package com.gaoqisen.webcenter.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoqisen.webcenter.constant.RedisKeyConstant;
import com.gaoqisen.webcenter.entity.SysRest;
import com.gaoqisen.webcenter.service.SysRestService;
import com.gaoqisen.webcenter.service.SysUserService;
import com.gaoqisen.webcenter.utils.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@Component
public class RestBatchInsertTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SysRestService sysRestService;

    private static Boolean isRuning = false;

    public void beginProcess() {
        while (true) {
            String restInsertInfo = stringRedisTemplate.opsForList().rightPop(RedisKeyConstant.RESTDATASET, 10, TimeUnit.SECONDS);
            if(StringUtils.isBlank(restInsertInfo)) {
                return;
            }
            try{
                List<SysRest> restList = JSON.parseArray(restInsertInfo, SysRest.class);
                for(SysRest sysRest : restList) {
                    sysRest.setCreateTime(new Date());
                    sysRest.setDigest(DigestUtils.getDigest(sysRest.getApplicationName().concat(sysRest.getUrl())));
                    this.sysRestService.saveOrUpdate(sysRest);
                }
                String currentApplicationName = restList.get(0).getApplicationName();
                String currentApplicationRest = stringRedisTemplate.opsForValue().get(currentApplicationName);
                if(currentApplicationRest == null || currentApplicationRest.isEmpty()) {
                    List<SysRest> list = this.sysRestService.list(new QueryWrapper<SysRest>().eq("application_name",currentApplicationName));
                    stringRedisTemplate.opsForValue().set(currentApplicationName, JSON.toJSONString(list));
                }
            }catch(Exception e) {
                logger.error("REST接口批量插入失败: " + restInsertInfo);
                return;
            }
        }
    }

    @Scheduled(cron = "${webcenter.scheduled.cron}")
    public void handleRestBatchInsert(){
        logger.info("Redis消费批量REST新增开始.");
        if(isRuning) {
            return;
        }
        isRuning = true;
        beginProcess();
        isRuning = false;
    }


}