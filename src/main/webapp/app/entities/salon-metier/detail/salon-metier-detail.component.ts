import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalonMetier } from '../salon-metier.model';

@Component({
  selector: 'jhi-salon-metier-detail',
  templateUrl: './salon-metier-detail.component.html',
})
export class SalonMetierDetailComponent implements OnInit {
  salonMetier: ISalonMetier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salonMetier }) => {
      this.salonMetier = salonMetier;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
