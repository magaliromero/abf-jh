import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITemas } from '../temas.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../temas.test-samples';

import { TemasService } from './temas.service';

const requireRestSample: ITemas = {
  ...sampleWithRequiredData,
};

describe('Temas Service', () => {
  let service: TemasService;
  let httpMock: HttpTestingController;
  let expectedResult: ITemas | ITemas[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TemasService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Temas', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const temas = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(temas).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Temas', () => {
      const temas = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(temas).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Temas', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Temas', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Temas', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTemasToCollectionIfMissing', () => {
      it('should add a Temas to an empty array', () => {
        const temas: ITemas = sampleWithRequiredData;
        expectedResult = service.addTemasToCollectionIfMissing([], temas);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(temas);
      });

      it('should not add a Temas to an array that contains it', () => {
        const temas: ITemas = sampleWithRequiredData;
        const temasCollection: ITemas[] = [
          {
            ...temas,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTemasToCollectionIfMissing(temasCollection, temas);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Temas to an array that doesn't contain it", () => {
        const temas: ITemas = sampleWithRequiredData;
        const temasCollection: ITemas[] = [sampleWithPartialData];
        expectedResult = service.addTemasToCollectionIfMissing(temasCollection, temas);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(temas);
      });

      it('should add only unique Temas to an array', () => {
        const temasArray: ITemas[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const temasCollection: ITemas[] = [sampleWithRequiredData];
        expectedResult = service.addTemasToCollectionIfMissing(temasCollection, ...temasArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const temas: ITemas = sampleWithRequiredData;
        const temas2: ITemas = sampleWithPartialData;
        expectedResult = service.addTemasToCollectionIfMissing([], temas, temas2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(temas);
        expect(expectedResult).toContain(temas2);
      });

      it('should accept null and undefined values', () => {
        const temas: ITemas = sampleWithRequiredData;
        expectedResult = service.addTemasToCollectionIfMissing([], null, temas, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(temas);
      });

      it('should return initial array if no Temas is added', () => {
        const temasCollection: ITemas[] = [sampleWithRequiredData];
        expectedResult = service.addTemasToCollectionIfMissing(temasCollection, undefined, null);
        expect(expectedResult).toEqual(temasCollection);
      });
    });

    describe('compareTemas', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTemas(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTemas(entity1, entity2);
        const compareResult2 = service.compareTemas(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTemas(entity1, entity2);
        const compareResult2 = service.compareTemas(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTemas(entity1, entity2);
        const compareResult2 = service.compareTemas(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
