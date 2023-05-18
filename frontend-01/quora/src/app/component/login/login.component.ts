import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  public loginFormGroup!: FormGroup;

  constructor(private formBuilder: FormBuilder, private authenticationService: AuthenticationService, private router: Router){

  }

  ngOnInit(): void {
    this.loginFormGroup = this.formBuilder.group({
      user: this.formBuilder.group({
        name: [''],
        password: [''],
      })
    });
  }

  onSubmit(){
    
    const callback = () : void => { this.router.navigate(['/home']); }

    this.authenticationService.login(this.loginFormGroup?.get('user')?.value['name'],
    this.loginFormGroup?.get('user')?.value['password'], callback)
    
  }
}
