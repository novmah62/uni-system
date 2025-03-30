import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideHttpClient} from '@angular/common/http';
import {MessageService} from 'primeng/api';
import {AutoRefreshTokenService, provideKeycloak, UserActivityService, withAutoRefreshToken} from 'keycloak-angular';

export const appConfig: ApplicationConfig = {
  providers: [provideKeycloak({
    config: {
      url: 'http://localhost:8080',
      realm: 'uni-system',
      clientId: 'post-ui',
    },
    initOptions: {
      onLoad: 'check-sso',
      silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      checkLoginIframe: false
    },
    features: [
      withAutoRefreshToken({
        onInactivityTimeout: 'logout',
        sessionTimeout: 60000
      })
    ],
    providers: [AutoRefreshTokenService, UserActivityService]
  }), provideZoneChangeDetection({eventCoalescing: true}), provideRouter(routes), provideHttpClient(), MessageService]
};
