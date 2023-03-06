import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISalonMetier } from '../salon-metier.model';
import { SalonMetierService } from '../service/salon-metier.service';

@Component({
  templateUrl: './salon-metier-delete-dialog.component.html',
})
export class SalonMetierDeleteDialogComponent {
  salonMetier?: ISalonMetier;

  constructor(protected salonMetierService: SalonMetierService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.salonMetierService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
