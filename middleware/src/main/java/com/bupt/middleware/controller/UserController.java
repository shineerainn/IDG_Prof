package com.bupt.middleware.controller;

import com.bupt.middleware.entity.UserPwd;
import com.bupt.middleware.entity.UserRole;
import com.bupt.middleware.entity.result.Result;
import com.bupt.middleware.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

/**
 * @author chenxiao
 * @date 2025/4/1 00:11
 * @description: Functions of User - 修复Result.success调用错误
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    // 用户登录验证接口
    @PostMapping("/login")
    public Result<String> login(@RequestParam String userId, @RequestParam String password,
                                HttpServletRequest request, HttpServletResponse response) {
        log.info("用户登录尝试: {}", userId);

        try {
            // 验证用户密码
            Result<UserPwd> pwdResult = userService.getPwd(userId);

            if (pwdResult.getCode() != 200 || pwdResult.getData() == null) {
                log.warn("用户不存在: {}", userId);
                return Result.error(400, "用户不存在");
            }

            String dbPassword = pwdResult.getData().getUserPwd();
            log.info("用户: {}, 密码验证: {}", userId, dbPassword.equals(password));

            if (!dbPassword.equals(password)) {
                log.warn("密码错误 - 用户: {}", userId);
                return Result.error(401, "密码错误");
            }

            // === 强制Session管理 ===

            // 1. 销毁现有Session（如果存在）
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                log.info("销毁现有Session: {}", oldSession.getId());
                oldSession.invalidate();
            }

            // 2. 创建新Session
            HttpSession session = request.getSession(true); // 强制创建新Session
            log.info("创建新Session: {}", session.getId());

            // 3. 设置Session属性
            session.setAttribute("userId", userId);
            session.setAttribute("loginTime", System.currentTimeMillis());
            session.setMaxInactiveInterval(24 * 60 * 60); // 24小时

            // 4. 强制设置Cookie（确保浏览器能收到）
            Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(24 * 60 * 60);  // 24小时
            sessionCookie.setHttpOnly(true);
            sessionCookie.setSecure(false); // 如果是HTTPS设为true
            response.addCookie(sessionCookie);

            // 5. 设置响应头确保Session正确传递
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");

            // 6. 验证Session是否正确创建
            String savedUserId = (String) session.getAttribute("userId");
            log.info("=== Session创建验证 ===");
            log.info("Session ID: {}", session.getId());
            log.info("保存的用户ID: {}", savedUserId);
            log.info("Session创建时间: {}", new java.util.Date(session.getCreationTime()));
            log.info("Session最大不活动间隔: {} 秒", session.getMaxInactiveInterval());
            log.info("Session是否为新创建: {}", session.isNew());

            if (savedUserId == null || !savedUserId.equals(userId)) {
                log.error("Session创建失败！保存的用户ID与预期不符");
                return Result.error(500, "Session创建失败");
            }

            log.info("=== 登录成功 ===");
            return Result.success("登录成功");

        } catch (Exception e) {
            log.error("登录异常 - 用户: {}, 错误: {}", userId, e.getMessage(), e);
            return Result.error(500, "登录异常: " + e.getMessage());
        }
    }

    // 修复：获取当前登录用户信息接口
    @GetMapping("/current-user")
    public Result<String> getCurrentUser(HttpServletRequest request) {
        log.info("=== 开始获取当前登录用户信息 ===");

        try {
            HttpSession session = request.getSession(false);
            log.info("Session检查: {}", session != null ? "存在" : "不存在");

            if (session != null) {
                log.info("Session ID: {}", session.getId());
                log.info("Session创建时间: {}", new java.util.Date(session.getCreationTime()));
                log.info("Session最后访问时间: {}", new java.util.Date(session.getLastAccessedTime()));

                String userId = (String) session.getAttribute("userId");
                log.info("从session获取的userId: [{}]", userId);

                if (userId != null && !userId.trim().isEmpty()) {
                    log.info("用户ID有效，开始构建用户信息: {}", userId);

                    // 构建用户信息字符串
                    StringBuilder userInfo = new StringBuilder();
                    userInfo.append("userId:").append(userId);

                    // 获取用户角色
                    try {
                        log.info("开始获取用户角色: {}", userId);
                        Result<UserRole> roleResult = userService.getRole(userId);
                        log.info("角色查询结果 - 状态码: {}, 数据: {}", roleResult.getCode(), roleResult.getData());

                        if (roleResult.getCode() == 200 && roleResult.getData() != null) {
                            String userRole = roleResult.getData().getUserRole();
                            userInfo.append(",role:").append(userRole != null ? userRole : "user");
                            log.info("获取用户角色成功: {}", userRole);
                        } else {
                            userInfo.append(",role:user");
                            log.info("使用默认角色: user");
                        }
                    } catch (Exception roleException) {
                        log.warn("获取用户角色时发生异常: {}", roleException.getMessage());
                        userInfo.append(",role:user");
                    }

                    // 添加登录时间
                    Object loginTime = session.getAttribute("loginTime");
                    if (loginTime != null) {
                        userInfo.append(",loginTime:").append(loginTime);
                        log.info("添加登录时间: {}", loginTime);
                    }

                    String finalUserInfo = userInfo.toString();
                    log.info("=== 构建完成的用户信息字符串: [{}] ===", finalUserInfo);

                    // 关键修复：使用正确的Result.success方法，将数据放在data字段
                    log.info("=== 返回用户信息到data字段 ===");
                    return Result.success(finalUserInfo, "获取用户信息成功");

                } else {
                    log.warn("Session中的userId为空: [{}]", userId);

                    // 打印session中的所有属性用于调试
                    log.info("Session中的所有属性:");
                    java.util.Enumeration<String> attributeNames = session.getAttributeNames();
                    while (attributeNames.hasMoreElements()) {
                        String attrName = attributeNames.nextElement();
                        Object attrValue = session.getAttribute(attrName);
                        log.info("  {} = {}", attrName, attrValue);
                    }
                }
            } else {
                log.warn("Session不存在或已过期");
            }

        } catch (Exception e) {
            log.error("获取当前用户信息时发生异常: {}", e.getMessage(), e);
            return Result.error(500, "获取用户信息异常: " + e.getMessage());
        }

        log.warn("=== 用户未登录或session已过期 ===");
        return Result.error(401, "用户未登录");
    }

    // 改进的用户登出接口
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("用户登出请求");

        HttpSession session = request.getSession(false);

        if (session != null) {
            String userId = (String) session.getAttribute("userId");

            // 完全销毁session
            session.invalidate();

            // 清除JSESSIONID cookie
            Cookie sessionCookie = new Cookie("JSESSIONID", null);
            sessionCookie.setMaxAge(0);
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);

            log.info("用户 {} 已登出，session已完全销毁", userId);
            return Result.success("登出成功");
        }

        log.info("用户登出 - 无活动session");
        return Result.success("登出成功");
    }

    // 检查session状态（调试用）
    @GetMapping("/session/status")
    public Result<String> getSessionStatus(HttpServletRequest request) {
        log.info("检查session状态");

        StringBuilder status = new StringBuilder();

        HttpSession session = request.getSession(false);
        if (session != null) {
            status.append("Session存在: true\n");
            status.append("Session ID: ").append(session.getId()).append("\n");

            String userId = (String) session.getAttribute("userId");
            status.append("用户ID: ").append(userId != null ? userId : "null").append("\n");

            Object loginTime = session.getAttribute("loginTime");
            status.append("登录时间: ").append(loginTime != null ? loginTime : "null").append("\n");
            status.append("最大非活动间隔: ").append(session.getMaxInactiveInterval()).append("秒\n");
            status.append("创建时间: ").append(new java.util.Date(session.getCreationTime())).append("\n");
            status.append("最后访问时间: ").append(new java.util.Date(session.getLastAccessedTime())).append("\n");

            // 显示所有session属性
            status.append("所有session属性:\n");
            java.util.Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String attrName = attributeNames.nextElement();
                Object attrValue = session.getAttribute(attrName);
                status.append("  ").append(attrName).append(" = ").append(attrValue).append("\n");
            }
        } else {
            status.append("Session存在: false\n");
        }

        // 使用正确的Result.success方法
        return Result.success(status.toString(), "Session状态查询成功");
    }

    // 强制用户切换接口（调试用）
    @PostMapping("/switch-user")
    public Result<String> switchUser(@RequestParam String newUserId, @RequestParam String password,
                                     HttpServletRequest request, HttpServletResponse response) {
        log.info("强制用户切换请求: {}", newUserId);

        try {
            // 1. 验证新用户密码
            Result<UserPwd> pwdResult = userService.getPwd(newUserId);
            if (pwdResult.getCode() != 200 || pwdResult.getData() == null) {
                return Result.error(400, "目标用户不存在");
            }

            String dbPassword = pwdResult.getData().getUserPwd();
            if (!dbPassword.equals(password)) {
                return Result.error(401, "目标用户密码错误");
            }

            // 2. 清除当前session
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                String oldUserId = (String) oldSession.getAttribute("userId");
                oldSession.invalidate();
                log.info("已清除用户 {} 的session", oldUserId);
            }

            // 3. 清除cookie
            Cookie sessionCookie = new Cookie("JSESSIONID", null);
            sessionCookie.setMaxAge(0);
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);

            // 4. 创建新session
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("userId", newUserId);
            newSession.setAttribute("loginTime", System.currentTimeMillis());
            newSession.setMaxInactiveInterval(24 * 60 * 60);

            log.info("已创建用户 {} 的新session: {}", newUserId, newSession.getId());
            return Result.success("用户切换成功");

        } catch (Exception e) {
            log.error("用户切换异常: {}", e.getMessage(), e);
            return Result.error(500, "用户切换异常: " + e.getMessage());
        }
    }

    // === 以下为原有方法，保持不变 ===

    @GetMapping("/getPwd")
    public Result<UserPwd> getPwd(@RequestParam(value = "userId") String userId) {
        log.info("Attempting to get password for user ID: {}", userId);
        try {
            Result<UserPwd> result = userService.getPwd(userId);
            if (result.getCode() == 200) {
                log.info("Successfully retrieved password for user ID: {}", userId);
            } else {
                log.warn("Failed to get password for user ID: {}. Error: {}", userId, result.getMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("Exception occurred while getting password for user ID: {}. Error: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/updatePwd")
    public Result<String> updatePwd(@RequestBody UserPwd newUserPwd) {
        log.info("Attempting to update password for user ID: {}", newUserPwd.getUserId());
        try {
            Result<String> result = userService.updatePwd(newUserPwd);
            if (result.getCode() == 200) {
                log.info("Successfully updated password for user ID: {}", newUserPwd.getUserId());
            } else {
                log.error("Failed to update password for user ID: {}. Error: {}", newUserPwd.getUserId(), result.getMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("Exception occurred while updating password for user ID: {}. Error: {}", newUserPwd.getUserId(), e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/addUser")
    public Result<String> addUser(@RequestBody UserRole userRole) {
        log.info("Attempting to add new user with ID: {}", userRole.getUserId());
        try {
            Result<String> result = userService.addUser(userRole);
            if (result.getCode() == 200) {
                log.info("Successfully added new user with ID: {}", userRole.getUserId());
            } else {
                log.error("Failed to add new user with ID: {}. Error: {}", userRole.getUserId(), result.getMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("Exception occurred while adding new user with ID: {}. Error: {}", userRole.getUserId(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/getRole")
    public Result<UserRole> getRole(@RequestParam(value = "userId") String userId) {
        log.info("Attempting to get role for user ID: {}", userId);
        try {
            Result<UserRole> result = userService.getRole(userId);
            if (result.getCode() == 200) {
                log.info("Successfully retrieved role for user ID: {}", userId);
            } else {
                log.warn("Failed to get role for user ID: {}. Error: {}", userId, result.getMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("Exception occurred while getting role for user ID: {}. Error: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/updateRole")
    public Result<String> updateRole(@RequestBody UserRole newUserRole) {
        log.info("Attempting to update role for user ID: {}", newUserRole.getUserId());
        try {
            Result<String> result = userService.updateRole(newUserRole);
            if (result.getCode() == 200) {
                log.info("Successfully updated role for user ID: {}", newUserRole.getUserId());
            } else {
                log.error("Failed to update role for user ID: {}. Error: {}", newUserRole.getUserId(), result.getMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("Exception occurred while updating role for user ID: {}. Error: {}", newUserRole.getUserId(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/exists")
    public Result<String> userExists(@RequestParam String userId) {
        log.info("Checking if user exists: {}", userId);
        try {
            Result<UserPwd> result = userService.getPwd(userId);
            boolean exists = result.getCode() == 200 && result.getData() != null;
            log.info("User {} exists: {}", userId, exists);

            if (exists) {
                return Result.success("用户存在");
            } else {
                return Result.success("用户不存在");
            }
        } catch (Exception e) {
            log.error("Exception occurred while checking user existence: {}", e.getMessage(), e);
            return Result.error(500, "检查用户存在性时发生异常");
        }
    }

    @PostMapping("/verify")
    public Result<String> verifyUser(@RequestParam String userId, @RequestParam String password) {
        log.info("简单验证用户: {}", userId);

        try {
            Result<UserPwd> pwdResult = userService.getPwd(userId);

            if (pwdResult.getCode() != 200 || pwdResult.getData() == null) {
                log.warn("用户不存在: {}", userId);
                return Result.error(400, "用户不存在");
            }

            String dbPassword = pwdResult.getData().getUserPwd();
            log.info("验证密码 - 输入: [{}], 数据库: [{}], 匹配: {}", password, dbPassword, dbPassword.equals(password));

            if (dbPassword.equals(password)) {
                return Result.success("验证成功");
            } else {
                return Result.error(401, "密码错误");
            }

        } catch (Exception e) {
            log.error("验证异常: {}", e.getMessage(), e);
            return Result.error(500, "验证异常");
        }
    }

    @GetMapping("/debug/info")
    public Result<String> getDebugInfo(@RequestParam String userId) {
        log.info("获取调试信息: {}", userId);

        try {
            Result<UserPwd> pwdResult = userService.getPwd(userId);

            StringBuilder debugInfo = new StringBuilder();
            debugInfo.append("用户ID: ").append(userId).append("\n");
            debugInfo.append("密码状态码: ").append(pwdResult.getCode()).append("\n");

            if (pwdResult.getData() != null) {
                String password = pwdResult.getData().getUserPwd();
                debugInfo.append("密码: [").append(password).append("]\n");
                debugInfo.append("密码长度: ").append(password.length()).append("\n");
            }

            // 使用正确的Result.success方法
            return Result.success(debugInfo.toString(), "调试信息获取成功");

        } catch (Exception e) {
            log.error("获取调试信息异常: {}", e.getMessage(), e);
            return Result.error(500, "获取调试信息异常");
        }
    }

    @GetMapping("/test")
    public Result<String> test() {
        log.info("测试接口被调用");
        return Result.success("接口正常工作");
    }

    @PostMapping("/loginInfo")
    public Result<String> getLoginInfo(@RequestParam String userId, @RequestParam String password) {
        log.info("获取登录信息: {}", userId);

        try {
            Result<UserPwd> pwdResult = userService.getPwd(userId);

            if (pwdResult.getCode() != 200 || pwdResult.getData() == null) {
                log.warn("用户不存在: {}", userId);
                return Result.error(400, "用户不存在");
            }

            String dbPassword = pwdResult.getData().getUserPwd();
            log.info("登录验证 - 输入密码: [{}], 数据库密码: [{}]", password, dbPassword);

            if (!dbPassword.equals(password)) {
                log.warn("密码错误: {}", userId);
                return Result.error(401, "密码错误");
            }

            String loginInfo = "用户ID: " + userId + ", 登录时间: " + System.currentTimeMillis();

            log.info("登录成功: {}", userId);
            // 使用正确的Result.success方法
            return Result.success(loginInfo, "登录信息获取成功");

        } catch (Exception e) {
            log.error("获取登录信息异常: {}", e.getMessage(), e);
            return Result.error(500, "登录异常");
        }
    }
}