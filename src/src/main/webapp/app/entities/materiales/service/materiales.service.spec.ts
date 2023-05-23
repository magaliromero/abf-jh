import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMateriales } from '../materiales.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../materiales.test-samples';

import { MaterialesService } from './materiales.service';

const requireRestSample: IMateriales = {
  ...sampleWithRequiredData,
};

describe('Materiales Service', () => {
  let service: MaterialesService;
  let httpMock: HttpTestingController;
  let expectedResult: IMateriales | IMateriales[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MaterialesService);
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

    it('should create a Materiales', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const materiales = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(materiales).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Materiales', () => {
      const materiales = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(materiales).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Materiales', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Materiales', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Materiales', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaterialesToCollectionIfMissing', () => {
      it('should add a Materiales to an empty array', () => {
        const materiales: IMateriales = sampleWithRequiredData;
        expectedResult = service.addMaterialesToCollectionIfMissing([], materiales);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materiales);
      });

      it('should not add a Materiales to an array that contains it', () => {
        const materiales: IMateriales = sampleWithRequiredData;
        const materialesCollection: IMateriales[] = [
          {
            ...materiales,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaterialesToCollectionIfMissing(materialesCollection, materiales);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Materiales to an array that doesn't contain it", () => {
        const materiales: IMateriales = sampleWithRequiredData;
        const materialesCollection: IMateriales[] = [sampleWithPartialData];
        expectedResult = service.addMaterialesToCollectionIfMissing(materialesCollection, materiales);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materiales);
      });

      it('should add only unique Materiales to an array', () => {
        const materialesArray: IMateriales[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const materialesCollection: IMateriales[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialesToCollectionIfMissing(materialesCollection, ...materialesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const materiales: IMateriales = sampleWithRequiredData;
        const materiales2: IMateriales = sampleWithPartialData;
        expectedResult = service.addMaterialesToCollectionIfMissing([], materiales, materiales2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materiales);
        expect(expectedResult).toContain(materiales2);
      });

      it('should accept null and undefined values', () => {
        const materiales: IMateriales = sampleWithRequiredData;
        expectedResult = service.addMaterialesToCollectionIfMissing([], null, materiales, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materiales);
      });

      it('should return initial array if no Materiales is added', () => {
        const materialesCollection: IMateriales[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialesToCollectionIfMissing(materialesCollection, undefined, null);
        expect(expectedResult).toEqual(materialesCollection);
      });
    });

    describe('compareMateriales', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMateriales(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMateriales(entity1, entity2);
        const compareResult2 = service.compareMateriales(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMateriales(entity1, entity2);
        const compareResult2 = service.compareMateriales(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMateriales(entity1, entity2);
        const compareResult2 = service.compareMateriales(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
