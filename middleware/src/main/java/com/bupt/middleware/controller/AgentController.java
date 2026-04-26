package com.bupt.middleware.controller;

import com.bupt.middleware.common.Result;
import com.bupt.middleware.dto.CreateConversationRequest;
import com.bupt.middleware.dto.SendMessageRequest;
import com.bupt.middleware.entity.AgentTool;
import com.bupt.middleware.entity.AgentConversation;
import com.bupt.middleware.entity.AgentMessage;
import com.bupt.middleware.entity.UserRole;
import com.bupt.middleware.service.AgentToolService;
import com.bupt.middleware.service.AgentConversationService;
import com.bupt.middleware.service.AgentMessageService;
import com.bupt.middleware.service.AgentChatService;
import com.bupt.middleware.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agent")
@Slf4j
public class AgentController {

    @Autowired
    private AgentToolService agentToolService;

    @Autowired
    private AgentConversationService agentConversationService;

    @Autowired
    private AgentMessageService agentMessageService;

    @Autowired
    private AgentChatService agentChatService;

    @Autowired
    private IUserService userService; // 注入用户服务，用于获取用户角色

    /**
     * 从请求中获取当前用户ID - 修改为优先从主系统获取
     */
    private String getCurrentUserId(HttpServletRequest request) {
        // 方式1: 优先从主系统session获取（新增）
        HttpSession session = request.getSession(false);
        if (session != null) {
            String mainSystemUserId = (String) session.getAttribute("userId");
            if (mainSystemUserId != null && !mainSystemUserId.isEmpty()) {
                log.debug("从主系统session获取用户ID: {}", mainSystemUserId);
                return mainSystemUserId;
            }
        }

        // 方式2: 从请求头获取 (前端传递用户ID)
        String userIdFromHeader = request.getHeader("X-User-ID");
        if (userIdFromHeader != null && !userIdFromHeader.isEmpty()) {
            log.debug("从请求头获取用户ID: {}", userIdFromHeader);
            return userIdFromHeader;
        }

        // 方式3: 从AgentChat自己的session获取（兼容性）
        if (session != null) {
            String agentUserId = (String) session.getAttribute("agentUserId");
            if (agentUserId != null) {
                log.debug("从agent session获取用户ID: {}", agentUserId);
                return agentUserId;
            }
        }

        log.warn("无法获取当前用户ID");
        return null;
    }

    /**
     * 验证用户权限
     */
    private boolean validateUser(String requestUserId, HttpServletRequest request) {
        if (requestUserId == null || requestUserId.isEmpty()) {
            log.warn("请求用户ID为空");
            return false;
        }

        String currentUserId = getCurrentUserId(request);
        if (currentUserId == null) {
            log.warn("无法获取当前认证用户ID");
            return false;
        }

        boolean isValid = requestUserId.equals(currentUserId);
        if (!isValid) {
            log.warn("用户权限验证失败: 请求用户ID={}, 当前用户ID={}", requestUserId, currentUserId);
        }

        return isValid;
    }

    /**
     * 验证对话归属权
     */
    private boolean validateConversationOwnership(Long conversationId, HttpServletRequest request) {
        try {
            Result<AgentConversation> conversationResult = agentConversationService.getConversationById(conversationId);
            if (conversationResult.getCode() != 200 || conversationResult.getData() == null) {
                log.warn("对话不存在: {}", conversationId);
                return false;
            }

            String conversationOwner = conversationResult.getData().getUserId();
            String currentUserId = getCurrentUserId(request);

            boolean isOwner = conversationOwner != null && conversationOwner.equals(currentUserId);
            if (!isOwner) {
                log.warn("对话权限验证失败: 对话ID={}, 拥有者={}, 当前用户={}",
                        conversationId, conversationOwner, currentUserId);
            }

            return isOwner;
        } catch (Exception e) {
            log.error("验证对话归属权时发生异常: ", e);
            return false;
        }
    }

