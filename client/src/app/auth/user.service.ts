import { Injectable } from '@angular/core';
import { ApiService, ENDPOINTS } from './http/api.service';
import { NewUser } from './model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor( private api: ApiService ) { }

  register(user: NewUser) {
    return this.api.post(ENDPOINTS.REGISTER_USER, null, user);
  }

}
