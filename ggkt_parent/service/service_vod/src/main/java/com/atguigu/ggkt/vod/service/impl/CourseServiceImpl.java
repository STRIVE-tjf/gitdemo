package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.model.vod.CourseDescription;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.vo.vod.*;
import com.atguigu.ggkt.vod.mapper.CourseMapper;
import com.atguigu.ggkt.vod.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-09
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    private ISubjectService iSubjectService;
    @Autowired
    private ITeacherService iTeacherService;
    @Autowired
    private ICourseDescriptionService iCourseDescriptionService;
    @Autowired
    private IChapterService iChapterService;
    @Autowired
    private IVideoService iVideoService;
    @Override
    public Map<String, Object> findPages(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        //根据CourseQueryVo获取条件值
        Long teacherId = courseQueryVo.getTeacherId();//讲师id
        String title = courseQueryVo.getTitle();//课程名称
        Long subjectId = courseQueryVo.getSubjectId();//第二层课程目录id
        Long subjectParentId = courseQueryVo.getSubjectParentId();//第一层课程目录id
        //判断条件非空并进行封装
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(teacherId)){
            queryWrapper.eq("teacher_id",teacherId);
        }
        if (!StringUtils.isEmpty(title)){
            queryWrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("subject_id",subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("subject_parent_id",subjectParentId);
        }
        //查询课程列表
        Page page = baseMapper.selectPage(pageParam, queryWrapper);
        long total = page.getTotal();//表中所有记录数
        long totalPage = page.getPages();//共多少页
        long current = page.getCurrent();//获取当前页码
        List<Course> records = page.getRecords();//当前页所有记录
        records.stream().forEach(item -> {this.getTeacherAndSubjectName(item);});
        //封装获取的数据
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("totalPage",totalPage);
        map.put("current",current);
        map.put("records",records);
        return map;
    }

    private Course getTeacherAndSubjectName(Course course) {
        //查询讲师
        Teacher teacher = iTeacherService.getById(course.getTeacherId());
        if (teacher != null){
            course.getParam().put("teacherName",teacher.getName());
        }
        //获取第一层课程目录名称
        Subject subjectOne = iSubjectService.getById(course.getSubjectParentId());
        if (subjectOne != null){
            course.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }
        //获取第二层课程目录名称
        Subject subjectTwo = iSubjectService.getById(course.getSubjectId());
        if (subjectTwo != null){
            course.getParam().put("subjectTitle",subjectTwo.getTitle());
        }
        return course;
    }

    @Override
    public Long addCourse(CourseFormVo courseFormVo) {
        //创建Course对象
        Course course = new Course();
        //将courseFormVo复制到course中
        BeanUtils.copyProperties(courseFormVo,course);
        //将course添加到数据库中
        baseMapper.insert(course);
        //创建CourseDescription对象
        CourseDescription courseDescription = new CourseDescription();
        //设置courseDescription的值
        courseDescription.setDescription(courseFormVo.getDescription());
        courseDescription.setCourseId(course.getId());
        //执行保存
        iCourseDescriptionService.save(courseDescription);
        return course.getId();
    }
    //根据id获取课程信息
    @Override
    public CourseFormVo getCourseById(Long id) {
        //获取课程基本信息
        Course course = baseMapper.selectById(id);
        if (course == null){
            return null;
        }
        //根据id获取课程描述信息
        CourseDescription courseDescription = iCourseDescriptionService.getById(id);
        //创建CourseFormVo对象
        CourseFormVo courseFormVo = new CourseFormVo();
        //将course内容复制到courseFormVo里面
        BeanUtils.copyProperties(course,courseFormVo);
        if (courseDescription != null){
            courseFormVo.setDescription(courseDescription.getDescription());
        }
        return courseFormVo;
    }

    @Override
    public void updateCourseFormVo(CourseFormVo courseFormVo) {
        //修改课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        baseMapper.updateById(course);
        //修改课程描述信息
        CourseDescription description = new CourseDescription();
        description.setDescription(courseFormVo.getDescription());
        description.setId(course.getId());
        iCourseDescriptionService.updateById(description);
    }

    //根据id查询将要发布的课程的信息,以便做最后的确认
    @Override
    public CoursePublishVo getCoursePublishVo(Long id) {
        return baseMapper.selectCoursePublishVoById(id);

    }
    //课程的最终发布
    @Override
    public void publishCourseById(Long id) {
        Course course = baseMapper.selectById(id);
        course.setStatus(1);//状态1表示成功发布
        course.setCreateTime(new Date());//课程发布时间
        baseMapper.updateById(course);
    }
    //删除课程

    @Override
    public void removeCourse(Long id) {
        //删除小节
        iVideoService.removeByCourseId(id);
        //删除章节
        iChapterService.removeByCourseId(id);
        //删除描述信息
        iCourseDescriptionService.removeById(id);
        //删除课程
        baseMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> findCoursePage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        //获取条件
        String title = courseQueryVo.getTitle();//获取课程标题
        Long teacherId = courseQueryVo.getTeacherId();//获取讲师id
        Long subjectParentId = courseQueryVo.getSubjectParentId();//获取一级课程id
        Long subjectId = courseQueryVo.getSubjectId();//获取二级课程id
        //判断条件并封装
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(title)) {
            queryWrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(subjectId)) {
            queryWrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            queryWrapper.eq("teacher_id",teacherId);
        }
        Page<Course> coursePage = baseMapper.selectPage(pageParam, queryWrapper);
        long total = coursePage.getTotal();//获取总记录数
        long current = coursePage.getCurrent();//获取当前页码
        long pages = coursePage.getPages();//获取总页数
        long size = coursePage.getSize();//获取每页记录数
        List<Course> records = coursePage.getRecords();//获取当前页的所有记录
        records.stream().forEach(item -> {
            this.getTeacherNameAndCourseName(item);
        });
        Map<String,Object> map = new HashMap<>();
        map.put("totalCount",total);
        map.put("totalPage",pages);
        map.put("records",records);
        return map;
    }

    private Course getTeacherNameAndCourseName(Course course) {
        Teacher teacher = iTeacherService.getById(course.getTeacherId());
        if (teacher != null){
            course.getParam().put("teacherName",teacher.getName());
        }
        Subject subjectOne = iSubjectService.getById(course.getSubjectParentId());
        if(subjectOne != null) {
            course.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }
        Subject subjectTwo = iSubjectService.getById(course.getSubjectId());
        if(subjectTwo != null) {
            course.getParam().put("subjectTitle",subjectTwo.getTitle());
        }
        return course;
    }

    @Override
    public Map<String, Object> getInfoByCourseId(Long courseId) {
        //根据Id获取course
        Course course = baseMapper.selectById(courseId);
        //设置浏览量+1
        course.setViewCount(course.getViewCount()+1);
        baseMapper.updateById(course);
        //获取courseVo
        CourseVo courseVo = baseMapper.selectCourseVo(courseId);
        //获取章节小节列表
        List<ChapterVo> chapterVoList = iChapterService.getNetsTreeList(courseId);
        //获取详情
        CourseDescription courseDescription = iCourseDescriptionService.getById(courseId);
        //获取讲师
        Teacher teacher = iTeacherService.getById(courseId);
        Boolean isBuy = false;
        Map<String,Object> map = new HashMap<>();
        map.put("courseVo", courseVo);
        map.put("chapterVoList", chapterVoList);
        map.put("description", null != courseDescription ?
                courseDescription.getDescription() : "");
        map.put("teacher", teacher);
        map.put("isBuy", isBuy);//是否购买
        return map;

    }

    @Override
    public List<Course> findlist() {
        List<Course> list = baseMapper.selectList(null);
        list.stream().forEach(item -> {
            this.getTeacherAndSubjectName(item);
        });
        return list;
    }
}
