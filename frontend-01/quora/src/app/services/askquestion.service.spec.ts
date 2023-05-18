import { TestBed } from '@angular/core/testing';

import { AskquestionService } from './askquestion.service';

describe('AskquestionService', () => {
  let service: AskquestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AskquestionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
