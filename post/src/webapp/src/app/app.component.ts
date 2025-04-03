import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  // title = 'webapp';
  // authenticated = false;
  // constructor(private readonly keycloak: Keycloak) {
  //   const keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL);
  //
  //   effect(() => {
  //     const keycloakEvent = keycloakSignal();
  //
  //     if (keycloakEvent.type === KeycloakEventType.Ready) {
  //       this.authenticated = typeEventArgs<ReadyArgs>(keycloakEvent.args);
  //     }
  //
  //     if (keycloakEvent.type === KeycloakEventType.AuthLogout) {
  //       this.authenticated = false;
  //     }
  //     this.checkSession();
  //   });
  // }
  // async checkSession() {
  //   const isLoggedIn = await this.keycloak.authenticated;
  //   if (isLoggedIn) {
  //     console.log('Người dùng đã đăng nhập, không cần gọi lại API.');
  //   } else {
  //     console.log('Người dùng chưa đăng nhập, có thể cần lấy token mới.');
  //   }
  // }
}