    private String resolveIdentity(String requestIdentity, HttpServletRequest request) {
        if (requestIdentity != null && !requestIdentity.isBlank()) {
            return requestIdentity;
        }
        String currentUserId = getCurrentUserId(request);
        if (currentUserId != null) {
            try {
                com.bupt.middleware.entity.result.Result<UserRole> roleResult = userService.getRole(currentUserId);
                if (roleResult.getCode() == 200 && roleResult.getData() != null
                        && roleResult.getData().getUserRole() != null) {
                    return roleResult.getData().getUserRole();
                }
            } catch (Exception e) {
                log.warn("Failed to resolve agent identity for user {}: {}", currentUserId, e.getMessage());
            }
        }
        return "student";
    }

    /**
     * 获取所有智能体工具 (公共资源，不需要用户验证)
     */
    @GetMapping("/tools")
    public Result<List<AgentTool>> getTools(@RequestParam(required = false) String category) {
        log.info("获取智能体工具列表，分类: {}", category);
        return agentToolService.getAllActiveTools(category);
    }

    /**
     * 根据ID获取工具详情 (公共资源，不需要用户验证)
     */
    @GetMapping("/tools/{id}")
    public Result<AgentTool> getToolById(@PathVariable Long id) {
        log.info("获取工具详情，ID: {}", id);
        return agentToolService.getToolById(id);
    }

    /**
     * 获取用户的对话列表
     */
    @GetMapping("/conversations")
    public Result<List<AgentConversation>> getUserConversations(
            @RequestParam String userId,
            @RequestParam(required = false) Long toolId,
            HttpServletRequest request) {

        log.info("获取用户对话列表，用户ID: {}, 工具ID: {}", userId, toolId);

        // 验证用户权限
        if (!validateUser(userId, request)) {
            return Result.error(403, "无权访问其他用户的对话列表");
        }

        return agentConversationService.getUserConversations(userId, toolId);
    }

    /**
     * 创建新对话
     */
    @PostMapping("/conversations")
    public Result<AgentConversation> createConversation(
            @RequestBody CreateConversationRequest request,
            HttpServletRequest httpRequest) {

        log.info("创建新对话，用户ID: {}, 工具ID: {}", request.getUserId(), request.getToolId());

        // 验证用户权限
        if (!validateUser(request.getUserId(), httpRequest)) {
            return Result.error(403, "无权为其他用户创建对话");
        }

        return agentConversationService.createConversation(request);
    }

    /**
     * 获取对话详情
     */
    @GetMapping("/conversations/{id}")
    public Result<AgentConversation> getConversationById(
            @PathVariable Long id,
            HttpServletRequest request) {

        log.info("获取对话详情，ID: {}", id);

        // 验证对话归属权
        if (!validateConversationOwnership(id, request)) {
            return Result.error(403, "无权访问此对话");
        }

        return agentConversationService.getConversationById(id);
    }

    /**
     * 更新对话标题
     */
    @PutMapping("/conversations/{id}/title")
    public Result<AgentConversation> updateConversationTitle(
            @PathVariable Long id,
            @RequestBody String title,
            HttpServletRequest request) {

        log.info("更新对话标题，ID: {}, 标题: {}", id, title);

        // 验证对话归属权
        if (!validateConversationOwnership(id, request)) {
            return Result.error(403, "无权修改此对话");
        }

        return agentConversationService.updateConversationTitle(id, title);
    }

    /**
     * 删除对话
     */
    @DeleteMapping("/conversations/{id}")
    public Result<String> deleteConversation(
            @PathVariable Long id,
            HttpServletRequest request) {

        log.info("删除对话，ID: {}", id);

        // 验证对话归属权
        if (!validateConversationOwnership(id, request)) {
            return Result.error(403, "无权删除此对话");
        }

        return agentConversationService.deleteConversation(id);
    }

    /**
     * 获取对话的消息列表
     */
    @GetMapping("/conversations/{conversationId}/messages")
    public Result<List<AgentMessage>> getConversationMessages(
            @PathVariable Long conversationId,
            HttpServletRequest request) {

        log.info("获取对话消息，对话ID: {}", conversationId);

        // 验证对话归属权
        if (!validateConversationOwnership(conversationId, request)) {
            return Result.error(403, "无权查看此对话的消息");
        }

        return agentMessageService.getConversationMessages(conversationId);
    }

