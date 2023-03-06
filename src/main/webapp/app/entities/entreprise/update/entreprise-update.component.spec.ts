import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EntrepriseService } from '../service/entreprise.service';
import { IEntreprise, Entreprise } from '../entreprise.model';

import { EntrepriseUpdateComponent } from './entreprise-update.component';

describe('Entreprise Management Update Component', () => {
  let comp: EntrepriseUpdateComponent;
  let fixture: ComponentFixture<EntrepriseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let entrepriseService: EntrepriseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EntrepriseUpdateComponent],
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
      .overrideTemplate(EntrepriseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EntrepriseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    entrepriseService = TestBed.inject(EntrepriseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const entreprise: IEntreprise = { id: 456 };

      activatedRoute.data = of({ entreprise });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(entreprise));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Entreprise>>();
      const entreprise = { id: 123 };
      jest.spyOn(entrepriseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entreprise });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: entreprise }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(entrepriseService.update).toHaveBeenCalledWith(entreprise);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Entreprise>>();
      const entreprise = new Entreprise();
      jest.spyOn(entrepriseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entreprise });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: entreprise }));
      saveSubject.complete();

      // THEN
      expect(entrepriseService.create).toHaveBeenCalledWith(entreprise);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Entreprise>>();
      const entreprise = { id: 123 };
      jest.spyOn(entrepriseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entreprise });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(entrepriseService.update).toHaveBeenCalledWith(entreprise);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
