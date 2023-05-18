import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  getToken() {
    return localStorage.getItem("token");
  }

  private token: string = "";

  constructor(private httpClient: HttpClient) { }

  private baseUrl = "http://localhost:8080/api/v1/auth/register"
  private loginUrl = "http://localhost:8080/api/v1/auth/authenticate"

  register(name: string, email: string, password: string){
    this.httpClient.post<GetResponse>(this.baseUrl, { name: name, email: email, password: password }).subscribe(data => {
        this.token = data.token

        console.log(this.token)
    })
  }

  login(name: string, password: string,  callback: () => void){
    this.httpClient.post<GetResponse>(this.loginUrl, { name: name, password: password }).subscribe(data => {
        this.token = data.token
        callback();
        console.log(this.token)
        localStorage.setItem('token', this.token)
    })
  }
}

interface GetResponse {
  token : string;
}