    /**
     * 发送消息并获取AI回复
     */
    @PostMapping("/conversations/{conversationId}/messages")
    public Result<AgentMessage> sendMessage(
            @PathVariable Long conversationId,
            @RequestBody SendMessageRequest request,
            HttpServletRequest httpRequest) {

        log.info("发送消息到对话，对话ID: {}", conversationId);

        // 验证对话归属权
        if (!validateConversationOwnership(conversationId, httpRequest)) {
            return Result.error(403, "无权向此对话发送消息");
        }

        try {
            // 调用聊天服务处理消息
            return agentChatService.processMessage(
                    conversationId,
                    request.getContent(),
                    request.getModelName(),
                    resolveIdentity(request.getIdentity(), httpRequest),
                    request.getProfileState(),
                    request.getConversationContext());
        } catch (Exception e) {
            log.error("处理消息失败: ", e);
            return Result.error(500, "处理消息失败: " + e.getMessage());
        }
    }

    /**
     * 用户登录/设置会话 (兼容性接口，建议使用主系统登录)
     */
    @PostMapping("/auth/login")
    public Result<String> login(
            @RequestParam String userId,
            HttpServletRequest request) {

        log.info("Agent用户登录，用户ID: {}", userId);

        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
        }

        // 将用户ID存入session（使用不同的key避免与主系统冲突）
        HttpSession session = request.getSession(true);
        session.setAttribute("agentUserId", userId.trim());
        session.setMaxInactiveInterval(24 * 60 * 60); // 24小时

        log.info("Agent用户 {} 登录成功，会话ID: {}", userId, session.getId());
        return Result.success("登录成功");
    }

    /**
     * 用户登出
     */
    @PostMapping("/auth/logout")
    public Result<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String userId = getCurrentUserId(request);
            // 只清除Agent相关的session属性，不影响主系统
            session.removeAttribute("agentUserId");
            log.info("Agent用户 {} 已登出", userId);
        }

        return Result.success("登出成功");
    }

    /**
     * 获取当前用户信息 - 修改为返回完整用户信息并集成主系统
     */
    @GetMapping("/auth/current-user")
    public Result<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
        String userId = getCurrentUserId(request);
        log.info("AgentChat获取当前用户信息，用户ID: {}", userId);

        if (userId != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", userId);
            userInfo.put("userName", userId);

            // 尝试从主系统session获取更多信息
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object loginTime = session.getAttribute("loginTime");
                if (loginTime != null) {
                    userInfo.put("loginTime", loginTime);
                    userInfo.put("source", "main_system");
                } else {
                    userInfo.put("loginTime", System.currentTimeMillis());
                    userInfo.put("source", "agent_system");
                }
            }

            // 获取用户角色信息
            try {
                com.bupt.middleware.entity.result.Result<UserRole> roleResult = userService.getRole(userId);
                if (roleResult.getCode() == 200 && roleResult.getData() != null) {
                    userInfo.put("role", roleResult.getData().getUserRole());
                } else {
                    userInfo.put("role", "user");
                }
            } catch (Exception e) {
                log.warn("获取用户角色失败: {}", e.getMessage());
                userInfo.put("role", "user");
            }

            log.info("返回AgentChat用户信息: {}", userInfo);
            return Result.success(userInfo);
        } else {
            log.warn("AgentChat未找到当前用户");
            return Result.error(401, "未认证用户");
        }
    }

    /**
     * 检查用户认证状态
     */
    @GetMapping("/auth/status")
    public Result<Map<String, Object>> getAuthStatus(HttpServletRequest request) {
        String userId = getCurrentUserId(request);

        Map<String, Object> status = new HashMap<>();
        status.put("authenticated", userId != null);
        status.put("userId", userId);

        HttpSession session = request.getSession(false);
        if (session != null) {
            status.put("sessionId", session.getId());
            status.put("hasMainSystemSession", session.getAttribute("userId") != null);
            status.put("hasAgentSession", session.getAttribute("agentUserId") != null);
        }

        return Result.success(status);
    }
}
