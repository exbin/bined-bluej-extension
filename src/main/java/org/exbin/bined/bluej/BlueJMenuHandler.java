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
package org.exbin.bined.bluej;

import bluej.extensions.BClass;
import bluej.extensions.BObject;
import bluej.extensions.BPackage;
import bluej.extensions.BlueJ;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Interface for for menu handling.
 *
 * @version 0.2.0 2019/05/12
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface BlueJMenuHandler {

    @Nonnull
    BlueJ getBlueJ();

    @Nullable
    BPackage getCurPackage();

    @Nullable
    BClass getCurClass();

    @Nullable
    BObject getCurObject();
}
