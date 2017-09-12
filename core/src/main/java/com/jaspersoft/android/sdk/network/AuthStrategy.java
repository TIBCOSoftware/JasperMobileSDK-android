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

package com.jaspersoft.android.sdk.network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class AuthStrategy {
    @NotNull
    private final SpringAuthServiceFactory springAuthServiceFactory;
    @NotNull
    private final PreAuthenticationServiceFactory preAuthenticationServiceFactory;
    @NotNull
    private final SingleSignOnServiceFactory singleSignOnServiceFactory;
    @NotNull
    private final AuthenticationLifecycle authenticationLifecycle;

    @Nullable
    private SpringAuthService springAuthService;
    @Nullable
    private PreAuthenticationLoginService preAuthenticationLoginService;
    @Nullable
    private SingleSignOnLoginService singleSignOnLoginService;

    @TestOnly
    AuthStrategy(@NotNull SpringAuthServiceFactory springAuthServiceFactory,
                 @NotNull PreAuthenticationServiceFactory preAuthenticationServiceFactory, @NotNull SingleSignOnServiceFactory singleSignOnServiceFactory, @NotNull AuthenticationLifecycle authenticationLifecycle) {
        this.springAuthServiceFactory = springAuthServiceFactory;
        this.preAuthenticationServiceFactory = preAuthenticationServiceFactory;
        this.singleSignOnServiceFactory = singleSignOnServiceFactory;
        this.authenticationLifecycle = authenticationLifecycle;
    }

    void apply(Credentials credentials) throws IOException, HttpException {
        authenticationLifecycle.beforeSessionReload();
        if (credentials instanceof SpringCredentials) {
            springLoginService().authenticate((SpringCredentials) credentials);
        } else if (credentials instanceof PreAuthenticationCredentials) {
            preAuthLoginService().authenticate((PreAuthenticationCredentials) credentials);
        } else if (credentials instanceof SingleSignOnCredentials) {
            singleSignOnLoginService().authenticate((SingleSignOnCredentials) credentials);
        } else {
            throw new IllegalArgumentException(credentials.getClass().getName() + " credentials is not supported!");
        }
        authenticationLifecycle.afterSessionReload();
    }

    private SpringAuthService springLoginService() {
        if (springAuthService == null) {
            springAuthService = springAuthServiceFactory.create();
        }
        return springAuthService;
    }

    private PreAuthenticationLoginService preAuthLoginService() {
        if (preAuthenticationLoginService == null) {
            preAuthenticationLoginService = preAuthenticationServiceFactory.create();
        }
        return preAuthenticationLoginService;
    }

    private SingleSignOnLoginService singleSignOnLoginService() {
        if (singleSignOnLoginService == null) {
            singleSignOnLoginService = singleSignOnServiceFactory.create();
        }
        return singleSignOnLoginService;
    }
}
