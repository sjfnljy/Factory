package com.sjf.controller.base;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.entity.base.Process;
import com.sjf.entity.base.ProcessRouter;
import com.sjf.service.base.ProcessRouterService;
import com.sjf.vo.base.RouterAssignProcessVo;
import com.sjf.vo.base.RouterQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 工艺路线管理请求处理器
 * @Author: SJF
 * @Date: 2024/1/25 14:59
 */

@Api(tags = "基础数据工艺路线管理接口")
@RestController
@RequestMapping("/factory/base/router")
public class ProcessRouterController {

    @Resource
    private ProcessRouterService processRouterService;

    @GetMapping(value = "/exportData")
    @ApiOperation("导出工艺路线数据")
    public void exportData(HttpServletResponse response) {
        processRouterService.exportData(response);
    }

    @PostMapping("/importData")
    @ApiOperation("导入工艺路线数据")
    public Result<Object> importData(MultipartFile file) {
        return  processRouterService.importData(file);
    }

    @ApiOperation("查询所有工艺路线")
    @GetMapping("/all")
    public Result<List<ProcessRouter>> getAllPath(){
        List<ProcessRouter> pathList = processRouterService.list();
        if(CollUtil.isEmpty(pathList)){
            return Result.build(HttpCode.SUCCESS,"查询结果不存在");
        }
        return Result.ok(pathList);
    }

    @ApiOperation("条件查询并分页返回")
    @GetMapping("/page/{current}/{size}")
    public Result<Page<ProcessRouter>> conditionQuery(@PathVariable("current") Integer current,
                                                      @PathVariable("size") Integer size,
                                                      RouterQueryVo routerQueryVo) {
        return processRouterService.conditionQuery(current, size, routerQueryVo);
    }

    @ApiOperation("根据工艺路线主键 id 查询工艺路线对应工序详细信息")
    @GetMapping("/get/{routerId}")
    public Result<List<Process>> getProcessByRouterId(@PathVariable("routerId") Long routerId){
        return processRouterService.getProcessByRouterId(routerId);
    }

    @ApiOperation("新增工艺路线")
    @PostMapping("/save")
    public Result<Object> savePath(@RequestBody ProcessRouter processRouter){
        return processRouterService.saveProcessRouter(processRouter);
    }

    @ApiOperation("修改工艺路线")
    @PostMapping("/update")
    public Result<Object> updatePath(@RequestBody ProcessRouter processRouter){
        return processRouterService.updateProcessRouter(processRouter);
    }

    @ApiOperation("根据工艺路线 id 删除工艺路线")
    @DeleteMapping("/delete/{routerId}")
    public Result<Object> deletePath(@PathVariable("routerId") Long routerId) {
        return processRouterService.deleteProcessRouter(routerId);
    }

    @ApiOperation(value = "给工艺路线分配工序")
    @PostMapping("/doAssign")
    public Result<Object> doAssign(@RequestBody RouterAssignProcessVo routerAssignProcessVo) {
        return processRouterService.assignProcess(routerAssignProcessVo);
    }
}
