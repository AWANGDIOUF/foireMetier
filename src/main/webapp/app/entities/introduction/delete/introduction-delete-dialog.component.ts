import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIntroduction } from '../introduction.model';
import { IntroductionService } from '../service/introduction.service';

@Component({
  templateUrl: './introduction-delete-dialog.component.html',
})
export class IntroductionDeleteDialogComponent {
  introduction?: IIntroduction;

  constructor(protected introductionService: IntroductionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.introductionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
