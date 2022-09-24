package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.Chapter;

import com.atguigu.ggkt.model.vod.Video;
import com.atguigu.ggkt.vo.vod.ChapterVo;
import com.atguigu.ggkt.vo.vod.VideoVo;
import com.atguigu.ggkt.vod.mapper.ChapterMapper;
import com.atguigu.ggkt.vod.service.IChapterService;
import com.atguigu.ggkt.vod.service.IVideoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-09
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements IChapterService {
    //注入videoService属性
    @Autowired
    private IVideoService iVideoService;
    @Override
    public List<ChapterVo> getNetsTreeList(Long courseId) {
        //创建一个最终的List来存放最后封装好的数据并返回
        List<ChapterVo> finalChapterVoList = new ArrayList<>();
        //获取章节列表
        // 1.封装条件构造器
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",courseId);
        // 2.获取章节列表
        List<Chapter> chapters = baseMapper.selectList(chapterQueryWrapper);
        //获取小节列表
        // 1.封装条件构造器
        LambdaQueryWrapper<Video> videoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        videoLambdaQueryWrapper.eq(Video::getCourseId,courseId);
        // 2.获取小节列表
        List<Video> videoList = iVideoService.list(videoLambdaQueryWrapper);
        //封装章节列表
        // 1.遍历章节列表
        for (Chapter chapter :chapters) {
            //将chapter复制到chapterVo里面
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            finalChapterVoList.add(chapterVo);
            //遍历videoList
            List<VideoVo> videoVoList = new ArrayList<>();
            for (Video video :videoList) {
                //获取chapter的id和video的chapter_id,两者一致就表名这个小节列表是属于这个章节的
                if (chapter.getId().equals(video.getChapterId())){
                    //将video复制到videoVo
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video,videoVo);
                    //添加到videoVoList中
                    videoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoList);
        }

        //封装小结列表
        return finalChapterVoList;
    }
    //删除章节信息

    @Override
    public void removeByCourseId(Long id) {
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",id);
        baseMapper.delete(chapterQueryWrapper);
    }
}
