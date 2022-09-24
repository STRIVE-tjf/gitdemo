package com.atguigu.ggkt.live.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.client.course.CourseFeignClient;
import com.atguigu.ggkt.client.user.UserInfoFeignClient;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.live.mapper.LiveCourseMapper;
import com.atguigu.ggkt.live.mtcloud.CommonResult;
import com.atguigu.ggkt.live.mtcloud.MTCloud;
import com.atguigu.ggkt.live.service.*;
import com.atguigu.ggkt.model.live.*;
import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.utils.DateUtil;
import com.atguigu.ggkt.vo.live.LiveCourseConfigVo;
import com.atguigu.ggkt.vo.live.LiveCourseFormVo;
import com.atguigu.ggkt.vo.live.LiveCourseGoodsView;
import com.atguigu.ggkt.vo.live.LiveCourseVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * <p>
 * 直播课程表 服务实现类
 * </p>
 *
 * @author tianjf
 * @since 2022-09-22
 */
@Service
public class LiveCourseServiceImpl extends ServiceImpl<LiveCourseMapper, LiveCourse> implements ILiveCourseService {
    //远程调用CourseFeignClient接口
    @Autowired
    private CourseFeignClient courseFeignClient;
    @Autowired
    private UserInfoFeignClient userInfoFeignClient;
    @Autowired
    private MTCloud mtCloudClient;
    @Autowired
    private ILiveCourseDescriptionService iLiveCourseDescriptionService;
    @Autowired
    private ILiveCourseAccountService iLiveCourseAccountService;
    @Autowired
    private ILiveCourseConfigService iLiveCourseConfigService;
    @Autowired
    private ILiveCourseGoodsService iLiveCourseGoodsService;
    @Autowired
    private ILiveCourseService iLiveCourseService;

