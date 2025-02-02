/*
 * Copyright (C) 2020-2023 Velocity Contributors
 *
 * The Velocity API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the api top-level directory.
 */

package com.velocitypowered.api.event.command;

import com.google.common.base.Preconditions;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.annotation.AwaitingEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * This event is fired when someone executes a command. Velocity will wait for this event to finish
 * firing before trying to handle the command and/or forwarding it to the server.
 */
@AwaitingEvent
public final class CommandExecuteEvent implements ResultedEvent<CommandResult> {

  private final CommandSource commandSource;
  private final String command;
  private CommandResult result;

  /**
   * Constructs a CommandExecuteEvent.
   *
   * @param commandSource the source executing the command
   * @param command       the command being executed without first slash
   */
  public CommandExecuteEvent(CommandSource commandSource, String command) {
    this.commandSource = Preconditions.checkNotNull(commandSource, "commandSource");
    this.command = Preconditions.checkNotNull(command, "command");
    this.result = CommandResult.allow();
  }

  public CommandSource getCommandSource() {
    return commandSource;
  }

  /**
   * Gets the original command being executed without the first slash.
   *
   * @return the original command being executed
   */
  public String getCommand() {
    return command;
  }

  @Override
  public CommandResult result() {
    return result;
  }

  @Override
  public void setResult(CommandResult result) {
    this.result = Preconditions.checkNotNull(result, "result");
  }

  @Override
  public String toString() {
    return "CommandExecuteEvent{"
        + "commandSource=" + commandSource
        + ", command=" + command
        + ", result=" + result
        + '}';
  }

  /**
   * Represents the result of the {@link CommandExecuteEvent}.
   */
  public static final class CommandResult implements ResultedEvent.Result {

    private static final CommandResult ALLOWED = new CommandResult(true, false, null);
    private static final CommandResult DENIED = new CommandResult(false, false, null);
    private static final CommandResult FORWARD_TO_SERVER = new CommandResult(false, true, null);

    private @Nullable String command;
    private final boolean status;
    private final boolean forward;

    private CommandResult(boolean status, boolean forward, @Nullable String command) {
      this.status = status;
      this.forward = forward;
      this.command = command;
    }

    public Optional<String> getCommand() {
      return Optional.ofNullable(command);
    }

    public boolean isForwardToServer() {
      return forward;
    }

    @Override
    public boolean allowed() {
      return status;
    }

    @Override
    public String toString() {
      return status ? "allowed" : "denied";
    }

    /**
     * Allows the command to be sent, without modification.
     *
     * @return the allowed result
     */
    public static CommandResult allow() {
      return ALLOWED;
    }

    /**
     * Prevents the command from being executed.
     *
     * @return the denied result
     */
    public static CommandResult deny() {
      return DENIED;
    }

    /**
     * Forwards the command to server instead of executing it on the proxy. This is the default
     * behavior when a command is not registered on Velocity.
     *
     * @return the forward result
     */
    public static CommandResult forwardToServer() {
      return FORWARD_TO_SERVER;
    }

    /**
     * Prevents the command from being executed on proxy, but forward command to server.
     *
     * @param newCommand the command without first slash to use instead
     * @return a result with a new command being forwarded to server
     */
    public static CommandResult forwardToServer(@NonNull String newCommand) {
      Preconditions.checkNotNull(newCommand, "newCommand");
      return new CommandResult(false, true, newCommand);
    }

    /**
     * Allows the command to be executed, but silently replaces the command with a different
     * command.
     *
     * @param newCommand the command to use instead without first slash
     * @return a result with a new command
     */
    public static CommandResult command(@NonNull String newCommand) {
      Preconditions.checkNotNull(newCommand, "newCommand");
      return new CommandResult(true, false, newCommand);
    }
  }
}
