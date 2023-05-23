import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPrestamos } from '../prestamos.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../prestamos.test-samples';

import { PrestamosService, RestPrestamos } from './prestamos.service';

const requireRestSample: RestPrestamos = {
  ...sampleWithRequiredData,
  fechaPrestamo: sampleWithRequiredData.fechaPrestamo?.format(DATE_FORMAT),
  fechaDevolucion: sampleWithRequiredData.fechaDevolucion?.format(DATE_FORMAT),
};

describe('Prestamos Service', () => {
  let service: PrestamosService;
  let httpMock: HttpTestingController;
  let expectedResult: IPrestamos | IPrestamos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrestamosService);
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

    it('should create a Prestamos', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const prestamos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(prestamos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Prestamos', () => {
      const prestamos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(prestamos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Prestamos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Prestamos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Prestamos', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPrestamosToCollectionIfMissing', () => {
      it('should add a Prestamos to an empty array', () => {
        const prestamos: IPrestamos = sampleWithRequiredData;
        expectedResult = service.addPrestamosToCollectionIfMissing([], prestamos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prestamos);
      });

      it('should not add a Prestamos to an array that contains it', () => {
        const prestamos: IPrestamos = sampleWithRequiredData;
        const prestamosCollection: IPrestamos[] = [
          {
            ...prestamos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPrestamosToCollectionIfMissing(prestamosCollection, prestamos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Prestamos to an array that doesn't contain it", () => {
        const prestamos: IPrestamos = sampleWithRequiredData;
        const prestamosCollection: IPrestamos[] = [sampleWithPartialData];
        expectedResult = service.addPrestamosToCollectionIfMissing(prestamosCollection, prestamos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prestamos);
      });

      it('should add only unique Prestamos to an array', () => {
        const prestamosArray: IPrestamos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const prestamosCollection: IPrestamos[] = [sampleWithRequiredData];
        expectedResult = service.addPrestamosToCollectionIfMissing(prestamosCollection, ...prestamosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prestamos: IPrestamos = sampleWithRequiredData;
        const prestamos2: IPrestamos = sampleWithPartialData;
        expectedResult = service.addPrestamosToCollectionIfMissing([], prestamos, prestamos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prestamos);
        expect(expectedResult).toContain(prestamos2);
      });

      it('should accept null and undefined values', () => {
        const prestamos: IPrestamos = sampleWithRequiredData;
        expectedResult = service.addPrestamosToCollectionIfMissing([], null, prestamos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prestamos);
      });

      it('should return initial array if no Prestamos is added', () => {
        const prestamosCollection: IPrestamos[] = [sampleWithRequiredData];
        expectedResult = service.addPrestamosToCollectionIfMissing(prestamosCollection, undefined, null);
        expect(expectedResult).toEqual(prestamosCollection);
      });
    });

    describe('comparePrestamos', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePrestamos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePrestamos(entity1, entity2);
        const compareResult2 = service.comparePrestamos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePrestamos(entity1, entity2);
        const compareResult2 = service.comparePrestamos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePrestamos(entity1, entity2);
        const compareResult2 = service.comparePrestamos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
