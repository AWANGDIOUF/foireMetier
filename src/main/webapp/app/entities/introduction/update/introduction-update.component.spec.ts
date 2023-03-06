import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IntroductionService } from '../service/introduction.service';
import { IIntroduction, Introduction } from '../introduction.model';

import { IntroductionUpdateComponent } from './introduction-update.component';

describe('Introduction Management Update Component', () => {
  let comp: IntroductionUpdateComponent;
  let fixture: ComponentFixture<IntroductionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let introductionService: IntroductionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IntroductionUpdateComponent],
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
      .overrideTemplate(IntroductionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IntroductionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    introductionService = TestBed.inject(IntroductionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const introduction: IIntroduction = { id: 456 };

      activatedRoute.data = of({ introduction });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(introduction));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Introduction>>();
      const introduction = { id: 123 };
      jest.spyOn(introductionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ introduction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: introduction }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(introductionService.update).toHaveBeenCalledWith(introduction);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Introduction>>();
      const introduction = new Introduction();
      jest.spyOn(introductionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ introduction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: introduction }));
      saveSubject.complete();

      // THEN
      expect(introductionService.create).toHaveBeenCalledWith(introduction);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Introduction>>();
      const introduction = { id: 123 };
      jest.spyOn(introductionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ introduction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(introductionService.update).toHaveBeenCalledWith(introduction);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
