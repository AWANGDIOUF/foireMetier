import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIntroduction, Introduction } from '../introduction.model';

import { IntroductionService } from './introduction.service';

describe('Introduction Service', () => {
  let service: IntroductionService;
  let httpMock: HttpTestingController;
  let elemDefault: IIntroduction;
  let expectedResult: IIntroduction | IIntroduction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IntroductionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      introduction: 'AAAAAAA',
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

    it('should create a Introduction', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Introduction()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Introduction', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          introduction: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Introduction', () => {
      const patchObject = Object.assign({}, new Introduction());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Introduction', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          introduction: 'BBBBBB',
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

    it('should delete a Introduction', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIntroductionToCollectionIfMissing', () => {
      it('should add a Introduction to an empty array', () => {
        const introduction: IIntroduction = { id: 123 };
        expectedResult = service.addIntroductionToCollectionIfMissing([], introduction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(introduction);
      });

      it('should not add a Introduction to an array that contains it', () => {
        const introduction: IIntroduction = { id: 123 };
        const introductionCollection: IIntroduction[] = [
          {
            ...introduction,
          },
          { id: 456 },
        ];
        expectedResult = service.addIntroductionToCollectionIfMissing(introductionCollection, introduction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Introduction to an array that doesn't contain it", () => {
        const introduction: IIntroduction = { id: 123 };
        const introductionCollection: IIntroduction[] = [{ id: 456 }];
        expectedResult = service.addIntroductionToCollectionIfMissing(introductionCollection, introduction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(introduction);
      });

      it('should add only unique Introduction to an array', () => {
        const introductionArray: IIntroduction[] = [{ id: 123 }, { id: 456 }, { id: 38944 }];
        const introductionCollection: IIntroduction[] = [{ id: 123 }];
        expectedResult = service.addIntroductionToCollectionIfMissing(introductionCollection, ...introductionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const introduction: IIntroduction = { id: 123 };
        const introduction2: IIntroduction = { id: 456 };
        expectedResult = service.addIntroductionToCollectionIfMissing([], introduction, introduction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(introduction);
        expect(expectedResult).toContain(introduction2);
      });

      it('should accept null and undefined values', () => {
        const introduction: IIntroduction = { id: 123 };
        expectedResult = service.addIntroductionToCollectionIfMissing([], null, introduction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(introduction);
      });

      it('should return initial array if no Introduction is added', () => {
        const introductionCollection: IIntroduction[] = [{ id: 123 }];
        expectedResult = service.addIntroductionToCollectionIfMissing(introductionCollection, undefined, null);
        expect(expectedResult).toEqual(introductionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
