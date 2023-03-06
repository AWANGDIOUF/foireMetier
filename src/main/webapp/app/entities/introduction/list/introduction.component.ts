import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIntroduction } from '../introduction.model';
import { IntroductionService } from '../service/introduction.service';
import { IntroductionDeleteDialogComponent } from '../delete/introduction-delete-dialog.component';

@Component({
  selector: 'jhi-introduction',
  templateUrl: './introduction.component.html',
})
export class IntroductionComponent implements OnInit {
  introductions?: IIntroduction[];
  isLoading = false;

  constructor(protected introductionService: IntroductionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.introductionService.query().subscribe({
      next: (res: HttpResponse<IIntroduction[]>) => {
        this.isLoading = false;
        this.introductions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IIntroduction): number {
    return item.id!;
  }

  delete(introduction: IIntroduction): void {
    const modalRef = this.modalService.open(IntroductionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.introduction = introduction;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
