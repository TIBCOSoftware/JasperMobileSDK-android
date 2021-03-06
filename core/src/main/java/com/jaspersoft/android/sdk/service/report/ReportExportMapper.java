/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.data.report.ReportExportOutput;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ReportExportMapper {
    public ReportExportOutput transform(final ExportOutputResource outputResource) {
        return new ReportExportOutput() {
            @Override
            public boolean isFinal() {
                return outputResource.isFinal();
            }

            @Override
            public PageRange getPages() {
                return PageRange.parse(outputResource.getPages());
            }

            @Override
            public InputStream getStream() throws IOException {
                return outputResource.getOutputResource().getStream();
            }
        };
    }
}
