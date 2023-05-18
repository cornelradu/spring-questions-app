import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Question } from 'src/app/common/question';
import { AskquestionService } from 'src/app/services/askquestion.service';

@Component({
  selector: 'app-answer-question',
  templateUrl: './answer-question.component.html',
  styleUrls: ['./answer-question.component.css']
})
export class AnswerQuestionComponent {
  public answerFormGroup!: FormGroup;
  question!: Question;

  constructor(private formBuilder: FormBuilder, private askquestionService: AskquestionService, private router: Router,private route: ActivatedRoute){

  }

  ngOnInit(): void {
    this.answerFormGroup = this.formBuilder.group({
      user: this.formBuilder.group({
        answer: [''],
      })
    });

    this.route.paramMap.subscribe(()=>
    {  this.handleProductDetails();}
    )
  }

  handleProductDetails(): void {
    const theQuestionId: number = + this.route.snapshot.paramMap.get('id')!;
    console.log(theQuestionId)
    this.askquestionService.getQuestion(theQuestionId).subscribe(
      data =>
      {
          this.question = data;
      }
    )
  }

  onSubmit(){
    this.askquestionService.postanswer(this.question.id, this.answerFormGroup?.get('user')?.value['answer']).subscribe(
      data => {
         this.router.navigate(['/home']);
      }
    );
  }
}
