import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICobros } from '../cobros.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cobros.test-samples';

import { CobrosService, RestCobros } from './cobros.service';

const requireRestSample: RestCobros = {
  ...sampleWithRequiredData,
  fechaRegistro: sampleWithRequiredData.fechaRegistro?.format(DATE_FORMAT),
  fechaPago: sampleWithRequiredData.fechaPago?.format(DATE_FORMAT),
};

describe('Cobros Service', () => {
  let service: CobrosService;
  let httpMock: HttpTestingController;
  let expectedResult: ICobros | ICobros[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CobrosService);
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

    it('should create a Cobros', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const cobros = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cobros).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cobros', () => {
      const cobros = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cobros).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cobros', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cobros', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cobros', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCobrosToCollectionIfMissing', () => {
      it('should add a Cobros to an empty array', () => {
        const cobros: ICobros = sampleWithRequiredData;
        expectedResult = service.addCobrosToCollectionIfMissing([], cobros);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cobros);
      });

      it('should not add a Cobros to an array that contains it', () => {
        const cobros: ICobros = sampleWithRequiredData;
        const cobrosCollection: ICobros[] = [
          {
            ...cobros,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCobrosToCollectionIfMissing(cobrosCollection, cobros);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cobros to an array that doesn't contain it", () => {
        const cobros: ICobros = sampleWithRequiredData;
        const cobrosCollection: ICobros[] = [sampleWithPartialData];
        expectedResult = service.addCobrosToCollectionIfMissing(cobrosCollection, cobros);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cobros);
      });

      it('should add only unique Cobros to an array', () => {
        const cobrosArray: ICobros[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cobrosCollection: ICobros[] = [sampleWithRequiredData];
        expectedResult = service.addCobrosToCollectionIfMissing(cobrosCollection, ...cobrosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cobros: ICobros = sampleWithRequiredData;
        const cobros2: ICobros = sampleWithPartialData;
        expectedResult = service.addCobrosToCollectionIfMissing([], cobros, cobros2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cobros);
        expect(expectedResult).toContain(cobros2);
      });

      it('should accept null and undefined values', () => {
        const cobros: ICobros = sampleWithRequiredData;
        expectedResult = service.addCobrosToCollectionIfMissing([], null, cobros, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cobros);
      });

      it('should return initial array if no Cobros is added', () => {
        const cobrosCollection: ICobros[] = [sampleWithRequiredData];
        expectedResult = service.addCobrosToCollectionIfMissing(cobrosCollection, undefined, null);
        expect(expectedResult).toEqual(cobrosCollection);
      });
    });

    describe('compareCobros', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCobros(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCobros(entity1, entity2);
        const compareResult2 = service.compareCobros(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCobros(entity1, entity2);
        const compareResult2 = service.compareCobros(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCobros(entity1, entity2);
        const compareResult2 = service.compareCobros(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
