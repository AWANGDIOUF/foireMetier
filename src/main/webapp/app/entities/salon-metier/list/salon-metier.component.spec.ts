import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SalonMetierService } from '../service/salon-metier.service';

import { SalonMetierComponent } from './salon-metier.component';

describe('SalonMetier Management Component', () => {
  let comp: SalonMetierComponent;
  let fixture: ComponentFixture<SalonMetierComponent>;
  let service: SalonMetierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SalonMetierComponent],
    })
      .overrideTemplate(SalonMetierComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SalonMetierComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SalonMetierService);

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
    expect(comp.salonMetiers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
