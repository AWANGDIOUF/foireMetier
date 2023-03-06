import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EntrepriseService } from '../service/entreprise.service';

import { EntrepriseComponent } from './entreprise.component';

describe('Entreprise Management Component', () => {
  let comp: EntrepriseComponent;
  let fixture: ComponentFixture<EntrepriseComponent>;
  let service: EntrepriseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EntrepriseComponent],
    })
      .overrideTemplate(EntrepriseComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EntrepriseComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EntrepriseService);

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
    expect(comp.entreprises?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
