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

  logout(){
    localStorage.setItem("token", '')
    this.token = "";
  }

  register(name: string, email: string, password: string, callback: () => void, error_callback: (message: string) => void){
    this.httpClient.post<GetResponse>(this.baseUrl, { name: name, email: email, password: password }).subscribe(data => {
        
        if(data.errorMessage != null){
          error_callback(data.errorMessage)
        } else {
          this.token = data.token
          console.log(data)
          callback()
          localStorage.setItem('token', this.token)
        }
    },
    (error) => {
        var message = "Cannot register with that username."
        error_callback(message)
    })
  }

  login(name: string, password: string,  callback: () => void, error_callback: (message: string) => void ){
    this.httpClient.post<GetResponse>(this.loginUrl, { name: name, password: password }).subscribe(data => {
        
        this.token = data.token
        
        callback();
        console.log(this.token)
        localStorage.setItem('token', this.token)
    },
    (error) => {
      var message = "Cannot login username and password are not in database."
      error_callback(message)
    })
  }
}

interface GetResponse {
  token : string;
  errorMessage: string;
}

