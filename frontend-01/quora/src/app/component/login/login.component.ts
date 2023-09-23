import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { MatDialog,MatDialogConfig } from '@angular/material/dialog';

import { InfoDialogComponent } from '../../info-dialog/info-dialog.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  public loginFormGroup!: FormGroup;

  constructor(private formBuilder: FormBuilder, private authenticationService: AuthenticationService, private router: Router, public dialog: MatDialog){

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
    const error_callback = (message: string) : void => {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.disableClose = true; // Disables closing the dialog by clicking outside
        dialogConfig.autoFocus = true; // Focuses the first focusable element in the dialog
        dialogConfig.width = '400px'; // Sets the width of the dialog
        dialogConfig.data = { message: message }; // Passes data to the dialog
        const dialogRef = this.dialog.open(InfoDialogComponent, dialogConfig);
    }
    //const dialogRef = this.dialog.open(InfoDialogComponent, {
    //  width: '300px', // You can adjust the width as needed
    //});
    this.authenticationService.login(this.loginFormGroup?.get('user')?.value['name'],
    this.loginFormGroup?.get('user')?.value['password'], callback, error_callback)
    
  }
}
