import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIntroduction } from '../introduction.model';

@Component({
  selector: 'jhi-introduction-detail',
  templateUrl: './introduction-detail.component.html',
})
export class IntroductionDetailComponent implements OnInit {
  introduction: IIntroduction | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ introduction }) => {
      this.introduction = introduction;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
