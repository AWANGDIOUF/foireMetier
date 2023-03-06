import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISalonMetier } from '../salon-metier.model';
import { SalonMetierService } from '../service/salon-metier.service';
import { SalonMetierDeleteDialogComponent } from '../delete/salon-metier-delete-dialog.component';

@Component({
  selector: 'jhi-salon-metier',
  templateUrl: './salon-metier.component.html',
})
export class SalonMetierComponent implements OnInit {
  salonMetiers?: ISalonMetier[];
  isLoading = false;

  constructor(protected salonMetierService: SalonMetierService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.salonMetierService.query().subscribe({
      next: (res: HttpResponse<ISalonMetier[]>) => {
        this.isLoading = false;
        this.salonMetiers = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISalonMetier): number {
    return item.id!;
  }

  delete(salonMetier: ISalonMetier): void {
    const modalRef = this.modalService.open(SalonMetierDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.salonMetier = salonMetier;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
