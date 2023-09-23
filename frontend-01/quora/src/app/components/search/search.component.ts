import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AskquestionService } from 'src/app/services/askquestion.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { AuxServiceService } from 'src/app/aux-service.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {

  searchAll: boolean = false;
  username: string = '';

  ngOnInit(): void {
    this.searchAll = false;

    

    this.aux_service.message$.subscribe(message => {
        this.getUsername()
    })
  }

  getUsername(){
    const token = this.authService.getToken();
    let headers = new HttpHeaders({
      "Content-Type": "application/json; charset=utf-8",
      Authorization: `Bearer ` + token
    });

    this.httpClient.get("http://localhost:8080/api/v1/user", {headers: headers}).subscribe(data => {
        const dictionary: { [key: string]: any } = data;

        this.username = dictionary['username']

    });
  }

  logout() {
    this.authService.logout()
    this.router.navigate(['/login']);
    
  }

  constructor(private router: Router, private askQuestionService:  AskquestionService, private authService: AuthenticationService, private httpClient: HttpClient, private aux_service: AuxServiceService) { }


  doSearch(keyword: string){
    this.router.navigateByUrl(`/home/${keyword}`);
  }

  changeSearch() {
    this.searchAll = !this.searchAll;
    this.askQuestionService.changeSearch(this.searchAll)
  }
}
