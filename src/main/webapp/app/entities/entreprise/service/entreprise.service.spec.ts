import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEntreprise, Entreprise } from '../entreprise.model';

import { EntrepriseService } from './entreprise.service';

describe('Entreprise Service', () => {
  let service: EntrepriseService;
  let httpMock: HttpTestingController;
  let elemDefault: IEntreprise;
  let expectedResult: IEntreprise | IEntreprise[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EntrepriseService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      entreprise: 'AAAAAAA',
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

    it('should create a Entreprise', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Entreprise()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Entreprise', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          entreprise: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Entreprise', () => {
      const patchObject = Object.assign(
        {
          entreprise: 'BBBBBB',
        },
        new Entreprise()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Entreprise', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          entreprise: 'BBBBBB',
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

    it('should delete a Entreprise', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEntrepriseToCollectionIfMissing', () => {
      it('should add a Entreprise to an empty array', () => {
        const entreprise: IEntreprise = { id: 123 };
        expectedResult = service.addEntrepriseToCollectionIfMissing([], entreprise);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(entreprise);
      });

      it('should not add a Entreprise to an array that contains it', () => {
        const entreprise: IEntreprise = { id: 123 };
        const entrepriseCollection: IEntreprise[] = [
          {
            ...entreprise,
          },
          { id: 456 },
        ];
        expectedResult = service.addEntrepriseToCollectionIfMissing(entrepriseCollection, entreprise);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Entreprise to an array that doesn't contain it", () => {
        const entreprise: IEntreprise = { id: 123 };
        const entrepriseCollection: IEntreprise[] = [{ id: 456 }];
        expectedResult = service.addEntrepriseToCollectionIfMissing(entrepriseCollection, entreprise);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entreprise);
      });

      it('should add only unique Entreprise to an array', () => {
        const entrepriseArray: IEntreprise[] = [{ id: 123 }, { id: 456 }, { id: 79913 }];
        const entrepriseCollection: IEntreprise[] = [{ id: 123 }];
        expectedResult = service.addEntrepriseToCollectionIfMissing(entrepriseCollection, ...entrepriseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const entreprise: IEntreprise = { id: 123 };
        const entreprise2: IEntreprise = { id: 456 };
        expectedResult = service.addEntrepriseToCollectionIfMissing([], entreprise, entreprise2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entreprise);
        expect(expectedResult).toContain(entreprise2);
      });

      it('should accept null and undefined values', () => {
        const entreprise: IEntreprise = { id: 123 };
        expectedResult = service.addEntrepriseToCollectionIfMissing([], null, entreprise, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(entreprise);
      });

      it('should return initial array if no Entreprise is added', () => {
        const entrepriseCollection: IEntreprise[] = [{ id: 123 }];
        expectedResult = service.addEntrepriseToCollectionIfMissing(entrepriseCollection, undefined, null);
        expect(expectedResult).toEqual(entrepriseCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
