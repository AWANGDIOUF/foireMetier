import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IObjectif } from '../objectif.model';
import { ObjectifService } from '../service/objectif.service';
import { ObjectifDeleteDialogComponent } from '../delete/objectif-delete-dialog.component';

@Component({
  selector: 'jhi-objectif',
  templateUrl: './objectif.component.html',
})
export class ObjectifComponent implements OnInit {
  objectifs?: IObjectif[];
  isLoading = false;

  constructor(protected objectifService: ObjectifService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.objectifService.query().subscribe({
      next: (res: HttpResponse<IObjectif[]>) => {
        this.isLoading = false;
        this.objectifs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IObjectif): number {
    return item.id!;
  }

  delete(objectif: IObjectif): void {
    const modalRef = this.modalService.open(ObjectifDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.objectif = objectif;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
