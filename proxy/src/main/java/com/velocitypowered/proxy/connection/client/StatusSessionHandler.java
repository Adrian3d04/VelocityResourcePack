/*
 * Copyright (C) 2018-2023 Velocity Contributors
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

package com.velocitypowered.proxy.connection.client;

import com.velocitypowered.proxy.VelocityServer;
import com.velocitypowered.proxy.connection.MinecraftConnection;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.connection.util.VelocityInboundConnection;
import com.velocitypowered.proxy.network.protocol.packet.LegacyDisconnect;
import com.velocitypowered.proxy.network.protocol.packet.LegacyPing;
import com.velocitypowered.proxy.network.protocol.packet.StatusPing;
import com.velocitypowered.proxy.network.protocol.packet.StatusRequest;
import com.velocitypowered.proxy.util.except.QuietRuntimeException;
import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles server list ping packets from a client.
 */
public class StatusSessionHandler implements MinecraftSessionHandler {

  private static final Logger logger = LogManager.getLogger(StatusSessionHandler.class);
  private static final QuietRuntimeException EXPECTED_AWAITING_REQUEST = new QuietRuntimeException(
      "Expected connection to be awaiting status request");

  private final VelocityServer server;
  private final MinecraftConnection connection;
  private final VelocityInboundConnection inbound;
  private boolean pingReceived = false;

  StatusSessionHandler(VelocityServer server, VelocityInboundConnection inbound) {
    this.server = server;
    this.connection = inbound.getConnection();
    this.inbound = inbound;
  }

  @Override
  public void activated() {
    if (server.configuration().isShowPingRequests()) {
      logger.info("{} is pinging the server with version {}", this.inbound,
          this.connection.getProtocolVersion());
    }
  }

  @Override
  public boolean handle(LegacyPing packet) {
    if (this.pingReceived) {
      throw EXPECTED_AWAITING_REQUEST;
    }
    this.pingReceived = true;
    server.getServerListPingHandler().getPing(this.inbound)
        .thenAcceptAsync(ping -> connection.closeWith(
                LegacyDisconnect.fromServerPing(ping, packet.getVersion())),
            connection.eventLoop())
        .exceptionally((ex) -> {
          logger.error("Exception while handling legacy ping {}", packet, ex);
          return null;
        });
    return true;
  }

  @Override
  public boolean handle(StatusPing packet) {
    connection.closeWith(packet);
    return true;
  }

  @Override
  public boolean handle(StatusRequest packet) {
    if (this.pingReceived) {
      throw EXPECTED_AWAITING_REQUEST;
    }
    this.pingReceived = true;

    server.getServerListPingHandler().getPacketResponse(this.inbound)
        .thenAcceptAsync(connection::write, connection.eventLoop())
        .exceptionally((ex) -> {
          logger.error("Exception while handling status request {}", packet, ex);
          return null;
        });
    return true;
  }

  @Override
  public void handleUnknown(ByteBuf buf) {
    // what even is going on?
    connection.close(true);
  }
}
