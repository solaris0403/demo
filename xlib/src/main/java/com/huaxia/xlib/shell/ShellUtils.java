package com.huaxia.xlib.shell;

import androidx.annotation.NonNull;


import com.huaxia.xlib.common.Utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * shell 工具类。
 * 部分来自 https://github.com/Blankj/AndroidUtilCode
 *
 * @author xzy
 */
@SuppressWarnings("unused")
public final class ShellUtils {

    private static final String LINE_SEP = System.getProperty("line.separator");

    private ShellUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Execute the command asynchronously.
     *
     * @param command  The command.
     * @param isRooted True to use root, false otherwise.
     * @param callback The callback.
     * @return the task
     */
    public static Utils.Task<CommandResult> execCmdAsync(final String command,
                                                         final boolean isRooted,
                                                         final Utils.Callback<CommandResult>
                                                                 callback) {
        return execCmdAsync(new String[]{command}, isRooted, true, callback);
    }

    /**
     * Execute the command asynchronously.
     *
     * @param commands The commands.
     * @param isRooted True to use root, false otherwise.
     * @param callback The callback.
     * @return the task
     */
    public static Utils.Task<CommandResult> execCmdAsync(final List<String> commands,
                                                         final boolean isRooted,
                                                         final Utils.Callback<CommandResult>
                                                                 callback) {
        return execCmdAsync(commands == null ? null : commands.toArray(new String[]{}), isRooted,
                true, callback);
    }

    /**
     * Execute the command asynchronously.
     *
     * @param commands The commands.
     * @param isRooted True to use root, false otherwise.
     * @param callback The callback.
     * @return the task
     */
    public static Utils.Task<CommandResult> execCmdAsync(final String[] commands,
                                                         final boolean isRooted,
                                                         final Utils.Callback<CommandResult>
                                                                 callback) {
        return execCmdAsync(commands, isRooted, true, callback);
    }

    /**
     * Execute the command asynchronously.
     *
     * @param command         The command.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @param callback        The callback.
     * @return the task
     */
    public static Utils.Task<CommandResult> execCmdAsync(final String command,
                                                         final boolean isRooted,
                                                         final boolean isNeedResultMsg,
                                                         final Utils.Callback<CommandResult>
                                                                 callback) {
        return execCmdAsync(new String[]{command}, isRooted, isNeedResultMsg, callback);
    }

    /**
     * Execute the command asynchronously.
     *
     * @param commands        The commands.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @param callback        The callback.
     * @return the task
     */
    public static Utils.Task<CommandResult> execCmdAsync(final List<String> commands,
                                                         final boolean isRooted,
                                                         final boolean isNeedResultMsg,
                                                         final Utils.Callback<CommandResult>
                                                                 callback) {
        return execCmdAsync(commands == null ? null : commands.toArray(new String[]{}),
                isRooted,
                isNeedResultMsg,
                callback);
    }

    /**
     * Execute the command asynchronously.
     *
     * @param commands        The commands.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @param callback        The callback.
     * @return the task
     */
    public static Utils.Task<CommandResult> execCmdAsync(final String[] commands,
                                                         final boolean isRooted,
                                                         final boolean isNeedResultMsg,
                                                         @NonNull final
                                                         Utils.Callback<CommandResult> callback) {
        return Utils.doAsync(new Utils.Task<CommandResult>(callback) {
            @Override
            public CommandResult doInBackground() {
                return execCmd(commands, isRooted, isNeedResultMsg);
            }
        });
    }

    /**
     * Execute the command.
     *
     * @param command  The command.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command, final boolean isRooted) {
        return execCmd(new String[]{command}, isRooted, true);
    }

    /**
     * Execute the command.
     *
     * @param commands The commands.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final List<String> commands, final boolean isRooted) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}), isRooted,
                true);
    }

    /**
     * Execute the command.
     *
     * @param commands The commands.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String[] commands, final boolean isRooted) {
        return execCmd(commands, isRooted, true);
    }

    /**
     * Execute the command.
     *
     * @param command         The command.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, isRooted, isNeedResultMsg);
    }

    /**
     * Execute the command.
     *
     * @param commands        The commands.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final List<String> commands,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}),
                isRooted,
                isNeedResultMsg);
    }

    /**
     * Execute the command.
     *
     * @param commands        The commands.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String[] commands,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, "", "");
        }
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRooted ? "su" : "sh");
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) continue;
                os.write(command.getBytes());
                os.writeBytes(LINE_SEP);
                os.flush();
            }
            os.writeBytes("exit" + LINE_SEP);
            os.flush();
            result = process.waitFor();
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
                );
                errorResult = new BufferedReader(
                        new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8)
                );
                String line;
                if ((line = successResult.readLine()) != null) {
                    successMsg.append(line);
                    while ((line = successResult.readLine()) != null) {
                        successMsg.append(LINE_SEP).append(line);
                    }
                }
                if ((line = errorResult.readLine()) != null) {
                    errorMsg.append(line);
                    while ((line = errorResult.readLine()) != null) {
                        errorMsg.append(LINE_SEP).append(line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (successResult != null) {
                    successResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(
                result,
                successMsg == null ? "" : successMsg.toString(),
                errorMsg == null ? "" : errorMsg.toString()
        );
    }

    /**
     * The result of command.
     */
    public static class CommandResult {
        public int result;
        public String successMsg;
        public String errorMsg;

        public CommandResult(final int result, final String successMsg, final String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return "result: " + result + "\n" +
                    "successMsg: " + successMsg + "\n" +
                    "errorMsg: " + errorMsg;
        }
    }
}
