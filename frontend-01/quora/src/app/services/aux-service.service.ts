import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuxServiceService {
  private messageSource = new Subject<string>();
  message$ = this.messageSource.asObservable();
  
  private messageSource2 = new Subject<string>();
  message2$ = this.messageSource.asObservable();

  sendMessage(message: string) {
    this.messageSource.next(message);
  }

  sendMessage2(message: string) {
    this.messageSource.next(message);
  }
  
  constructor() { }
}
