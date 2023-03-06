import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEntreprise } from '../entreprise.model';
import { EntrepriseService } from '../service/entreprise.service';
import { EntrepriseDeleteDialogComponent } from '../delete/entreprise-delete-dialog.component';

@Component({
  selector: 'jhi-entreprise',
  templateUrl: './entreprise.component.html',
})
export class EntrepriseComponent implements OnInit {
  entreprises?: IEntreprise[];
  isLoading = false;

  constructor(protected entrepriseService: EntrepriseService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.entrepriseService.query().subscribe({
      next: (res: HttpResponse<IEntreprise[]>) => {
        this.isLoading = false;
        this.entreprises = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IEntreprise): number {
    return item.id!;
  }

  delete(entreprise: IEntreprise): void {
    const modalRef = this.modalService.open(EntrepriseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.entreprise = entreprise;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