    @Override
    public IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam) {
        IPage<LiveCourse> liveCoursePage = baseMapper.selectPage(pageParam, null);
        //获取当前页所有记录
        List<LiveCourse> liveCourseList = liveCoursePage.getRecords();
        //遍历当前页所有记录
        for (LiveCourse  liveCourse :liveCourseList) {
            //获取讲师id
            Long teacherId = liveCourse.getTeacherId();
            //远程调用接口,根据讲师id获取讲师信息
            Teacher teacherInfo = courseFeignClient.getTeacherInfo(teacherId);
            //获取讲师姓名
            String name = teacherInfo.getName();
            //获取讲师等级
            Integer level = teacherInfo.getLevel();
            //封装数据
            liveCourse.getParam().put("teacherName",name);
            liveCourse.getParam().put("teacherLevel",level);
        }

        return liveCoursePage;
    }

    //添加直播课程
    @Override
    public void saveLive(LiveCourseFormVo liveCourseFormVo) {
        //将liveCourseFormVo --> liveCourse
        LiveCourse liveCourse = new LiveCourse();
        BeanUtils.copyProperties(liveCourseFormVo,liveCourse);
        //调用远程接口,根据liveCourseFormVo里面的getTeacherId获取讲师信息
        Teacher teacher = courseFeignClient.getTeacherInfo(liveCourseFormVo.getTeacherId());
        //调用MTCloud工具类里面的courseAdd添加直播课程
            //封装其他参数
        HashMap<Object, Object> options = new HashMap<Object, Object>();
        options.put("scenes", 2);//直播类型。1: 教育直播，2: 生活直播。默认 1，说明：根据平台开通的直播类型填写
        options.put("password", liveCourseFormVo.getPassword());
            //调用courseAdd方法
        /*
         * 增加一个直播课程
         *
         *  course_name 课程名称
         *  account 发起直播课程的主播账号(这里用讲师id作为主播账号)
         *  start_time 课程开始时间,格式: 2015-01-10 12:00:00
         *  end_time 课程结束时间,格式: 2015-01-10 13:00:00
         nickname 讲师昵称,这里使用讲师姓名
        accountIntro 讲师介绍
        options 其他参数
         * @return
         */
        try {
            String res = mtCloudClient.courseAdd(liveCourseFormVo.getCourseName(),
                    teacher.getId().toString(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacher.getName(),
                    teacher.getIntro(),
                    options
                    );
            System.out.println("***res***: "+res);
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {
                //添加课程基本信息
                JSONObject object = commonResult.getData();
                liveCourse.setCourseId(object.getLong("course_id"));
                baseMapper.insert(liveCourse);
                //添加课程详情信息
                LiveCourseDescription liveCourseDescription = new LiveCourseDescription();
                liveCourseDescription.setDescription(liveCourseFormVo.getDescription());
                liveCourseDescription.setLiveCourseId(liveCourse.getId());
                iLiveCourseDescriptionService.save(liveCourseDescription);
                //添加课程账号信息
                LiveCourseAccount liveCourseAccount = new LiveCourseAccount();
                liveCourseAccount.setLiveCourseId(liveCourse.getId());
                liveCourseAccount.setZhuboAccount(object.getString("bid"));
                liveCourseAccount.setZhuboPassword(liveCourseFormVo.getPassword());
                liveCourseAccount.setAdminKey(object.getString("admin_key"));
                liveCourseAccount.setUserKey(object.getString("user_key"));
                liveCourseAccount.setZhuboKey(object.getString("zhubo_key"));
                iLiveCourseAccountService.save(liveCourseAccount);
            }else {
                String getmsg = commonResult.getmsg();
                throw new GgktException(20001,getmsg);
            }
        } catch (Exception e) {
            throw new GgktException(20001,"添加直播课程失败");
        }

    }
    //删除直播课程
    @Override
    public void deleteCourse(Long id) {
        LiveCourse liveCourse = baseMapper.selectById(id);
        if (liveCourse != null){
            Long courseId = liveCourse.getCourseId();
            try {
                //调用MTCloud里面的方法删除平台里的直播课程
                mtCloudClient.courseDelete(courseId.toString());
                //删除表中的直播数据
                baseMapper.deleteById(id);
            } catch (Exception e) {
                e.printStackTrace();
                throw new GgktException(20001,"删除直播课程失败");
            }
        }
    }
    //根据课程Id获取直播课程基本信息和详情信息
    @Override
    public LiveCourseFormVo getLiveCourseFormVo(Long id) {
        //获取直播课程基本信息
        LiveCourse liveCourse = baseMapper.selectById(id);
        //根据课程Id获取直播课程详情信息
        LiveCourseDescription liveCourseDescription = iLiveCourseDescriptionService.getCourseDescription(id);
        //将直播课程基本信息和详情信息封装到CourseFormVo中
        LiveCourseFormVo liveCourseFormVo = new LiveCourseFormVo();
            //封装基本信息
        BeanUtils.copyProperties(liveCourse,liveCourseFormVo);
            //封装直播课程详情信息
        liveCourseFormVo.setDescription(liveCourseDescription.getDescription());
        return liveCourseFormVo;
    }
    //修改直播课程信息

    @Override
    public void updateLiveCourse(LiveCourseFormVo liveCourseFormVo) {
        //获取直播课程信息
        LiveCourse liveCourse = baseMapper.selectById(liveCourseFormVo.getId());
        //将liveCourseFormVo复制到liveCourse
        BeanUtils.copyProperties(liveCourseFormVo,liveCourse);
        //远程调用接口,获取讲师信息
        Teacher teacherInfo = courseFeignClient.getTeacherInfo(liveCourseFormVo.getTeacherId());
        //调用MTCloud的courseUpdate方法执行平台中的直播信息的修改
        /**
         * 更新课程信息
         *
         *   course_id 课程ID
         *   account 发起直播课程的主播账号
         *   course_name 课程名称
         *   start_time 课程开始时间,格式:2015-01-01 12:00:00
         *   end_time 课程结束时间,格式:2015-01-01 13:00:00
         *   nickname 	主播的昵称
         *   accountIntro 	主播的简介
         *  options 		可选参数
         */
        HashMap<Object,Object> options = new HashMap<>();
        try {
            String res = mtCloudClient.courseUpdate(liveCourse.getId().toString(),
                    teacherInfo.getId().toString(),
                    liveCourse.getCourseName(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacherInfo.getName(),
                    teacherInfo.getIntro(),
                    options
                    );
            //返回结果转换，判断是否成功
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {
                JSONObject object = commonResult.getData();
                liveCourse.setCourseId(object.getLong("course_id"));
                //修改表中直播课程基本信息
                baseMapper.updateById(liveCourse);
                //修改直播课程详情表
                LiveCourseDescription liveCourseDescription = new LiveCourseDescription();
                liveCourseDescription.setDescription(liveCourseFormVo.getDescription());
                liveCourseDescription.setLiveCourseId(liveCourse.getId());
                iLiveCourseDescriptionService.updateById(liveCourseDescription);
            }else {
                throw new GgktException(2001,"修改直播课程详情信息失败");
            }
        } catch (Exception e) {
            throw new GgktException(20001,"修改直播课程信息失败");
        }
    }
    //根据课程id获取直播配置信息并封装

    @Override
    public LiveCourseConfigVo getCourseConfigByCourseId(Long id) {
        LiveCourseConfigVo liveCourseConfigVo = new LiveCourseConfigVo();
        LiveCourseConfig liveCourseConfig = iLiveCourseConfigService.getLiveCourseConfig(id);
        if (liveCourseConfig != null) {
            List<LiveCourseGoods> liveCourseGoods = iLiveCourseGoodsService.getGoods(id);
            BeanUtils.copyProperties(liveCourseConfig,liveCourseConfigVo);
            liveCourseConfigVo.setLiveCourseGoodsList(liveCourseGoods);
        }
        return liveCourseConfigVo;
    }
    //修改直播配置信息
    @Override
    public void updateConfig(LiveCourseConfigVo liveCourseConfigVo) {
        //修改数据表
        LiveCourseConfig liveCourseConfig = new LiveCourseConfig();
        BeanUtils.copyProperties(liveCourseConfigVo,liveCourseConfig);
        if (liveCourseConfigVo.getId() != null){
            iLiveCourseConfigService.save(liveCourseConfig);//如果数据表中没有这个直播配置信息,就新增
        }else {
            iLiveCourseConfigService.updateById(liveCourseConfig);//如果数据表中没有这个直播配置信息,就修改
        }
        //修改直播配置中的商品
        //根据直播课程id删除直播课程配置中的商品
        LambdaQueryWrapper<LiveCourseGoods> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(LiveCourseGoods::getLiveCourseId,liveCourseConfigVo.getLiveCourseId());
        iLiveCourseGoodsService.remove(queryWrapper);
        //根据直播课程id添加直播课程配置中的商品
        if (!CollectionUtils.isEmpty(liveCourseConfigVo.getLiveCourseGoodsList())){
            List<LiveCourseGoods> liveCourseGoodsList = liveCourseConfigVo.getLiveCourseGoodsList();
            iLiveCourseGoodsService.saveBatch(liveCourseGoodsList);
        }
        //修改直播平台中的直播配置信息
        this.updateLifeLiveConfig(liveCourseConfigVo);
    }
    //修改直播平台中的直播配置信息
    private void updateLifeLiveConfig(LiveCourseConfigVo liveCourseConfigVo) {
        //根据直播课程id获取直播配置信息
        LiveCourse liveCourse = iLiveCourseService.getById(liveCourseConfigVo.getLiveCourseId());
        //参数设置
        HashMap<Object,Object> options = new HashMap<Object, Object>();
        //界面模式
        options.put("pageViewMode", liveCourseConfigVo.getPageViewMode());
        //观看人数开关
        JSONObject number = new JSONObject();
        number.put("enable", liveCourseConfigVo.getNumberEnable());
        options.put("number", number.toJSONString());
        //观看人数开关
        JSONObject store = new JSONObject();
        number.put("enable", liveCourseConfigVo.getStoreEnable());
        number.put("type", liveCourseConfigVo.getStoreType());
        options.put("store", number.toJSONString());
        //商城列表
        List<LiveCourseGoods> liveCourseGoodsList = liveCourseConfigVo.getLiveCourseGoodsList();
        if(!CollectionUtils.isEmpty(liveCourseGoodsList)) {
            List<LiveCourseGoodsView> liveCourseGoodsViewList = new ArrayList<>();
            for(LiveCourseGoods liveCourseGoods : liveCourseGoodsList) {
                LiveCourseGoodsView liveCourseGoodsView = new LiveCourseGoodsView();
                BeanUtils.copyProperties(liveCourseGoods, liveCourseGoodsView);
                liveCourseGoodsViewList.add(liveCourseGoodsView);
            }
            JSONObject goodsListEdit = new JSONObject();
            goodsListEdit.put("status", "0");
            options.put("goodsListEdit ", goodsListEdit.toJSONString());
            options.put("goodsList", JSON.toJSONString(liveCourseGoodsViewList));
        }

        try {
            //调用MTCloud的方法执行修改
            String res = mtCloudClient.courseUpdateLifeConfig(liveCourse.getCourseId().toString(), options);
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) != MTCloud.CODE_SUCCESS) {
                throw new GgktException(20001,"修改直播配置信息失败");
            }
        } catch (Exception e) {
            throw new  GgktException(20001,"修改直播配置信息失败");
        }
    }
    //获取最近直播课程

    @Override
    public List<LiveCourseVo> findLatelyList() {
        List<LiveCourseVo> liveCourseVoList = baseMapper.findLatelyList();
        for (LiveCourseVo liveCourseVo :liveCourseVoList) {
            //封装时间
            liveCourseVo.setStartTimeString(new DateTime(liveCourseVo.getStartTime()).toString("yyyy年MM月dd HH:mm"));
            liveCourseVo.setEndTimeString(new DateTime(liveCourseVo.getEndTime()).toString("HH:mm"));
            //封装讲师
            Long teacherId = liveCourseVo.getTeacherId();
            Teacher teacherInfo = courseFeignClient.getTeacherInfo(teacherId);
            liveCourseVo.setTeacher(teacherInfo);
            //封装直播状态
            liveCourseVo.setLiveStatus(this.getLiveStatus(liveCourseVo));
        }
        return liveCourseVoList;
    }
    /**
     * 直播状态 0：未开始 1：直播中 2：直播结束
     * @param liveCourse
     * @return
     */
    private int getLiveStatus(LiveCourse liveCourse) {
        // 直播状态 0：未开始 1：直播中 2：直播结束
        int liveStatus = 0;
        Date curTime = new Date();
        if(DateUtil.dateCompare(curTime, liveCourse.getStartTime())) {
            liveStatus = 0;
        } else if(DateUtil.dateCompare(curTime, liveCourse.getEndTime())) {
            liveStatus = 1;
        } else {
            liveStatus = 2;
        }
        return liveStatus;
    }
    //获取Access_token
    @Override
    public JSONObject getPlayAuth(Long id, Long userId) {
        LiveCourse liveCourse = iLiveCourseService.getById(id);
        UserInfo userInfo = userInfoFeignClient.getById(userId);
        HashMap<Object,Object> options = new HashMap<>();
        try {
            String res = mtCloudClient.courseAccess(liveCourse.getCourseId().toString()
                    , userId.toString(),
                    userInfo.getNickName(),
                    MTCloud.ROLE_USER,
                    80 * 80 * 80,
                    options);
            CommonResult<JSONObject> commonResult = JSONObject.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {
                JSONObject object = (JSONObject) commonResult.getData();
                System.out.println("access::"+object.getString("access_token"));
                return object;
            } else {
                throw new GgktException(20001,"获取失败");
            }
        } catch (Exception e) {
            throw new GgktException(20001,"获取Access_token失败");
        }

    }

    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        LiveCourse liveCourse = this.getById(courseId);
        liveCourse.getParam().put("startTimeString", new DateTime(liveCourse.getStartTime()).toString("yyyy年MM月dd HH:mm"));
        liveCourse.getParam().put("endTimeString", new DateTime(liveCourse.getEndTime()).toString("yyyy年MM月dd HH:mm"));
        Teacher teacher = courseFeignClient.getTeacherInfo(liveCourse.getTeacherId());
        LiveCourseDescription liveCourseDescription = iLiveCourseDescriptionService.getCourseDescription(courseId);

        Map<String, Object> map = new HashMap<>();
        map.put("liveCourse", liveCourse);
        map.put("liveStatus", this.getLiveStatus(liveCourse));
        map.put("teacher", teacher);
        if(null != liveCourseDescription) {
            map.put("description", liveCourseDescription.getDescription());
        } else {
            map.put("description", "");
        }
        return map;
    }
}
