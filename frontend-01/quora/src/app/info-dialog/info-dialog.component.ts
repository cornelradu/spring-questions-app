import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA,MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-info-dialog',
  templateUrl: './info-dialog.component.html',
})
export class InfoDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,private dialogRef: MatDialogRef<InfoDialogComponent>) {
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

}