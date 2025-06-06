import {ApplicationConfig, inject, provideAppInitializer, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {MessageService} from 'primeng/api';
import {keycloakHttpInterceptor} from './utils/http/keycloak-http.interceptor';
import {KeycloakService} from './utils/keycloak/keycloak.service';
import {ToastModule} from 'primeng/toast';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([keycloakHttpInterceptor])
    ), MessageService, ToastModule,
    provideAppInitializer(() => {
      const initFn = ((key: KeycloakService) => {
        return () => key.init()
      })(inject(KeycloakService));
      return initFn();
    })
  ]

};
