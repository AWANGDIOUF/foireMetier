import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IInscription, Inscription } from '../inscription.model';
import { InscriptionService } from '../service/inscription.service';
import { DemandeStand } from 'app/entities/enumerations/demande-stand.model';

@Component({
  selector: 'jhi-inscription-update',
  templateUrl: './inscription-update.component.html',
})
export class InscriptionUpdateComponent implements OnInit {
  isSaving = false;
  demandeStandValues = Object.keys(DemandeStand);

  editForm = this.fb.group({
    id: [],
    partenaire: [],
    non: [],
    prenon: [],
    adresseProfessionnelle: [],
    telephoneProfessionnelle: [],
    emailProfessionnelle: [],
    demandeStand: [],
    tailleStand: [],
    autres: [],
  });

  constructor(protected inscriptionService: InscriptionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscription }) => {
      this.updateForm(inscription);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscription = this.createFromForm();
    if (inscription.id !== undefined) {
      this.subscribeToSaveResponse(this.inscriptionService.update(inscription));
    } else {
      this.subscribeToSaveResponse(this.inscriptionService.create(inscription));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscription>>): void {
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

  protected updateForm(inscription: IInscription): void {
    this.editForm.patchValue({
      id: inscription.id,
      partenaire: inscription.partenaire,
      non: inscription.non,
      prenon: inscription.prenon,
      adresseProfessionnelle: inscription.adresseProfessionnelle,
      telephoneProfessionnelle: inscription.telephoneProfessionnelle,
      emailProfessionnelle: inscription.emailProfessionnelle,
      demandeStand: inscription.demandeStand,
      tailleStand: inscription.tailleStand,
      autres: inscription.autres,
    });
  }

  protected createFromForm(): IInscription {
    return {
      ...new Inscription(),
      id: this.editForm.get(['id'])!.value,
      partenaire: this.editForm.get(['partenaire'])!.value,
      non: this.editForm.get(['non'])!.value,
      prenon: this.editForm.get(['prenon'])!.value,
      adresseProfessionnelle: this.editForm.get(['adresseProfessionnelle'])!.value,
      telephoneProfessionnelle: this.editForm.get(['telephoneProfessionnelle'])!.value,
      emailProfessionnelle: this.editForm.get(['emailProfessionnelle'])!.value,
      demandeStand: this.editForm.get(['demandeStand'])!.value,
      tailleStand: this.editForm.get(['tailleStand'])!.value,
      autres: this.editForm.get(['autres'])!.value,
    };
  }
}
