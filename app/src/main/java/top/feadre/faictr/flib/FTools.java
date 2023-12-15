/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package top.feadre.faictr.flib;

import com.xuexiang.xtask.logger.TaskLogger;

public class FTools {
    public static void log_d(String tag, String fun, String text) {
        TaskLogger.setTag(tag);
        TaskLogger.d(fun + ": " + text);
    }

    public static void log_d(String tag, String fun) {
        log_d(tag, fun, "");
    }

    public static void log_e(String tag, String fun, String text) {
        TaskLogger.setTag(tag);
        TaskLogger.e(fun + ": " + text);
    }

    public static void log_e(String tag, String fun) {
        log_e(tag, fun, "");
    }

    public static void log_w(String tag, String fun, String text) {
        TaskLogger.setTag(tag);
        TaskLogger.w(fun + ": " + text);
    }

    public static void log_w(String tag, String fun) {
        log_w(tag, fun, "");
    }
}
