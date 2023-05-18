import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './app.component';
import { RegisterComponent } from './components/register/register.component';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './component/home/home.component';
import { LoginComponent } from './component/login/login.component';
import { AskQuestionComponent } from './components/ask-question/ask-question.component';
import { AnswerQuestionComponent } from './components/answer-question/answer-question.component';
import { SearchComponent } from './components/search/search.component';

const routes: Routes = [
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: 'home', component: HomeComponent},
  {path: 'ask_question', component: AskQuestionComponent},
  {path: 'answer_question/:id', component: AnswerQuestionComponent},
  {path: 'home/:keyword', component: HomeComponent},
];

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    HomeComponent,
    LoginComponent,
    AskQuestionComponent,
    AnswerQuestionComponent,
    SearchComponent,
  ],
  imports: [
    RouterModule,
    RouterModule.forRoot(routes),
    BrowserModule,
    NgbModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
