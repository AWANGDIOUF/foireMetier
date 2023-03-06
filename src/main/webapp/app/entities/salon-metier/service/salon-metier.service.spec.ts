import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISalonMetier, SalonMetier } from '../salon-metier.model';

import { SalonMetierService } from './salon-metier.service';

describe('SalonMetier Service', () => {
  let service: SalonMetierService;
  let httpMock: HttpTestingController;
  let elemDefault: ISalonMetier;
  let expectedResult: ISalonMetier | ISalonMetier[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SalonMetierService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      salonMetier: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a SalonMetier', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SalonMetier()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SalonMetier', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          salonMetier: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SalonMetier', () => {
      const patchObject = Object.assign(
        {
          salonMetier: 'BBBBBB',
        },
        new SalonMetier()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SalonMetier', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          salonMetier: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a SalonMetier', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSalonMetierToCollectionIfMissing', () => {
      it('should add a SalonMetier to an empty array', () => {
        const salonMetier: ISalonMetier = { id: 123 };
        expectedResult = service.addSalonMetierToCollectionIfMissing([], salonMetier);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salonMetier);
      });

      it('should not add a SalonMetier to an array that contains it', () => {
        const salonMetier: ISalonMetier = { id: 123 };
        const salonMetierCollection: ISalonMetier[] = [
          {
            ...salonMetier,
          },
          { id: 456 },
        ];
        expectedResult = service.addSalonMetierToCollectionIfMissing(salonMetierCollection, salonMetier);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SalonMetier to an array that doesn't contain it", () => {
        const salonMetier: ISalonMetier = { id: 123 };
        const salonMetierCollection: ISalonMetier[] = [{ id: 456 }];
        expectedResult = service.addSalonMetierToCollectionIfMissing(salonMetierCollection, salonMetier);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salonMetier);
      });

      it('should add only unique SalonMetier to an array', () => {
        const salonMetierArray: ISalonMetier[] = [{ id: 123 }, { id: 456 }, { id: 16934 }];
        const salonMetierCollection: ISalonMetier[] = [{ id: 123 }];
        expectedResult = service.addSalonMetierToCollectionIfMissing(salonMetierCollection, ...salonMetierArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const salonMetier: ISalonMetier = { id: 123 };
        const salonMetier2: ISalonMetier = { id: 456 };
        expectedResult = service.addSalonMetierToCollectionIfMissing([], salonMetier, salonMetier2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salonMetier);
        expect(expectedResult).toContain(salonMetier2);
      });

      it('should accept null and undefined values', () => {
        const salonMetier: ISalonMetier = { id: 123 };
        expectedResult = service.addSalonMetierToCollectionIfMissing([], null, salonMetier, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salonMetier);
      });

      it('should return initial array if no SalonMetier is added', () => {
        const salonMetierCollection: ISalonMetier[] = [{ id: 123 }];
        expectedResult = service.addSalonMetierToCollectionIfMissing(salonMetierCollection, undefined, null);
        expect(expectedResult).toEqual(salonMetierCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
