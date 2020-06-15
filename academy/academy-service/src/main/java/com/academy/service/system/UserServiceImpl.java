package com.academy.service.system;

import com.academy.common.SystemConstant;
import com.academy.config.DataSource;
import com.academy.config.DataSourceEnum;
import com.academy.entity.SysUser;
import com.academy.entity.SysUserRoles;
import com.academy.mapper.SysUserMapper;
import com.academy.mapper.SysUserRolesMapper;
import com.academy.utils.MD5Util;
import com.academy.utils.ResultUtil;
import com.academy.utils.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysCodeService sysCodeService;

    @Autowired
    private SysUserRolesMapper sysUserRolesMapper;

    /**
     * 获取系统用户列表
     */
    @Override
    @DataSource(DataSourceEnum.GET)
    public ResultUtil getUserList(Map<String, Object> params) {
        try {
            List<JSONObject> userList = null;
            //分页查询当前页
            if (StringUtil.isNotEmpty(params.get("page")) && StringUtil.isNotEmpty(params.get("limit"))) {
                Integer curretPage = Integer.parseInt((String) params.get("page"));
                Integer size = Integer.parseInt((String) params.get("limit"));
                Page page = new Page(curretPage, size);
                userList = sysUserMapper.getUserList(page, params);
                //判断是否为空数据
                if (userList == null || userList.isEmpty()) {
                    return ResultUtil.success(new ArrayList<>(), 0);
                }
                return ResultUtil.success(userList, page.getTotal());
            }
            userList = sysUserMapper.getUserList(params);
            //判断是否为空数据
            if (userList == null || userList.isEmpty()) {
                return ResultUtil.success(new ArrayList<>(), 0);
            }
            return ResultUtil.success(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("系统异常");
        }
    }

    /**
     * 新增系统用户
     */
    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil insertUser(JSONObject user) {
        try {
            String userCode = sysCodeService.getSysCode("userCode");
            if (StringUtil.isEmpty(userCode)) {
                return ResultUtil.error("获取编码失败");
            }
            String email = user.getString("email");
            if (StringUtil.isEmpty(email)) {
                return ResultUtil.error("邮箱不能为空");
            }
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", email);
            List<SysUser> userList_old = sysUserMapper.selectList(queryWrapper);
            if (userList_old != null && userList_old.size() > 0) {
                return ResultUtil.error("邮箱重复");
            }
            String userName = user.getString("userName");
            String phone = user.getString("phone");
            String sex = user.getString("sex");
            String status = user.getString("status");
            String password = user.getString("password");
            String userType = user.getString("userType");
            if (StringUtil.isEmpty(userType) || StringUtil.isEmpty(email) || StringUtil.isEmpty(userName) || StringUtil.isEmpty(userCode) ||
                    StringUtil.isEmpty(sex) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(status) || StringUtil.isEmpty(password)) {
                return ResultUtil.error("参数缺失");
            }
            SysUser sysUser = new SysUser();

            sysUser.setLoginName(email);
            sysUser.setEmail(email);
            sysUser.setUserCode(userCode);
            sysUser.setUserName(userName);
            sysUser.setSex(Integer.parseInt(sex));
            sysUser.setPhone(phone);
            sysUser.setStatus(status);
            sysUser.setUserType(userType);
            sysUser.setPassword(MD5Util.encrypt16(password));
            SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
            sysUser.setCreateCode(currentUser.getUserCode());
            sysUser.setCreateTime(new Date());
            sysUserMapper.insert(sysUser);
            return ResultUtil.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error();
        }
    }

    /**
     * 根据条件获取系统用户
     */
    @Override
    @DataSource(DataSourceEnum.GET)
    @Transactional
    public JSONObject getUserByMap(Map<String, Object> contain) {
        List<JSONObject> userList = sysUserMapper.getUserList(contain);
        if (userList != null && userList.size() > 0) {
            return userList.get(0);
        }
        return new JSONObject();
    }

    /**
     * 编辑系统用户
     */
    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil updateUser(JSONObject user) {
        try {
            Integer id = user.getInteger("id");
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            List<SysUser> userList = sysUserMapper.selectList(queryWrapper);
            if (userList == null || userList.isEmpty()) {
                return ResultUtil.error("未找的对应的用户信息");
            }
            SysUser sysUser = userList.get(0);
            String userName = user.getString("userName");
            if (StringUtil.isNotEmpty(userName)) {
                sysUser.setUserName(userName);
            }
            String email = user.getString("email");
            if (StringUtil.isNotEmpty(email)) {
                sysUser.setEmail(email);
            }
            String password = user.getString("password");
            if (StringUtil.isNotEmpty(password)) {
                sysUser.setPassword(MD5Util.encrypt16(password));
            }
            String phone = user.getString("phone");
            if (StringUtil.isNotEmpty(phone)) {
                sysUser.setPhone(phone);
            }
            String userType = user.getString("userType");
            if (StringUtil.isNotEmpty(userType)) {
                sysUser.setUserType(userType);
            }
            String status = user.getString("status");
            if (StringUtil.isNotEmpty(status)) {
                sysUser.setStatus(status);
            }
            Integer sex = user.getInteger("sex");
            if (StringUtil.isNotEmpty(sex)) {
                sysUser.setSex(sex);
            }
            String avatar = user.getString("avatar");
            if (StringUtil.isNotEmpty(avatar)) {
                sysUser.setAvatar(avatar);
            }
            String nickname = user.getString("nickname");
            if (StringUtil.isNotEmpty(nickname)) {
                sysUser.setNickname(nickname);
            }

            SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
            sysUser.setUpdateCode(currentUser.getUserCode());
            sysUser.setUpdateTime(new Date());
            sysUserMapper.updateById(sysUser);
            return ResultUtil.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error();
        }
    }

    /**
     * 获取用户系统角色
     */
    @Override
    @DataSource(DataSourceEnum.GET)
    public JSONObject getUserRoleByMap(Map<String, Object> contain) {

        List<JSONObject> menuList = sysUserMapper.getUserList(contain);
        if (menuList == null || menuList.isEmpty()) {
            return new JSONObject();
        }
        JSONObject user = menuList.get(0);
        String userCode = user.getString("userCode");
        contain.put("userCode", userCode);
        List<JSONObject> userRoleList = sysUserMapper.getUserRoleList(contain);
        if (userRoleList == null || userRoleList.isEmpty()) {
            user.put("userRoleList", new ArrayList<>());
        }
        user.put("userRoleList", userRoleList);
        return user;
    }

    /**
     * 登录用户认证
     */
    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil userAuthorize(JSONObject role) {
        try {
            Integer id = role.getInteger("id");
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            List<SysUser> userList = sysUserMapper.selectList(queryWrapper);
            if (userList == null || userList.isEmpty()) {
                return ResultUtil.error("未找的对应的用户");
            }
            SysUser sysUser = userList.get(0);
            String userCode = sysUser.getUserCode();
            if (StringUtil.isEmpty(userCode)) {
                return ResultUtil.error("用户编码缺失");
            }
            QueryWrapper<SysUserRoles> queryWrapper_roles = new QueryWrapper<>();
            queryWrapper_roles.eq("user_code", userCode);
            sysUserRolesMapper.delete(queryWrapper_roles);
            JSONArray userRoles = role.getJSONArray("checkRoles");
            if (userRoles == null || userRoles.isEmpty()) {
                return ResultUtil.success("授权成功");
            }
            List<SysUserRoles> sysUserRoleList = new ArrayList<>();
            for (Object roleCode : userRoles) {
                SysUserRoles sysUserRoles = new SysUserRoles();
                sysUserRoles.setCreateCode("U000001");
                sysUserRoles.setCreateTime(new Date());
                sysUserRoles.setUserCode(userCode);
                sysUserRoles.setRoleCode(String.valueOf(roleCode));
                sysUserRoles.setStatus("Y");
                sysUserRoleList.add(sysUserRoles);
            }
            if (sysUserRoleList != null && sysUserRoleList.size() > 0) {
                sysUserMapper.insertUserRoleBatch(sysUserRoleList);
            }
            return ResultUtil.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error();
        }
    }

    /**
     * 查询用户下拉框数据
     */
    @Override
    @DataSource(DataSourceEnum.GET)
    public List<JSONObject> getUserTreeList(Map<String, Object> contain) {
        List<JSONObject> userList = sysUserMapper.getUserTreeList(contain);
        if (userList == null || userList.isEmpty()) {
            return new ArrayList<>();
        }
        return userList;

    }

}


