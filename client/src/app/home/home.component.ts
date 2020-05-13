import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiService, ENDPOINTS } from '../auth/http/api.service';
import { AuthenticationService } from '../auth/authentication.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor( private http: HttpClient, private api: ApiService, private authenticationService: AuthenticationService ) { }

  ngOnInit(): void {
  }

  logout() {
    this.authenticationService.logout(); 
  }

}
