import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFacturaDetalle } from '../factura-detalle.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../factura-detalle.test-samples';

import { FacturaDetalleService } from './factura-detalle.service';

const requireRestSample: IFacturaDetalle = {
  ...sampleWithRequiredData,
};

describe('FacturaDetalle Service', () => {
  let service: FacturaDetalleService;
  let httpMock: HttpTestingController;
  let expectedResult: IFacturaDetalle | IFacturaDetalle[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FacturaDetalleService);
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

    it('should create a FacturaDetalle', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const facturaDetalle = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(facturaDetalle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FacturaDetalle', () => {
      const facturaDetalle = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(facturaDetalle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FacturaDetalle', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FacturaDetalle', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FacturaDetalle', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFacturaDetalleToCollectionIfMissing', () => {
      it('should add a FacturaDetalle to an empty array', () => {
        const facturaDetalle: IFacturaDetalle = sampleWithRequiredData;
        expectedResult = service.addFacturaDetalleToCollectionIfMissing([], facturaDetalle);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(facturaDetalle);
      });

      it('should not add a FacturaDetalle to an array that contains it', () => {
        const facturaDetalle: IFacturaDetalle = sampleWithRequiredData;
        const facturaDetalleCollection: IFacturaDetalle[] = [
          {
            ...facturaDetalle,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFacturaDetalleToCollectionIfMissing(facturaDetalleCollection, facturaDetalle);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FacturaDetalle to an array that doesn't contain it", () => {
        const facturaDetalle: IFacturaDetalle = sampleWithRequiredData;
        const facturaDetalleCollection: IFacturaDetalle[] = [sampleWithPartialData];
        expectedResult = service.addFacturaDetalleToCollectionIfMissing(facturaDetalleCollection, facturaDetalle);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facturaDetalle);
      });

      it('should add only unique FacturaDetalle to an array', () => {
        const facturaDetalleArray: IFacturaDetalle[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const facturaDetalleCollection: IFacturaDetalle[] = [sampleWithRequiredData];
        expectedResult = service.addFacturaDetalleToCollectionIfMissing(facturaDetalleCollection, ...facturaDetalleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const facturaDetalle: IFacturaDetalle = sampleWithRequiredData;
        const facturaDetalle2: IFacturaDetalle = sampleWithPartialData;
        expectedResult = service.addFacturaDetalleToCollectionIfMissing([], facturaDetalle, facturaDetalle2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facturaDetalle);
        expect(expectedResult).toContain(facturaDetalle2);
      });

      it('should accept null and undefined values', () => {
        const facturaDetalle: IFacturaDetalle = sampleWithRequiredData;
        expectedResult = service.addFacturaDetalleToCollectionIfMissing([], null, facturaDetalle, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(facturaDetalle);
      });

      it('should return initial array if no FacturaDetalle is added', () => {
        const facturaDetalleCollection: IFacturaDetalle[] = [sampleWithRequiredData];
        expectedResult = service.addFacturaDetalleToCollectionIfMissing(facturaDetalleCollection, undefined, null);
        expect(expectedResult).toEqual(facturaDetalleCollection);
      });
    });

    describe('compareFacturaDetalle', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFacturaDetalle(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFacturaDetalle(entity1, entity2);
        const compareResult2 = service.compareFacturaDetalle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFacturaDetalle(entity1, entity2);
        const compareResult2 = service.compareFacturaDetalle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFacturaDetalle(entity1, entity2);
        const compareResult2 = service.compareFacturaDetalle(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
