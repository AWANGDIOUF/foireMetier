import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ObjectifService } from '../service/objectif.service';

import { ObjectifComponent } from './objectif.component';

describe('Objectif Management Component', () => {
  let comp: ObjectifComponent;
  let fixture: ComponentFixture<ObjectifComponent>;
  let service: ObjectifService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ObjectifComponent],
    })
      .overrideTemplate(ObjectifComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ObjectifComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ObjectifService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.objectifs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
