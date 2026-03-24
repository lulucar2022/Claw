package com.claw.modules.user.service;

import com.claw.common.exception.DataNotFoundException;
import com.claw.modules.user.dto.UpdateProfileRequest;
import com.claw.modules.user.entity.User;
import com.claw.modules.user.enums.UserStatusEnum;
import com.claw.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 根据ID获取用户
     */
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> DataNotFoundException.userNotFound(userId));
    }

    /**
     * 根据用户名获取用户
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> DataNotFoundException.userNotFound(username));
    }

    /**
     * 根据邮箱获取用户
     */
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> DataNotFoundException.userNotFound(email));
    }

    /**
     * 更新用户信息
     */
    @Transactional
    public User updateUser(Long userId, User updatedUser) {
        User user = getUserById(userId);
        
        if (updatedUser.getNickname() != null) {
            user.setNickname(updatedUser.getNickname());
        }
        if (updatedUser.getPhone() != null) {
            user.setPhone(updatedUser.getPhone());
        }
        if (updatedUser.getTechStack() != null) {
            user.setTechStack(updatedUser.getTechStack());
        }
        if (updatedUser.getExperienceYears() != null) {
            user.setExperienceYears(updatedUser.getExperienceYears());
        }
        if (updatedUser.getTargetPosition() != null) {
            user.setTargetPosition(updatedUser.getTargetPosition());
        }
        if (updatedUser.getCurrentLevel() != null) {
            user.setCurrentLevel(updatedUser.getCurrentLevel());
        }
        if (updatedUser.getDailyStudyTime() != null) {
            user.setDailyStudyTime(updatedUser.getDailyStudyTime());
        }
        if (updatedUser.getPreferredDifficulty() != null) {
            user.setPreferredDifficulty(updatedUser.getPreferredDifficulty());
        }
        if (updatedUser.getStudyGoal() != null) {
            user.setStudyGoal(updatedUser.getStudyGoal());
        }
        
        return userRepository.save(user);
    }

    /**
     * 检查用户名是否存在
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 检查邮箱是否存在
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 检查手机号是否存在
     */
    @Transactional(readOnly = true)
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    /**
     * 更新用户最后登录时间
     */
    @Transactional
    public void updateLastLoginTime(Long userId) {
        User user = getUserById(userId);
        user.updateLastLoginTime();
        userRepository.save(user);
    }

    /**
     * 更新用户头像
     */
    @Transactional
    public void updateAvatar(Long userId, String avatarUrl) {
        User user = getUserById(userId);
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
    }

    /**
     * 启用用户账户
     */
    @Transactional
    public void enableUser(Long userId) {
        User user = getUserById(userId);
        user.enable();
        userRepository.save(user);
    }

    /**
     * 禁用用户账户
     */
    @Transactional
    public void disableUser(Long userId) {
        User user = getUserById(userId);
        user.disable();
        userRepository.save(user);
    }

    /**
     * 锁定用户账户
     */
    @Transactional
    public void lockUser(Long userId) {
        User user = getUserById(userId);
        user.lock();
        userRepository.save(user);
    }

    /**
     * 解锁用户账户
     */
    @Transactional
    public void unlockUser(Long userId) {
        User user = getUserById(userId);
        user.unlock();
        userRepository.save(user);
    }

    /**
     * 获取当前用户及其完整资料信息
     */
    @Transactional(readOnly = true)
    public User getCurrentUserWithProfile() {
        // 这里需要获取当前认证用户
        // 由于当前上下文没有认证信息，我们暂时返回一个示例用户
        // 实际项目中应该从 SecurityContext 获取当前用户
        return getUserById(1L); // 临时实现，实际应使用认证信息
    }

    /**
     * 更新用户个人资料
     */
    @Transactional
    public User updateProfile(UpdateProfileRequest request) {
        User currentUser = getCurrentUserWithProfile();
        
        if (request.getNickname() != null) {
            currentUser.setNickname(request.getNickname());
        }
        if (request.getPhone() != null) {
            currentUser.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            currentUser.setEmail(request.getEmail());
        }
        if (request.getTechStack() != null) {
            // 将逗号分隔的字符串转换为List
            currentUser.setTechStack(List.of(request.getTechStack().split(",")));
        }
        if (request.getExperienceYears() != null) {
            currentUser.setExperienceYears(request.getExperienceYears());
        }
        if (request.getTargetPosition() != null) {
            currentUser.setTargetPosition(request.getTargetPosition());
        }
        if (request.getCurrentLevel() != null) {
            currentUser.setCurrentLevel(request.getCurrentLevel());
        }
        if (request.getDailyStudyTime() != null) {
            currentUser.setDailyStudyTime(request.getDailyStudyTime());
        }
        if (request.getPreferredDifficulty() != null) {
            currentUser.setPreferredDifficulty(request.getPreferredDifficulty());
        }
        if (request.getStudyGoal() != null) {
            currentUser.setStudyGoal(request.getStudyGoal());
        }
        if (request.getAvatar() != null) {
            currentUser.setAvatarUrl(request.getAvatar());
        }
        
        return userRepository.save(currentUser);
    }

    /**
     * 分页查询用户列表
     */
    @Transactional(readOnly = true)
    public Page<User> getUsers(Pageable pageable, String keyword, Integer status) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 根据关键词搜索用户名或邮箱
            return userRepository.searchByUsername(keyword.trim(), pageable);
        } else if (status != null) {
            // 根据状态筛选 - 需要手动实现分页
            UserStatusEnum statusEnum = UserStatusEnum.fromCode(status);
            List<User> allUsers = userRepository.findAll();
            List<User> filteredUsers = allUsers.stream()
                    .filter(user -> user.getStatus() == statusEnum)
                    .toList();
            
            // 手动分页
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), filteredUsers.size());
            List<User> pageContent = filteredUsers.subList(start, end);
            
            return new PageImpl<>(pageContent, pageable, filteredUsers.size());
        } else {
            // 返回所有未删除的用户
            return userRepository.findAllByDeletedAtIsNull(pageable);
        }
    }

    /**
     * 更新用户状态
     */
    @Transactional
    public void updateUserStatus(Long userId, Integer status) {
        User user = getUserById(userId);
        UserStatusEnum statusEnum = UserStatusEnum.fromCode(status);
        
        switch (statusEnum) {
            case DISABLED:
                user.disable();
                break;
            case NORMAL:
                user.enable();
                break;
            case LOCKED:
                user.lock();
                break;
            default:
                throw new IllegalArgumentException("无效的用户状态: " + status);
        }
        
        userRepository.save(user);
    }

    /**
     * 软删除用户
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * 恢复已删除的用户
     */
    @Transactional
    public void restoreUser(Long userId) {
        userRepository.restoreUser(userId);
    }

    /**
     * 获取用户统计摘要
     */
    @Transactional(readOnly = true)
    public Object getUserStatsSummary() {
        Map<String, Object> stats = new HashMap<>();
        
        // 统计各状态用户数量
        long totalUsers = userRepository.count();
        long normalUsers = userRepository.countByStatus(UserStatusEnum.NORMAL);
        long disabledUsers = userRepository.countByStatus(UserStatusEnum.DISABLED);
        long lockedUsers = userRepository.countByStatus(UserStatusEnum.LOCKED);
        
        stats.put("totalUsers", totalUsers);
        stats.put("normalUsers", normalUsers);
        stats.put("disabledUsers", disabledUsers);
        stats.put("lockedUsers", lockedUsers);
        stats.put("normalRate", totalUsers > 0 ? (normalUsers * 100.0 / totalUsers) : 0);
        
        // 今日新增用户
        long todayNewUsers = userRepository.countTodayNewUsers(LocalDateTime.now());
        stats.put("todayNewUsers", todayNewUsers);
        
        return stats;
    }

    /**
     * 根据技术栈搜索用户
     */
    @Transactional(readOnly = true)
    public Page<User> searchUsersByTech(String techKeyword, Pageable pageable) {
        return userRepository.findByTechAndExperience(techKeyword, null, null, pageable);
    }

    /**
     * 获取学习排行榜
     */
    @Transactional(readOnly = true)
    public Page<Object> getStudyRankings(Pageable pageable) {
        // 这里需要返回包含用户学习信息的对象列表
        // 暂时返回空分页，实际应查询学习数据
        return Page.empty(pageable);
    }

    /**
     * 获取每日新增用户统计
     */
    @Transactional(readOnly = true)
    public Object getDailyNewUserStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 这里应该返回最近7天的每日新增用户统计
        // 暂时返回示例数据
        stats.put("last7Days", new HashMap<>());
        stats.put("totalNewUsers", 0);
        stats.put("averageDailyNew", 0);
        
        return stats;
    }
}