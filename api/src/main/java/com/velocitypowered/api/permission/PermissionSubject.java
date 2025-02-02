/*
 * Copyright (C) 2018-2022 Velocity Contributors
 *
 * The Velocity API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the api top-level directory.
 */

package com.velocitypowered.api.permission;

import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.util.TriState;

/**
 * Represents a object that has a set of queryable permissions.
 */
public interface PermissionSubject {

  /**
   * Determines whether or not the subject has a particular permission.
   *
   * @param permission the permission to check for
   * @return whether or not the subject has the permission
   */
  default boolean hasPermission(String permission) {
    return this.permissionChecker().test(permission);
  }

  /**
   * Gets the subjects setting for a particular permission.
   *
   * @param permission the permission
   * @return the value the permission is set to
   */
  default TriState getPermissionValue(String permission) {
    return this.permissionChecker().value(permission);
  }

  /**
   * Gets the permission checker for the subject.
   *
   * @return subject's permission checker
   */
  PermissionChecker permissionChecker();
}
