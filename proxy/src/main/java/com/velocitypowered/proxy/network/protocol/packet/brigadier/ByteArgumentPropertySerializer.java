/*
 * Copyright (C) 2018-2022 Velocity Contributors
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

package com.velocitypowered.proxy.network.protocol.packet.brigadier;

import com.velocitypowered.api.network.ProtocolVersion;
import io.netty.buffer.ByteBuf;

class ByteArgumentPropertySerializer implements ArgumentPropertySerializer<Byte> {

  static final ByteArgumentPropertySerializer BYTE = new ByteArgumentPropertySerializer();

  private ByteArgumentPropertySerializer() {

  }

  @Override
  public Byte deserialize(ByteBuf buf, ProtocolVersion protocolVersion) {
    return buf.readByte();
  }

  @Override
  public void serialize(Byte object, ByteBuf buf, ProtocolVersion protocolVersion) {
    buf.writeByte(object);
  }
}
