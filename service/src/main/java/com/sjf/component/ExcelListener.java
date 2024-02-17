package com.sjf.component;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Getter;

import java.util.List;

/**
 * @Description: Excel 监听器
 * @Author: SJF
 * @Date: 2024/1/19 16:52
 */
@Getter
public class ExcelListener<T> implements ReadListener<T> {

    private static final int BATCH_COUNT = 100;

    private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private BaseMapper<T> mapper;
    public ExcelListener(BaseMapper<T> mapper) {
        this.mapper = mapper;
    }
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        cachedDataList.add(t);
        // 达到 BATCH_COUNT 了，需要去存储一次数据库，防止数据几万条数据在内存，容易 OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        cachedDataList.clear();
    }

    private void saveData() {
        for (T t : cachedDataList) {
            mapper.insert(t);
        }
    }
}
