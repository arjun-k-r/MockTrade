/*
 * Author: Balch
 * Created: 9/4/14 12:26 AM
 *
 * This file is part of MockTrade.
 *
 * MockTrade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MockTrade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MockTrade.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2014
 */

package com.balch.android.app.framework.bean;

public class BeanValidatorException extends Exception {
    public BeanValidatorException() {
    }

    public BeanValidatorException(String detailMessage) {
        super(detailMessage);
    }

    public BeanValidatorException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BeanValidatorException(Throwable throwable) {
        super(throwable);
    }
}
