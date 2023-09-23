import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { InfoDialogComponent } from '../../info-dialog/info-dialog.component';
import { MatDialog,MatDialogConfig } from '@angular/material/dialog';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerFormGroup!: FormGroup;

  constructor(private formBuilder: FormBuilder, private authenticationService: AuthenticationService, private router: Router, public dialog: MatDialog){

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

    const callback = () : void => { this.router.navigate(['/home']); }
    const error_callback = (message: string) : void => {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.disableClose = true; // Disables closing the dialog by clicking outside
        dialogConfig.autoFocus = true; // Focuses the first focusable element in the dialog
        dialogConfig.width = '400px'; // Sets the width of the dialog
        dialogConfig.data = { message: message }; // Passes data to the dialog
        const dialogRef = this.dialog.open(InfoDialogComponent, dialogConfig);
    }
    this.authenticationService.register(this.registerFormGroup?.get('user')?.value['name'],
    this.registerFormGroup?.get('user')?.value['email'],
    this.registerFormGroup?.get('user')?.value['password'], callback, error_callback)
  }

}
