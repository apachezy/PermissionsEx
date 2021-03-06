/*
 * PermissionsEx
 * Copyright (C) zml and PermissionsEx contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.stellardrift.permissionsex.util.glob;

/**
 * This error is thrown when invalid glob syntax is presented
 */
public class GlobParseException extends Exception {
    private static final long serialVersionUID = -1855597619477016702L;

    GlobParseException(String s) {
        super(s);
    }

    GlobParseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    GlobParseException(Throwable throwable) {
        super(throwable);
    }
}
