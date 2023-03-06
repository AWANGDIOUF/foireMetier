import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISalonMetier, SalonMetier } from '../salon-metier.model';
import { SalonMetierService } from '../service/salon-metier.service';

@Component({
  selector: 'jhi-salon-metier-update',
  templateUrl: './salon-metier-update.component.html',
})
export class SalonMetierUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    salonMetier: [],
  });

  constructor(protected salonMetierService: SalonMetierService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salonMetier }) => {
      this.updateForm(salonMetier);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salonMetier = this.createFromForm();
    if (salonMetier.id !== undefined) {
      this.subscribeToSaveResponse(this.salonMetierService.update(salonMetier));
    } else {
      this.subscribeToSaveResponse(this.salonMetierService.create(salonMetier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalonMetier>>): void {
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

  protected updateForm(salonMetier: ISalonMetier): void {
    this.editForm.patchValue({
      id: salonMetier.id,
      salonMetier: salonMetier.salonMetier,
    });
  }

  protected createFromForm(): ISalonMetier {
    return {
      ...new SalonMetier(),
      id: this.editForm.get(['id'])!.value,
      salonMetier: this.editForm.get(['salonMetier'])!.value,
    };
  }
}
