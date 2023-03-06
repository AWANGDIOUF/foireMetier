import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IObjectif, Objectif } from '../objectif.model';
import { ObjectifService } from '../service/objectif.service';

@Component({
  selector: 'jhi-objectif-update',
  templateUrl: './objectif-update.component.html',
})
export class ObjectifUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    objectif: [],
  });

  constructor(protected objectifService: ObjectifService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ objectif }) => {
      this.updateForm(objectif);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const objectif = this.createFromForm();
    if (objectif.id !== undefined) {
      this.subscribeToSaveResponse(this.objectifService.update(objectif));
    } else {
      this.subscribeToSaveResponse(this.objectifService.create(objectif));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IObjectif>>): void {
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

  protected updateForm(objectif: IObjectif): void {
    this.editForm.patchValue({
      id: objectif.id,
      objectif: objectif.objectif,
    });
  }

  protected createFromForm(): IObjectif {
    return {
      ...new Objectif(),
      id: this.editForm.get(['id'])!.value,
      objectif: this.editForm.get(['objectif'])!.value,
    };
  }
}
