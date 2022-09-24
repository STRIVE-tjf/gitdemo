package com.atguigu.ggkt.vod.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.vo.vod.SubjectEeVo;
import com.atguigu.ggkt.vod.listener.SubjectListener;
import com.atguigu.ggkt.vod.mapper.SubjectMapper;
import com.atguigu.ggkt.vod.service.ISubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-09
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements ISubjectService {
    @Autowired
    private SubjectListener subjectListener;
    @Override
    public List<Subject> selectSubjectList(Long id) {
        //查询第一层的目录结构
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",id);
        List<Subject> list = baseMapper.selectList(queryWrapper);
        for (Subject subject :list) {
            Long id1 = subject.getId();
            Boolean hasChildren = this.isHasChildren(id1);
            subject.setHasChildren(hasChildren);
        }
        return list;
    }
    public Boolean isHasChildren(Long childId){
        //查询parent_id=childId
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",childId);
        List list = baseMapper.selectList(queryWrapper);
        System.out.println(list);
        Integer integer = baseMapper.selectCount(queryWrapper);
        return integer>0? true : false;
    }
    //课程列表导出
    @Override
    public void exportDataList(HttpServletResponse response) {
        try {
            //对下载的参数进行设置
            response.setContentType("application/vnd.ms-excel");//下载格式为微软的excel形式
            response.setCharacterEncoding("utf-8");//设置编码格式
            String fileName = URLEncoder.encode("课程列表", "utf-8");//下载时候的文件名,防止中文乱码
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
            //查询课程分类表中的所有数据
            List<Subject> subjectList = baseMapper.selectList(null);
            //创建一个存放SubjectEeVo的列表
            List<SubjectEeVo> subjectEeVoList = new ArrayList<>();
            for (Subject subject :subjectList) {
                SubjectEeVo subjectEeVo = new SubjectEeVo();
                //使用Spring的BeanUtils.copyProperties将subject的内容复制到subjectEeVo里面
                BeanUtils.copyProperties(subject,subjectEeVo);
                subjectEeVoList.add(subjectEeVo);
            }

            //使用EasyExcel将课程分类表中的所有数据写入excel表
            EasyExcel.write(response.getOutputStream(), SubjectEeVo.class)
                    .sheet("课程分类")
                    .doWrite(subjectEeVoList);

        } catch (Exception e) {
            throw new GgktException(20001,"课程列表导出失败");
        }
    }
    //课程列表导入
    @Override
    public void importDataList(MultipartFile file) {
        //读取excel表中的数据
        try {
            EasyExcel.read(file.getInputStream(),SubjectEeVo.class,subjectListener)
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            throw new GgktException(20001,"课程列表导出失败!");
        }
    }
}
