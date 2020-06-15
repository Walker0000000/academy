package com.academy.service.system;

import com.academy.config.DataSource;
import com.academy.config.DataSourceEnum;
import com.academy.entity.SysMenu;
import com.academy.entity.SysUser;
import com.academy.utils.ResultUtil;
import com.academy.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.academy.mapper.CourseMapper;
import com.academy.entity.Course;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Course 表数据服务层接口实现类
 */
@Service("courseService")
public class CourseServiceImpl implements ICourseService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private SysCodeService sysCodeService;

    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil getCourseList(Map<String, Object> params) {
        try {
            List<JSONObject> courseList = null;
            if (StringUtil.isNotEmpty(params.get("page")) && StringUtil.isNotEmpty(params.get("limit"))) {
                Integer curretPage = Integer.parseInt((String) params.get("page"));
                Integer size = Integer.parseInt((String) params.get("limit"));
                Page page = new Page(curretPage, size);
                courseList = courseMapper.getCourseList(page, params);
                //判断是否为空数据
                if (courseList == null || courseList.isEmpty()) {
                    return ResultUtil.success(new ArrayList<>(), 0);
                }
                return ResultUtil.success(courseList, page.getTotal());
            }
            courseList = courseMapper.getCourseList(params);
            if (courseList == null || courseList.isEmpty()) {
                return ResultUtil.success(new ArrayList<>());
            }
            return ResultUtil.success(courseList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("系统异常");
        }
    }

    /**
     * 修改课程状态
     */
    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil updateCourseStatus(JSONObject params) {
        try {
            Integer id = (Integer) params.get("id");
            Course course = courseMapper.selectById(id);
            if (course == null) {
                return ResultUtil.error("不存在该课程");
            }

            course.setStatus(params.getInteger("status"));

            SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
            course.setUpdateUserCode(currentUser.getUserCode());
            course.setUpdateTime(new Date());
            courseMapper.updateById(course);
            return ResultUtil.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("操作失败");
        }
    }

    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil insertCourse(JSONObject menu) {

        try {
            String courseCode = sysCodeService.getSysCode("courseCode");
            if (StringUtil.isEmpty(courseCode)) {
                return ResultUtil.error("获取编码失败");
            }

            String courseName = menu.getString("courseName");
            String classCode = menu.getString("classCode");
            String labelCode = menu.getString("labelCode");
            String ages = menu.getString("ages");
            Integer classHour = menu.getInteger("classHour");
            Integer classTime = menu.getInteger("classTime");
            Map<String, Object> contain_System = new HashMap<>();
            contain_System.put("menu_level", "0");

            if (StringUtil.isEmpty(courseCode) || StringUtil.isEmpty(courseName) || StringUtil.isEmpty(classCode) ||
                    StringUtil.isEmpty(labelCode) || StringUtil.isEmpty(ages) || classHour == null ||
                    classTime == null) {
                return ResultUtil.error("参数缺失");
            }

            SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();

            Course course = new Course();
            course.setCode(courseCode);
            course.setName(courseName);
            course.setClassCode(classCode);
            course.setLabelCode(labelCode);
            course.setAges(ages);
            course.setClassHour(classHour);
            course.setClassTime(classTime);
            course.setStatus(1);
            course.setCreateUserCode(currentUser.getUserCode());
            course.setCreateTime(new Date());
            course.setUpdateUserCode(currentUser.getUserCode());
            course.setUpdateTime(new Date());
            courseMapper.insert(course);
            return ResultUtil.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error();
        }
    }

    @Override
    public JSONObject getCourseByMap(Map<String, Object> contain) {
        List<JSONObject> menuList = courseMapper.getCourseList(contain);
        if (menuList != null && menuList.size() > 0) {
            return menuList.get(0);
        }
        return new JSONObject();
    }

    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil updateCourse(JSONObject menu) {
        try {
            Integer id = menu.getInteger("id");
            QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            List<Course> courseList = courseMapper.selectList(queryWrapper);
            if (courseList == null || courseList.isEmpty()) {
                return ResultUtil.error("未找的对应的课程信息");
            }
            SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
            Course course = courseList.get(0);
            String courseName = menu.getString("courseName");
            if (StringUtil.isNotEmpty(courseName)) {
                course.setName(courseName);
            }
            String classCode = menu.getString("classCode");
            if (StringUtil.isNotEmpty(classCode)) {
                course.setClassCode(classCode);
            }
            String labelCode = menu.getString("labelCode");
            if (StringUtil.isNotEmpty(labelCode)) {
                course.setLabelCode(labelCode);
            }
            String ages = menu.getString("ages");
            if (StringUtil.isNotEmpty(ages)) {
                course.setAges(ages);
            }
            Integer classHour = menu.getInteger("classHour");
            if (StringUtil.isNotEmpty(classHour)) {
                course.setClassHour(classHour);
            }
            Integer classTime = menu.getInteger("classTime");
            if (StringUtil.isNotEmpty(classTime)) {
                course.setClassTime(classTime);
            }
            course.setUpdateUserCode(currentUser.getUserCode());
            course.setUpdateTime(new Date());
            courseMapper.updateById(course);
            return ResultUtil.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error();
        }
    }

}