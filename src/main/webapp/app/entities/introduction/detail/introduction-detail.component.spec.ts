import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IntroductionDetailComponent } from './introduction-detail.component';

describe('Introduction Management Detail Component', () => {
  let comp: IntroductionDetailComponent;
  let fixture: ComponentFixture<IntroductionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IntroductionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ introduction: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IntroductionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IntroductionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load introduction on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.introduction).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
