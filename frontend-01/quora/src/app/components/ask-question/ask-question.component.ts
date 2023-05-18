import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AskquestionService } from 'src/app/services/askquestion.service';

@Component({
  selector: 'app-ask-question',
  templateUrl: './ask-question.component.html',
  styleUrls: ['./ask-question.component.css']
})
export class AskQuestionComponent {
  public askquestionFormGroup!: FormGroup;

  constructor(private formBuilder: FormBuilder, private askquestionService: AskquestionService, private router: Router){

  }

  ngOnInit(): void {
    this.askquestionFormGroup = this.formBuilder.group({
      user: this.formBuilder.group({
        question: [''],
      })
    });
  }

  onSubmit(){
    const callback = () : void => { this.router.navigate(['/home']); }

    this.askquestionService.postquestion(this.askquestionFormGroup?.get('user')?.value['question'], callback);
  }
}
