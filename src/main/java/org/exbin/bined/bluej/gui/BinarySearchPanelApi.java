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
package org.exbin.bined.bluej.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.bined.ReplaceParameters;
import org.exbin.framework.bined.SearchParameters;

/**
 * Binary search panel interface.
 *
 * @version 0.2.0 2019/03/01
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface BinarySearchPanelApi {

    void performFind(SearchParameters dialogSearchParameters);

    void setMatchPosition(int matchPosition);

    void updatePosition();

    void performReplace(SearchParameters searchParameters, ReplaceParameters replaceParameters);

    void clearMatches();
}
