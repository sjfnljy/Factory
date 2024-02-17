package com.sjf.service.base.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.component.ExcelListener;
import com.sjf.constant.HttpCode;
import com.sjf.entity.base.Defect;
import com.sjf.exception.CustomException;
import com.sjf.mapper.base.DefectMapper;
import com.sjf.mapper.base.ProcessAndDefectMapper;
import com.sjf.service.base.DefectService;
import com.sjf.utils.PrefixGenerateUtil;
import com.sjf.vo.base.DefectQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 不良品项类型 Service 接口实现类
 * @Author: SJF
 * @Date: 2024/1/19 15:38
 */
@Service
@Slf4j
public class DefectServiceImpl extends ServiceImpl<DefectMapper, Defect> implements DefectService {

    @Resource
    private DefectMapper defectMapper;

    @Resource
    private ProcessAndDefectMapper processAndDefectMapper;

    @Override
    public void exportData(HttpServletResponse response) {
        try {
            // 设置响应结果类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            // 这里 URLEncoder.encode 可以防止中文乱码
            String fileName = URLEncoder.encode("不良品项数据", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 查询数据库中的数据。
            List<Defect> defectList = defectMapper.selectList(null);
            // 写出数据到浏览器端。
            EasyExcel.write(response.getOutputStream(), Defect.class).sheet("不良品项数据").doWrite(defectList);
        }catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public Result<Object> importData(MultipartFile file) {
        ExcelListener<Defect> listener = new ExcelListener<>(defectMapper);
        // 调用 read 方法读取 excel 数据。
        try {
            EasyExcel.read(file.getInputStream(),
                    Defect.class,
                    listener).sheet().doRead();
        } catch (IOException e) {
            throw new CustomException(HttpCode.FILE_IMPORT_FAILURE,"导入 Excel 数据失败");
        }
        return Result.build(HttpCode.SUCCESS, "导入 Excel 数据成功");
    }

    @Override
    public Result<Page<Defect>> conditionQuery(Integer current, Integer size, DefectQueryVo defectQueryVo) {
        // 判断是否传入分页查询参数以及条件查询对象。
        if(defectQueryVo == null || StrUtil.isBlank(String.valueOf(current))
                || StrUtil.isBlank(String.valueOf(size))){
            Result.build(HttpCode.UNPASSED_PARAMETER,"传入的查询条件为空");
        }
        // 判断传入的分页查询参数是否合法，至少不能小于等于 0。
        if(current <= 0 || size <= 0){
            return Result.build(HttpCode.INVALID_PARAMETER,"传入的分页参数为不合法");
        }

        // 获取传输的查询条件。
        String defectName = Objects.requireNonNull(defectQueryVo).getDefectName();
        String defectCode = Objects.requireNonNull(defectQueryVo).getDefectCode();
        LambdaQueryWrapper<Defect> wrapper = new LambdaQueryWrapper<>();

        // 不良品项名称模糊查询、不良品项编号精确匹配，优先显示近期被修改过的不良品项。。
        wrapper.like(StrUtil.isNotBlank(defectName),Defect::getDefectName,defectName)
                .eq(StrUtil.isNotBlank(defectCode),Defect::getDefectCode,defectCode)
                .orderByDesc(Defect::getUpdateTime);

        // 封装返回结果并返回结果信息。
        Page<Defect> page = new Page<>(current, size);
        Page<Defect> defectPage = defectMapper.selectPage(page, wrapper);
        if(defectPage == null){
            return Result.build(HttpCode.SUCCESS,"条件查询结果不存在");
        }
        return Result.ok(defectPage);
    }

    @Override
    public Result<Defect> getDefectById(Long defectId) {
        if (StrUtil.isBlank(String.valueOf(defectId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的不良品项 id 为空");
        }

        // 校验传入的不良品项 id 对应的对象是否存在。
        Defect defect = defectMapper.getDefectById(defectId);
        if (defect == null) {
            return Result.build(HttpCode.SUCCESS,"不良品项不存在");
        }
        return Result.ok(defect);
    }

    @Override
    public Result<Object> SaveDefect(Defect defect) {
        if(defect == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的不良品项对象不存在");
        }

        String defectName = defect.getDefectName();
        if (StrUtil.isBlank(defectName)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的不良品项名称不能为空");
        }else {
            Defect quiredDefect = defectMapper.getDefectByName(defectName);
            if (quiredDefect != null){
                return Result.build(HttpCode.ADD_FAILURE, "传入的不良品项名称对应的不良品项已存在");
            }
        }

        // 校验传输的不良品项编号问题。
        String defectCode = defect.getDefectCode();
        if (StrUtil.isBlank(defectCode)){
            String generateCode = PrefixGenerateUtil.generateCode(defectName);
            defect.setDefectCode(generateCode);
        }else{
            Defect quiredDefect = defectMapper.getDefectByCode(defectCode);
            if (quiredDefect != null){
                return Result.build(HttpCode.ADD_FAILURE, "传入的不良品项编号对应的不良品项已存在");
            }
        }

        // 校验传输的不良品项数量信息。
        Integer defectQuantity = defect.getQuantity();
        if (defectQuantity == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的不良品项数量信息不能为空");
        }else if (defectQuantity < 0){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的不良品项数量信息不能小于 0");
        }

        // 校验传输的不良品项成因信息。
        String defectCause = defect.getDefectCause();
        if (StrUtil.isBlank(defectCause)) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的不良品项成因信息不能为空");
        }

        // 校验通过则插入数据库中。
        defectMapper.insert(defect);
        return Result.build(HttpCode.SUCCESS, "新增不良品项成功");
    }

    @Override
    public Result<Object> UpdateDefect(Defect defect) {
        if(defect == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的不良品项对象不存在");
        }

        // 获取传输的不良品项信息并校验传输的不良品项对象 id 是否存在。
        Long defectId = defect.getId();
        if (StrUtil.isBlank(String.valueOf(defectId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的修改的不良品项 id 不能为空");
        } else {
            // id 存在则校验数据库中是否存在对应的角色对象。
            Defect quiredDefect = defectMapper.getDefectById(defectId);
            if (quiredDefect == null) {
                return Result.build(HttpCode.MODIFICATION_FAILURE,"不存在该不良品项 id 对应的不良品项，无法进行修改");
            }
        }

        // 校验通过则进行修改。
        defectMapper.updateDefect(defect);
        return Result.build(HttpCode.SUCCESS, "修改不良品项成功");
    }

    @Override
    public Result<Object> DeleteDefect(Long defectId) {
        if(StrUtil.isBlank(String.valueOf(defectId))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的删除的不良品项 id 不能为空");
        }

        // 校验传输的 id 是否存在：
        if(StrUtil.isBlank(String.valueOf(defectId))){
            return Result.build(HttpCode.DELETION_FAILURE, "删除不良品项时必须传输修改的不良品项 id");
        }else {
            // 校验不良品项 id 对应的不良品项对象是否存在。
            Defect quiredDefect = defectMapper.getDefectById(defectId);
            if(quiredDefect== null){
                return Result.build(HttpCode.DELETION_FAILURE, "删除的不良品项对象不存在");
            }
        }

        // 校验是否有工序仍在使用不良品项，若有则不能删除。
        List<Long> processIdList =  processAndDefectMapper.getProcessIdByDefectId(defectId);
        if (CollUtil.isNotEmpty(processIdList)){
            return Result.build(HttpCode.DELETION_FAILURE, "仍有工序在使用该不良品项，无法删除");
        }

        // 校验通过则进行删除。
        defectMapper.deleteDefect(defectId);
        return Result.build(HttpCode.SUCCESS, "删除不良品项成功");
    }
}
