/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.exbin.framework.gui.options.api;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.gui.utils.ComponentResourceProvider;

/**
 * Interface for basic options panels.
 *
 * @version 0.2.1 2019/07/13
 * @author ExBin Project (http://exbin.org)
 * @param <T> options data class
 */
@ParametersAreNonnullByDefault
public interface OptionsCapable<T extends OptionsData> extends ComponentResourceProvider {

    /**
     * Loads configuration from given options data.
     *
     * @param options
     */
    void loadFromOptions(T options);

    /**
     * Saves configuration from given options data.
     *
     * @param options
     */
    void saveToOptions(T options);

    /**
     * Registers listener for changes monitoring.
     *
     * @param listener modified options listener
     */
    void setOptionsModifiedListener(OptionsModifiedListener listener);
}
