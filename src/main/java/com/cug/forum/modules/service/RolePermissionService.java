package com.cug.forum.modules.service;

import com.cug.forum.modules.entity.Permission;
import com.cug.forum.modules.entity.RolePermission;

import java.util.List;
import java.util.Set;

public interface RolePermissionService {
    List<Permission> findPermissions(long roleId);
    void deleteByRoleId(long roleId);
    void add(Set<RolePermission> rolePermissions);

}
