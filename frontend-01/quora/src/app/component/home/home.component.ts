import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuxServiceService } from 'src/app/services/aux-service.service';
import { Answer, Question } from 'src/app/common/question';
import { SearchComponent } from 'src/app/components/search/search.component';
import { AskquestionService } from 'src/app/services/askquestion.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  
  questionList: Question[] = [];
  searchMode: boolean = false;
  userOnly: string = 'false';
  constructor(private service: AskquestionService,private route: ActivatedRoute, private aux_service: AuxServiceService){}
  
  ngOnInit(): void {
      
      this.route.paramMap.subscribe(()=> {
        this.listAllQuestions()
      });

      this.service.searchChangeEvent
        .subscribe((data:string) => {
            this.userOnly = data;
            this.listAllQuestions()
        });
      
        this.aux_service.sendMessage("test")
        this.aux_service.message2$.subscribe(data => {
          this.listAllQuestions()
        })
  }

  listAllQuestions(){
    const searchMode = this.route.snapshot.paramMap.has('keyword');
    if (!searchMode){
      this.service.getUserQuestions(this.userOnly == 'true').subscribe(data => {
        this.questionList = data
        console.log(this.questionList)
      })
    } else {
      const theKeyWord: string = this.route.snapshot.paramMap.get('keyword')!;

      console.log(this.userOnly)
      this.service.searchUserQuestions(theKeyWord,this.userOnly == 'true').subscribe(data => {
        this.questionList = data
      })
    }
  }

  upvote(qId: number) {
    this.service.vote(qId, 'up').subscribe(data => {
      this.listAllQuestions()
    })
    
  }

  downvote(qId: number) {
    console.log("downvote")
    this.service.vote(qId, 'down').subscribe(data => {
      this.listAllQuestions()
    })
    
  }

  deleteAnswer( id: number, ids: Answer[]){
    const val = ids[id].id
    this.service.deleteAnswer(val).subscribe(data => {
      this.service.getUserQuestions(this.userOnly == 'true').subscribe(data => {
        this.questionList = data
      })
    })
  }

  deleteQuestion(id: number){
    this.service.deleteQuestion(id).subscribe(data => {
      this.service.getUserQuestions(this.userOnly == 'true').subscribe(data => {
        this.questionList = data
      })
    })
  }

    

  
}
