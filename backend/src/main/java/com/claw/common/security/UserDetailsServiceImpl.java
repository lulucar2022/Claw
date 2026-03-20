package com.claw.common.security;

import com.claw.modules.user.entity.User;
import com.claw.modules.user.enums.UserStatusEnum;
import com.claw.modules.user.repository.UserRepository;
import com.claw.common.exception.DataNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 尝试通过用户名或邮箱查找用户
        User user = userRepository.findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
        
        // 检查用户状态
        checkUserStatus(user);
        
        // 获取用户角色（从User实体中获取，默认为ROLE_USER）
        String[] authorities = getUserAuthorities(user);
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(user.getStatus() == UserStatusEnum.LOCKED)
                .credentialsExpired(false)
                .disabled(user.getStatus() == UserStatusEnum.DISABLED)
                .build();
    }
    
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("用户不存在"));
        
        // 检查用户状态
        checkUserStatus(user);
        
        // 获取用户角色
        String[] authorities = getUserAuthorities(user);
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(user.getStatus() == UserStatusEnum.LOCKED)
                .credentialsExpired(false)
                .disabled(user.getStatus() == UserStatusEnum.DISABLED)
                .build();
    }
    
    /**
     * 检查用户状态，如果状态不正常则抛出相应异常
     */
    private void checkUserStatus(User user) {
        if (user.getStatus() == UserStatusEnum.DISABLED) {
            throw new UsernameNotFoundException("用户已被禁用: " + user.getUsername());
        }
        
        if (user.getStatus() == UserStatusEnum.LOCKED) {
            throw new UsernameNotFoundException("用户已被锁定: " + user.getUsername());
        }
        
        // 检查邮箱验证状态（可选，根据需求）
        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            // 可以记录日志或处理未验证用户
            // 这里不阻止登录，但可以标记为需要验证
        }
    }
    
    /**
     * 获取用户权限
     * 注意：目前User实体中没有角色字段，这里返回默认的ROLE_USER
     * 后续可以根据用户类型或从其他表获取实际角色
     */
    private String[] getUserAuthorities(User user) {
        List<String> authorities = new ArrayList<>();
        
        // 默认所有用户都有ROLE_USER权限
        authorities.add("ROLE_USER");
        
        // 可以根据用户类型添加额外权限
        // 例如：管理员、VIP用户等
        // if (user.isAdmin()) {
        //     authorities.add("ROLE_ADMIN");
        // }
        // 
        // if (user.isVip()) {
        //     authorities.add("ROLE_VIP");
        // }
        
        return authorities.toArray(new String[0]);
    }
    
    /**
     * 获取用户角色（兼容老代码）
     * 返回GrantedAuthority列表
     */
    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // 默认所有用户都有ROLE_USER权限
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        // 可以根据用户类型添加额外权限
        // 例如：管理员、VIP用户等
        // if (user.isAdmin()) {
        //     authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        // }
        // 
        // if (user.isVip()) {
        //     authorities.add(new SimpleGrantedAuthority("ROLE_VIP"));
        // }
        
        return authorities;
    }
}