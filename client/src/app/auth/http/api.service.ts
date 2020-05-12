import { Injectable } from '@angular/core';
import { HttpClient } from '../../../../node_modules/@angular/common/http';
import { Observable } from '../../../../node_modules/rxjs';

const apiUrl = `http://localhost:8080`;

export const ENDPOINTS = {

    'TOKEN': `${apiUrl}/auth/token`,
    'CURRENT_USER': `${apiUrl}/auth/current`,

    'REGISTER_USER': `${apiUrl}/manage/register`,
    'ACTIVATE_USER': `${apiUrl}/manage/activate`,
    'USER_ROLES': `${apiUrl}/manage/roles`,

    'API_RESOURCE': `${apiUrl}/api/resource`,
    'API_MANAGE': `${apiUrl}/api/manage`

}

@Injectable( {providedIn: 'root'} )
export class ApiService {
    
    constructor( private http: HttpClient ) {
    }
    
    get<T>(endpoint: string, queryParams?: any): Observable<T> {
        let uri = this.resolveUri( endpoint, queryParams );
        
        return this.http.get<T>(uri);
    }

    post<T>(endpoint: string, queryParams?: any, body: any = {}) {
        let uri = this.resolveUri( endpoint, queryParams );

        return this.http.post<T>(uri, body);
    }

    private resolveUri(base: string, queryParams?: any): string {
        let uri = base;
        if ( queryParams ) {
            uri += '?'
            Object.keys(queryParams).forEach( key => {
                uri += key + '=' + queryParams[key] + '&';
            });
            uri = uri.substring(0, uri.length - 1);
        }
        return uri;
    }

}