package com.sjf.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjf.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 以 Json 格式向前端写回数据工具类。
 * @Author: SJF
 * @Date: 2024/1/14
 **/
@Slf4j
public final class ResponseUtil {
    public static void out(HttpServletResponse response, Result<Object> r) {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            mapper.writeValue(response.getWriter(), r);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }
}
