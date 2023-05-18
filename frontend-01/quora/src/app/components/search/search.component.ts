import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AskquestionService } from 'src/app/services/askquestion.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
  searchAll: boolean = false;
  
  ngOnInit(): void {
    this.searchAll = false;
  }

  constructor(private router: Router, private askQuestionService: AskquestionService) { }


  doSearch(keyword: string){
    this.router.navigateByUrl(`/home/${keyword}`);
  }

  changeSearch() {
    this.searchAll = !this.searchAll;
    this.askQuestionService.changeSearch(this.searchAll)
  }
}
