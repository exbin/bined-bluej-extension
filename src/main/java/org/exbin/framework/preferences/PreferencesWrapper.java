/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.framework.preferences;

import bluej.extensions.BlueJ;
import java.util.Base64;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.api.Preferences;

/**
 * Wrapper for preferences.
 *
 * @version 0.2.0 2019/07/22
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class PreferencesWrapper implements Preferences {

    private final BlueJ bluej;

    public PreferencesWrapper(BlueJ bluej) {
        this.bluej = bluej;
    }

    @Override
    public boolean exists(String key) {
        return bluej.getExtensionPropertyString(key, null) != null;
    }

    @Override
    public String get(String key) {
        return get(key, null);
    }

    @Override
    public String get(String key, @Nullable String def) {
        return bluej.getExtensionPropertyString(key, def);
    }

    @Override
    public void put(String key, @Nullable String value) {
        if (value == null) {
            bluej.setExtensionPropertyString(key, null);
        } else {
            bluej.setExtensionPropertyString(key, value);
        }
    }

    @Override
    public void remove(String key) {
        bluej.setExtensionPropertyString(key, null);
    }

    @Override
    public void putInt(String key, int value) {
        bluej.setExtensionPropertyString(key, String.valueOf(value));
    }

    @Override
    public int getInt(String key, int def) {
        return Integer.valueOf(bluej.getExtensionPropertyString(key, String.valueOf(def)));
    }

    @Override
    public void putLong(String key, long value) {
        bluej.setExtensionPropertyString(key, String.valueOf(value));
    }

    @Override
    public long getLong(String key, long def) {
        return Long.valueOf(bluej.getExtensionPropertyString(key, String.valueOf(def)));
    }

    @Override
    public void putBoolean(String key, boolean value) {
        bluej.setExtensionPropertyString(key, String.valueOf(value));
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        return Boolean.valueOf(bluej.getExtensionPropertyString(key, String.valueOf(def)));
    }

    @Override
    public void putFloat(String key, float value) {
        bluej.setExtensionPropertyString(key, String.valueOf(value));
    }

    @Override
    public float getFloat(String key, float def) {
        return Float.valueOf(bluej.getExtensionPropertyString(key, String.valueOf(def)));
    }

    @Override
    public void putDouble(String key, double value) {
        bluej.setExtensionPropertyString(key, String.valueOf(value));
    }

    @Override
    public double getDouble(String key, double def) {
        return Double.valueOf(bluej.getExtensionPropertyString(key, String.valueOf(def)));
    }

    @Override
    public void putByteArray(String key, byte[] value) {
        bluej.setExtensionPropertyString(key, Base64.getEncoder().encodeToString(value));
    }

    @Override
    public byte[] getByteArray(String key, byte[] def) {
        return Base64.getDecoder().decode(bluej.getExtensionPropertyString(key, Base64.getEncoder().encodeToString(def)));
    }

    @Override
    public void flush() {
    }

    @Override
    public void sync() {
    }
}
