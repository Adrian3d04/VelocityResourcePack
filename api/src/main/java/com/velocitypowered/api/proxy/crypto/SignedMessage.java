/*
 * Copyright (C) 2022-2023 Velocity Contributors
 *
 * The Velocity API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the api top-level directory.
 */

package com.velocitypowered.api.proxy.crypto;

import java.util.UUID;

/**
 * A signed message.
 */
public interface SignedMessage extends KeySigned {

  /**
   * Returns the signed message.
   *
   * @return the message
   */
  String message();

  /**
   * Returns the signers UUID.
   *
   * @return the uuid
   */
  UUID signerUuid();

  /**
   * If true the signature of this message applies to a stylized component instead.
   *
   * @return signature signs preview
   */
  boolean isPreviewSigned();

}
