import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRegistroClases } from '../registro-clases.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../registro-clases.test-samples';

import { RegistroClasesService, RestRegistroClases } from './registro-clases.service';

const requireRestSample: RestRegistroClases = {
  ...sampleWithRequiredData,
  fecha: sampleWithRequiredData.fecha?.format(DATE_FORMAT),
};

describe('RegistroClases Service', () => {
  let service: RegistroClasesService;
  let httpMock: HttpTestingController;
  let expectedResult: IRegistroClases | IRegistroClases[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RegistroClasesService);
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

    it('should create a RegistroClases', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const registroClases = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(registroClases).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RegistroClases', () => {
      const registroClases = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(registroClases).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RegistroClases', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RegistroClases', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RegistroClases', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRegistroClasesToCollectionIfMissing', () => {
      it('should add a RegistroClases to an empty array', () => {
        const registroClases: IRegistroClases = sampleWithRequiredData;
        expectedResult = service.addRegistroClasesToCollectionIfMissing([], registroClases);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(registroClases);
      });

      it('should not add a RegistroClases to an array that contains it', () => {
        const registroClases: IRegistroClases = sampleWithRequiredData;
        const registroClasesCollection: IRegistroClases[] = [
          {
            ...registroClases,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRegistroClasesToCollectionIfMissing(registroClasesCollection, registroClases);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RegistroClases to an array that doesn't contain it", () => {
        const registroClases: IRegistroClases = sampleWithRequiredData;
        const registroClasesCollection: IRegistroClases[] = [sampleWithPartialData];
        expectedResult = service.addRegistroClasesToCollectionIfMissing(registroClasesCollection, registroClases);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(registroClases);
      });

      it('should add only unique RegistroClases to an array', () => {
        const registroClasesArray: IRegistroClases[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const registroClasesCollection: IRegistroClases[] = [sampleWithRequiredData];
        expectedResult = service.addRegistroClasesToCollectionIfMissing(registroClasesCollection, ...registroClasesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const registroClases: IRegistroClases = sampleWithRequiredData;
        const registroClases2: IRegistroClases = sampleWithPartialData;
        expectedResult = service.addRegistroClasesToCollectionIfMissing([], registroClases, registroClases2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(registroClases);
        expect(expectedResult).toContain(registroClases2);
      });

      it('should accept null and undefined values', () => {
        const registroClases: IRegistroClases = sampleWithRequiredData;
        expectedResult = service.addRegistroClasesToCollectionIfMissing([], null, registroClases, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(registroClases);
      });

      it('should return initial array if no RegistroClases is added', () => {
        const registroClasesCollection: IRegistroClases[] = [sampleWithRequiredData];
        expectedResult = service.addRegistroClasesToCollectionIfMissing(registroClasesCollection, undefined, null);
        expect(expectedResult).toEqual(registroClasesCollection);
      });
    });

    describe('compareRegistroClases', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRegistroClases(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRegistroClases(entity1, entity2);
        const compareResult2 = service.compareRegistroClases(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRegistroClases(entity1, entity2);
        const compareResult2 = service.compareRegistroClases(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRegistroClases(entity1, entity2);
        const compareResult2 = service.compareRegistroClases(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
