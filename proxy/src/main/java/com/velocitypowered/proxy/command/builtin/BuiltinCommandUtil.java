/*
 * Copyright (C) 2019-2021 Velocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.velocitypowered.proxy.command.builtin;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class BuiltinCommandUtil {

  private BuiltinCommandUtil() {
    throw new AssertionError();
  }

  static List<RegisteredServer> sortedServerList(ProxyServer proxy) {
    List<RegisteredServer> servers = new ArrayList<>(proxy.registeredServers());
    servers.sort(Comparator.comparing(RegisteredServer::serverInfo));
    return Collections.unmodifiableList(servers);
  }
}
