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
package org.exbin.framework.bined;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.bined.PositionCodeType;

/**
 * Cursor position format for status.
 *
 * @version 0.2.0 2019/03/15
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class StatusCursorPositionFormat {

    private PositionCodeType positionCodeType = PositionCodeType.DECIMAL;
    private boolean showOffset = true;

    public StatusCursorPositionFormat() {
    }

    public StatusCursorPositionFormat(PositionCodeType positionCodeType, boolean showOffset) {
        this.positionCodeType = positionCodeType;
        this.showOffset = showOffset;
    }

    @Nonnull
    public PositionCodeType getCodeType() {
        return positionCodeType;
    }

    public void setCodeType(PositionCodeType positionCodeType) {
        this.positionCodeType = Objects.requireNonNull(positionCodeType);
    }

    public boolean isShowOffset() {
        return showOffset;
    }

    public void setShowOffset(boolean showOffset) {
        this.showOffset = showOffset;
    }
}
