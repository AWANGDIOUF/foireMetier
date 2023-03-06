import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ObjectifService } from '../service/objectif.service';
import { IObjectif, Objectif } from '../objectif.model';

import { ObjectifUpdateComponent } from './objectif-update.component';

describe('Objectif Management Update Component', () => {
  let comp: ObjectifUpdateComponent;
  let fixture: ComponentFixture<ObjectifUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let objectifService: ObjectifService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ObjectifUpdateComponent],
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
      .overrideTemplate(ObjectifUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ObjectifUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    objectifService = TestBed.inject(ObjectifService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const objectif: IObjectif = { id: 456 };

      activatedRoute.data = of({ objectif });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(objectif));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Objectif>>();
      const objectif = { id: 123 };
      jest.spyOn(objectifService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objectif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: objectif }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(objectifService.update).toHaveBeenCalledWith(objectif);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Objectif>>();
      const objectif = new Objectif();
      jest.spyOn(objectifService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objectif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: objectif }));
      saveSubject.complete();

      // THEN
      expect(objectifService.create).toHaveBeenCalledWith(objectif);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Objectif>>();
      const objectif = { id: 123 };
      jest.spyOn(objectifService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objectif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(objectifService.update).toHaveBeenCalledWith(objectif);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
