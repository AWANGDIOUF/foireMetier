import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SalonMetierDetailComponent } from './salon-metier-detail.component';

describe('SalonMetier Management Detail Component', () => {
  let comp: SalonMetierDetailComponent;
  let fixture: ComponentFixture<SalonMetierDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SalonMetierDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ salonMetier: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SalonMetierDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SalonMetierDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load salonMetier on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.salonMetier).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
