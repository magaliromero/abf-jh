import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISucursales } from '../sucursales.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sucursales.test-samples';

import { SucursalesService } from './sucursales.service';

const requireRestSample: ISucursales = {
  ...sampleWithRequiredData,
};

describe('Sucursales Service', () => {
  let service: SucursalesService;
  let httpMock: HttpTestingController;
  let expectedResult: ISucursales | ISucursales[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SucursalesService);
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

    it('should create a Sucursales', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const sucursales = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sucursales).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sucursales', () => {
      const sucursales = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sucursales).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sucursales', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sucursales', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Sucursales', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSucursalesToCollectionIfMissing', () => {
      it('should add a Sucursales to an empty array', () => {
        const sucursales: ISucursales = sampleWithRequiredData;
        expectedResult = service.addSucursalesToCollectionIfMissing([], sucursales);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sucursales);
      });

      it('should not add a Sucursales to an array that contains it', () => {
        const sucursales: ISucursales = sampleWithRequiredData;
        const sucursalesCollection: ISucursales[] = [
          {
            ...sucursales,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSucursalesToCollectionIfMissing(sucursalesCollection, sucursales);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sucursales to an array that doesn't contain it", () => {
        const sucursales: ISucursales = sampleWithRequiredData;
        const sucursalesCollection: ISucursales[] = [sampleWithPartialData];
        expectedResult = service.addSucursalesToCollectionIfMissing(sucursalesCollection, sucursales);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sucursales);
      });

      it('should add only unique Sucursales to an array', () => {
        const sucursalesArray: ISucursales[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sucursalesCollection: ISucursales[] = [sampleWithRequiredData];
        expectedResult = service.addSucursalesToCollectionIfMissing(sucursalesCollection, ...sucursalesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sucursales: ISucursales = sampleWithRequiredData;
        const sucursales2: ISucursales = sampleWithPartialData;
        expectedResult = service.addSucursalesToCollectionIfMissing([], sucursales, sucursales2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sucursales);
        expect(expectedResult).toContain(sucursales2);
      });

      it('should accept null and undefined values', () => {
        const sucursales: ISucursales = sampleWithRequiredData;
        expectedResult = service.addSucursalesToCollectionIfMissing([], null, sucursales, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sucursales);
      });

      it('should return initial array if no Sucursales is added', () => {
        const sucursalesCollection: ISucursales[] = [sampleWithRequiredData];
        expectedResult = service.addSucursalesToCollectionIfMissing(sucursalesCollection, undefined, null);
        expect(expectedResult).toEqual(sucursalesCollection);
      });
    });

    describe('compareSucursales', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSucursales(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSucursales(entity1, entity2);
        const compareResult2 = service.compareSucursales(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSucursales(entity1, entity2);
        const compareResult2 = service.compareSucursales(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSucursales(entity1, entity2);
        const compareResult2 = service.compareSucursales(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
