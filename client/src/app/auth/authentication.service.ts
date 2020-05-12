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

   login(username, password) {
     return this.api.post<any>(ENDPOINTS.TOKEN, {username: username, password: password} )
    //  return this.http.post<any>(`http://localhost:8080/auth/token?username=${username}&password=${password}`, {} )
     .pipe( map( (response: any) => {
      let user = new User;
      user.token = response.accessToken;
      user.username = username;

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
