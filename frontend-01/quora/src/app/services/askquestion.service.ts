import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EventEmitter, Injectable, Output } from '@angular/core';
import { Question } from '../common/question';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AskquestionService {
  public changeSearch(searchAll: boolean) {
    this.searchChangeEvent.emit("" + searchAll);
  }
  
  @Output() searchChangeEvent = new EventEmitter<string>();


  
  private baseUrl = "http://localhost:8080/api/v1/question"
  private answerUrl = "http://localhost:8080/api/v1/answer"

  constructor(private authService: AuthenticationService, private httpClient: HttpClient) { }

  postquestion(question: string, callback: () => void){
    const token = this.authService.getToken();
    let headers = new HttpHeaders({
      "Content-Type": "application/json; charset=utf-8",
      Authorization: `Bearer ` + token
    });

    this.httpClient.post<any>(this.baseUrl, { question: question }, {headers: headers}).subscribe(data => {
        console.log(data)
        callback()
    })
  }

  getUserQuestions(userOnly: boolean){
    const token = this.authService.getToken();
    let headers = new HttpHeaders({
      "Content-Type": "application/json; charset=utf-8",
      Authorization: `Bearer ` + token
    });
    return this.httpClient.get<Question[]>(this.baseUrl + "?searchAll=" + (userOnly == false), {headers: headers})
  }

  searchUserQuestions(keyword: String, userOnly: boolean){
    const token = this.authService.getToken();
    let headers = new HttpHeaders({
      "Content-Type": "application/json; charset=utf-8",
      Authorization: `Bearer ` + token
    });
    return this.httpClient.get<Question[]>(this.baseUrl + "?keyword=" + keyword + '&' + "searchAll=" + (userOnly == false) , {headers: headers})
  }

  getQuestion(theQuestionId: number) {
    const token = this.authService.getToken();
    let headers = new HttpHeaders({
      "Content-Type": "application/json; charset=utf-8",
      Authorization: `Bearer ` + token
    });
    return this.httpClient.get<Question>(this.baseUrl + "/" + theQuestionId, {headers: headers})
  }

  postanswer(question_id: number, answer_text: string) {
    const token = this.authService.getToken();
    let headers = new HttpHeaders({
      "Content-Type": "application/json; charset=utf-8",
      Authorization: `Bearer ` + token
    });
    return this.httpClient.post<Question>(this.baseUrl + "/" + question_id + "/answer", {answer: answer_text}, {headers: headers})
  }

  deleteAnswer(id: number) {
    const token = this.authService.getToken();
    let headers = new HttpHeaders({
      "Content-Type": "application/json; charset=utf-8",
      Authorization: `Bearer ` + token
    });
    return this.httpClient.delete<any>(this.answerUrl + "/" + id,{headers: headers});
  }

  deleteQuestion(id: number){
    const token = this.authService.getToken();
    let headers = new HttpHeaders({
      "Content-Type": "application/json; charset=utf-8",
      Authorization: `Bearer ` + token
    });
    return this.httpClient.delete<any>(this.baseUrl + "/" + id,{headers: headers});
  }

  vote(qId: number, voteDirection: string) {
    const token = this.authService.getToken();
    let headers = new HttpHeaders({
      "Content-Type": "application/json; charset=utf-8",
      Authorization: `Bearer ` + token
    });
    console.log(voteDirection)
    return this.httpClient.post<any>(this.baseUrl + "/" + qId + "/vote", {voteDirection: voteDirection}, {headers: headers});
  }
}



