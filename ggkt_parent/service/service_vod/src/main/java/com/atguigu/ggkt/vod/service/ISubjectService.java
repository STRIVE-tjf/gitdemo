package com.atguigu.ggkt.vod.service;

import com.atguigu.ggkt.model.vod.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-09
 */
public interface ISubjectService extends IService<Subject> {

    List<Subject> selectSubjectList(Long id);

    void exportDataList(HttpServletResponse response);

    void importDataList(MultipartFile file);
}
