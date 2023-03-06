import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SalonMetierService } from '../service/salon-metier.service';
import { ISalonMetier, SalonMetier } from '../salon-metier.model';

import { SalonMetierUpdateComponent } from './salon-metier-update.component';

describe('SalonMetier Management Update Component', () => {
  let comp: SalonMetierUpdateComponent;
  let fixture: ComponentFixture<SalonMetierUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let salonMetierService: SalonMetierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SalonMetierUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SalonMetierUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SalonMetierUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    salonMetierService = TestBed.inject(SalonMetierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const salonMetier: ISalonMetier = { id: 456 };

      activatedRoute.data = of({ salonMetier });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(salonMetier));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SalonMetier>>();
      const salonMetier = { id: 123 };
      jest.spyOn(salonMetierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salonMetier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salonMetier }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(salonMetierService.update).toHaveBeenCalledWith(salonMetier);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SalonMetier>>();
      const salonMetier = new SalonMetier();
      jest.spyOn(salonMetierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salonMetier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salonMetier }));
      saveSubject.complete();

      // THEN
      expect(salonMetierService.create).toHaveBeenCalledWith(salonMetier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SalonMetier>>();
      const salonMetier = { id: 123 };
      jest.spyOn(salonMetierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salonMetier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(salonMetierService.update).toHaveBeenCalledWith(salonMetier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
