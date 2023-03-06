import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEvenement, Evenement } from '../evenement.model';
import { EvenementService } from '../service/evenement.service';

@Component({
  selector: 'jhi-evenement-update',
  templateUrl: './evenement-update.component.html',
})
export class EvenementUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    evenement: [],
  });

  constructor(protected evenementService: EvenementService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evenement }) => {
      this.updateForm(evenement);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evenement = this.createFromForm();
    if (evenement.id !== undefined) {
      this.subscribeToSaveResponse(this.evenementService.update(evenement));
    } else {
      this.subscribeToSaveResponse(this.evenementService.create(evenement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvenement>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(evenement: IEvenement): void {
    this.editForm.patchValue({
      id: evenement.id,
      evenement: evenement.evenement,
    });
  }

  protected createFromForm(): IEvenement {
    return {
      ...new Evenement(),
      id: this.editForm.get(['id'])!.value,
      evenement: this.editForm.get(['evenement'])!.value,
    };
  }
}
