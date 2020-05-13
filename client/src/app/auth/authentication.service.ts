import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from './model/user';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { ApiService, ENDPOINTS } from './http/api.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private currentUserSubject: BehaviorSubject<User>;
  private currentUser: Observable<User>;

  constructor( private http: HttpClient, private api: ApiService ) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
   }

   public get currentUserValue(): User {
     return this.currentUserSubject.value;
   }

   public hasRole(role: string): boolean {
    if ( this.currentUserSubject.value ) {
      return this.currentUserSubject.value.roles.includes( role );
    } else {
      return false;
    }
   }

   login(username, password) {
     return this.api.post<any>(ENDPOINTS.TOKEN, {username: username, password: password} )
     .pipe( map( (response: any) => {
      let user = new User;
      user.token = response.accessToken;
      user.username = response.username;
      user.roles = response.roles;

      localStorage.setItem('currentUser', JSON.stringify(user));
      this.currentUserSubject.next(user);

      return user;
     }) );
   }

   logout() {
     localStorage.removeItem('currentUser');
     this.currentUserSubject.next(null);
   }

}
