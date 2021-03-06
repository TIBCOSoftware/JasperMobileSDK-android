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

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ReportScheduleRestApi;
import com.jaspersoft.android.sdk.network.entity.schedule.JobDescriptor;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnitEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobData;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ReportScheduleUseCase {
    private final ServiceExceptionMapper mExceptionMapper;
    private final ReportScheduleRestApi mScheduleRestApi;
    private final InfoCacheManager mCacheManager;
    private final JobSearchCriteriaMapper mSearchCriteriaMapper;
    private final JobDataMapper mJobDataMapper;
    private final JobFormMapper mJobFormMapper;
    private final JobUnitMapper mJobUnitMapper;

    ReportScheduleUseCase(ServiceExceptionMapper exceptionMapper,
                          ReportScheduleRestApi scheduleRestApi,
                          InfoCacheManager cacheManager,
                          JobSearchCriteriaMapper searchCriteriaMapper,
                          JobDataMapper jobDataMapper,
                          JobFormMapper jobFormMapper,
                          JobUnitMapper jobUnitMapper
    ) {
        mExceptionMapper = exceptionMapper;
        mScheduleRestApi = scheduleRestApi;
        mCacheManager = cacheManager;
        mSearchCriteriaMapper = searchCriteriaMapper;
        mJobDataMapper = jobDataMapper;
        mJobFormMapper = jobFormMapper;
        mJobUnitMapper = jobUnitMapper;
    }

    public List<JobUnit> searchJob(JobSearchCriteria criteria) throws ServiceException {
        try {
            Map<String, Object> searchParams = mSearchCriteriaMapper.transform(criteria);
            List<JobUnitEntity> jobUnitEntities = mScheduleRestApi.searchJob(searchParams);
            return mJobUnitMapper.transform(jobUnitEntities);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public JobData createJob(JobForm form) throws ServiceException {
        return alterJob(form, null);
    }

    public JobData updateJob(int id, JobForm form) throws ServiceException {
        return alterJob(form, id);
    }

    private JobData alterJob(JobForm form, Integer id) throws ServiceException {
        try {
            ServerInfo info = mCacheManager.getInfo();
            JobFormEntity formEntity = mJobFormMapper.toFormEntity(form);
            JobDescriptor jobDescriptor;
            if (id == null) {
                jobDescriptor = mScheduleRestApi.createJob(formEntity);
            } else {
                jobDescriptor = mScheduleRestApi.updateJob(id, formEntity);
            }
            return mJobDataMapper.transform(jobDescriptor, info.getDatetimeFormatPattern());
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public Set<Integer> deleteJobs(Set<Integer> jobIds) throws ServiceException {
        try {
            return mScheduleRestApi.deleteJobs(jobIds);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public JobForm readJob(int jobId) throws ServiceException {
        try {
            JobFormEntity entity = mScheduleRestApi.requestJob(jobId);
            return mJobFormMapper.toDataForm(entity);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        }
    }
}
