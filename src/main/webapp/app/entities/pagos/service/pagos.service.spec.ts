import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPagos } from '../pagos.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pagos.test-samples';

import { PagosService, RestPagos } from './pagos.service';

const requireRestSample: RestPagos = {
  ...sampleWithRequiredData,
  fecha: sampleWithRequiredData.fecha?.format(DATE_FORMAT),
};

describe('Pagos Service', () => {
  let service: PagosService;
  let httpMock: HttpTestingController;
  let expectedResult: IPagos | IPagos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PagosService);
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

    it('should create a Pagos', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pagos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pagos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pagos', () => {
      const pagos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pagos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pagos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pagos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pagos', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPagosToCollectionIfMissing', () => {
      it('should add a Pagos to an empty array', () => {
        const pagos: IPagos = sampleWithRequiredData;
        expectedResult = service.addPagosToCollectionIfMissing([], pagos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pagos);
      });

      it('should not add a Pagos to an array that contains it', () => {
        const pagos: IPagos = sampleWithRequiredData;
        const pagosCollection: IPagos[] = [
          {
            ...pagos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPagosToCollectionIfMissing(pagosCollection, pagos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pagos to an array that doesn't contain it", () => {
        const pagos: IPagos = sampleWithRequiredData;
        const pagosCollection: IPagos[] = [sampleWithPartialData];
        expectedResult = service.addPagosToCollectionIfMissing(pagosCollection, pagos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pagos);
      });

      it('should add only unique Pagos to an array', () => {
        const pagosArray: IPagos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pagosCollection: IPagos[] = [sampleWithRequiredData];
        expectedResult = service.addPagosToCollectionIfMissing(pagosCollection, ...pagosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pagos: IPagos = sampleWithRequiredData;
        const pagos2: IPagos = sampleWithPartialData;
        expectedResult = service.addPagosToCollectionIfMissing([], pagos, pagos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pagos);
        expect(expectedResult).toContain(pagos2);
      });

      it('should accept null and undefined values', () => {
        const pagos: IPagos = sampleWithRequiredData;
        expectedResult = service.addPagosToCollectionIfMissing([], null, pagos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pagos);
      });

      it('should return initial array if no Pagos is added', () => {
        const pagosCollection: IPagos[] = [sampleWithRequiredData];
        expectedResult = service.addPagosToCollectionIfMissing(pagosCollection, undefined, null);
        expect(expectedResult).toEqual(pagosCollection);
      });
    });

    describe('comparePagos', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePagos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePagos(entity1, entity2);
        const compareResult2 = service.comparePagos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePagos(entity1, entity2);
        const compareResult2 = service.comparePagos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePagos(entity1, entity2);
        const compareResult2 = service.comparePagos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
