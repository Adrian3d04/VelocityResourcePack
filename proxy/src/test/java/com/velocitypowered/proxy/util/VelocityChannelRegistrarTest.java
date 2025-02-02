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

package com.velocitypowered.proxy.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.ImmutableSet;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

class VelocityChannelRegistrarTest {

  private static final ChannelIdentifier MODERN = ChannelIdentifier.ofKey(
      Key.key("velocity", "test"));
  private static final ChannelIdentifier SIMPLE_LEGACY =
      ChannelIdentifier.legacy("VelocityTest");

  private static final ChannelIdentifier MODERN_SPECIAL_REMAP = ChannelIdentifier.ofKey(
      Key.key("bungeecord", "main"));
  private static final ChannelIdentifier SPECIAL_REMAP_LEGACY =
      ChannelIdentifier.legacy("BungeeCord");

  private static final String SIMPLE_LEGACY_REMAPPED = "legacy:velocitytest";

  @Test
  void register() {
    VelocityChannelRegistrar registrar = new VelocityChannelRegistrar();
    registrar.register(MODERN, SIMPLE_LEGACY);

    // Two channels cover the modern channel (velocity:test) and the legacy-mapped channel
    // (legacy:velocitytest). Make sure they're what we expect.
    assertEquals(ImmutableSet.of(MODERN.id(), SIMPLE_LEGACY_REMAPPED), registrar
        .getModernChannelIds());
    assertEquals(ImmutableSet.of(SIMPLE_LEGACY.id(), MODERN.id()), registrar
        .getLegacyChannelIds());
  }

  @Test
  void registerSpecialRewrite() {
    VelocityChannelRegistrar registrar = new VelocityChannelRegistrar();
    registrar.register(SPECIAL_REMAP_LEGACY, MODERN_SPECIAL_REMAP);

    // This one, just one channel for the modern case.
    assertEquals(ImmutableSet.of(MODERN_SPECIAL_REMAP.id()), registrar.getModernChannelIds());
    assertEquals(ImmutableSet.of(MODERN_SPECIAL_REMAP.id(), SPECIAL_REMAP_LEGACY.id()),
        registrar.getLegacyChannelIds());
  }

  @Test
  void unregister() {
    VelocityChannelRegistrar registrar = new VelocityChannelRegistrar();
    registrar.register(MODERN, SIMPLE_LEGACY);
    registrar.unregister(SIMPLE_LEGACY);

    assertEquals(ImmutableSet.of(MODERN.id()), registrar.getModernChannelIds());
    assertEquals(ImmutableSet.of(MODERN.id()), registrar.getLegacyChannelIds());
  }
}