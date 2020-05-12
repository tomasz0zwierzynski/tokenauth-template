import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiService, ENDPOINTS } from '../auth/http/api.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor( private http: HttpClient, private api: ApiService ) { }

  ngOnInit(): void {
  }

  test() {
    this.api.get(ENDPOINTS.CURRENT_USER)
    // this.http.get(`http://localhost:8080/auth/current`)
    .subscribe( data => {
      console.log( data );
    })
  }

}
