package com.sjf.controller.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjf.common.Result;
import com.sjf.entity.base.Defect;
import com.sjf.service.base.DefectService;
import com.sjf.vo.base.DefectQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 不良品项类型请求处理器
 * @Author: SJF
 * @Date: 2024/1/19 15:35
 */
@Api(tags = "不良品项类型管理接口")
@RestController
@RequestMapping("/factory/base/defect")
public class DefectController {

    @Resource
    private DefectService defectService;

    @GetMapping(value = "/exportData")
    @ApiOperation("导出不良品项数据")
    public void exportData(HttpServletResponse response) {
        defectService.exportData(response);
    }

    @PostMapping("/importData")
    @ApiOperation("导入不良品项数据")
    public Result<Object> importData(MultipartFile file) {
        return  defectService.importData(file);
    }

    @ApiOperation("条件查询并分页返回")
    @GetMapping("/page/{current}/{size}")
    public Result<Page<Defect>> conditionQuery(@PathVariable("current") Integer current,
                                               @PathVariable("size") Integer size,
                                               DefectQueryVo defectQueryVo) {
        return defectService.conditionQuery(current, size, defectQueryVo);
    }

    @ApiOperation("根据 id 查询不良品项详细信息")
    @GetMapping("/get/{defectId}")
    public Result<Defect> getDefectById(@PathVariable("defectId") Long defectId) {
        return defectService.getDefectById(defectId);
    }

    @ApiOperation("新增不良品项")
    @PostMapping("/save")
    public Result<Object> saveDefect(@RequestBody Defect defect){
        return defectService.SaveDefect(defect);
    }

    @ApiOperation("修改不良品项信息")
    @PutMapping("/update")
    public Result<Object> updateDefect(@RequestBody Defect defect){
        return defectService.UpdateDefect(defect);
    }

    @ApiOperation("根据不良品项 id 删除不良品项")
    @DeleteMapping("/delete/{defectId}")
    public Result<Object> deleteDefect(@PathVariable("defectId") Long defectId){
        return defectService.DeleteDefect(defectId);
    }
}
