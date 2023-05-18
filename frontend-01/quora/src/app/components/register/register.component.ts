import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerFormGroup!: FormGroup;

  constructor(private formBuilder: FormBuilder, private authenticationService: AuthenticationService, private router: Router){

  }

  ngOnInit(): void {
    this.registerFormGroup = this.formBuilder.group({
      user: this.formBuilder.group({
        name: [''],
        email: [''],
        password: [''],
        password_2: ['']
      })
    });
  }

  onSubmit(){
    console.log(this.registerFormGroup?.get('user')?.value)
    this.authenticationService.register(this.registerFormGroup?.get('user')?.value['name'],
    this.registerFormGroup?.get('user')?.value['email'],
    this.registerFormGroup?.get('user')?.value['password'])
    this.router.navigate(['/home']);
  }

}
