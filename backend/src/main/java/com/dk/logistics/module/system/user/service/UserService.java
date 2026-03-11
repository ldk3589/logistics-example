package com.dk.logistics.module.system.user.service;

import com.dk.logistics.common.api.PageResult;
import com.dk.logistics.module.system.user.dto.UserAddDTO;
import com.dk.logistics.module.system.user.dto.UserQueryDTO;
import com.dk.logistics.module.system.user.dto.UserUpdateDTO;
import com.dk.logistics.module.system.user.vo.UserVO;
import java.util.List;
public interface UserService {

    PageResult<UserVO> listUsers(UserQueryDTO queryDTO);
    void addUser(UserAddDTO dto);

    void assignRoles(Long userId, List<Long> roleIds);

    List<Long> getRoleIdsByUserId(Long userId);

    UserVO getUserById(Long userId);

    void updateUser(Long userId, UserUpdateDTO dto);

    void deleteUser(Long userId);

    void changeUserStatus(Long userId, Integer status);
}