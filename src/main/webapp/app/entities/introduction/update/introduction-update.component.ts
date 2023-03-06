import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IIntroduction, Introduction } from '../introduction.model';
import { IntroductionService } from '../service/introduction.service';

@Component({
  selector: 'jhi-introduction-update',
  templateUrl: './introduction-update.component.html',
})
export class IntroductionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    introduction: [],
  });

  constructor(protected introductionService: IntroductionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ introduction }) => {
      this.updateForm(introduction);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const introduction = this.createFromForm();
    if (introduction.id !== undefined) {
      this.subscribeToSaveResponse(this.introductionService.update(introduction));
    } else {
      this.subscribeToSaveResponse(this.introductionService.create(introduction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIntroduction>>): void {
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

  protected updateForm(introduction: IIntroduction): void {
    this.editForm.patchValue({
      id: introduction.id,
      introduction: introduction.introduction,
    });
  }

  protected createFromForm(): IIntroduction {
    return {
      ...new Introduction(),
      id: this.editForm.get(['id'])!.value,
      introduction: this.editForm.get(['introduction'])!.value,
    };
  }
}
