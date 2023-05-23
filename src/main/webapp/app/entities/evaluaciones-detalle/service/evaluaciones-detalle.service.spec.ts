import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEvaluacionesDetalle } from '../evaluaciones-detalle.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../evaluaciones-detalle.test-samples';

import { EvaluacionesDetalleService } from './evaluaciones-detalle.service';

const requireRestSample: IEvaluacionesDetalle = {
  ...sampleWithRequiredData,
};

describe('EvaluacionesDetalle Service', () => {
  let service: EvaluacionesDetalleService;
  let httpMock: HttpTestingController;
  let expectedResult: IEvaluacionesDetalle | IEvaluacionesDetalle[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EvaluacionesDetalleService);
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

    it('should create a EvaluacionesDetalle', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const evaluacionesDetalle = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(evaluacionesDetalle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EvaluacionesDetalle', () => {
      const evaluacionesDetalle = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(evaluacionesDetalle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EvaluacionesDetalle', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EvaluacionesDetalle', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EvaluacionesDetalle', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEvaluacionesDetalleToCollectionIfMissing', () => {
      it('should add a EvaluacionesDetalle to an empty array', () => {
        const evaluacionesDetalle: IEvaluacionesDetalle = sampleWithRequiredData;
        expectedResult = service.addEvaluacionesDetalleToCollectionIfMissing([], evaluacionesDetalle);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evaluacionesDetalle);
      });

      it('should not add a EvaluacionesDetalle to an array that contains it', () => {
        const evaluacionesDetalle: IEvaluacionesDetalle = sampleWithRequiredData;
        const evaluacionesDetalleCollection: IEvaluacionesDetalle[] = [
          {
            ...evaluacionesDetalle,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEvaluacionesDetalleToCollectionIfMissing(evaluacionesDetalleCollection, evaluacionesDetalle);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EvaluacionesDetalle to an array that doesn't contain it", () => {
        const evaluacionesDetalle: IEvaluacionesDetalle = sampleWithRequiredData;
        const evaluacionesDetalleCollection: IEvaluacionesDetalle[] = [sampleWithPartialData];
        expectedResult = service.addEvaluacionesDetalleToCollectionIfMissing(evaluacionesDetalleCollection, evaluacionesDetalle);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evaluacionesDetalle);
      });

      it('should add only unique EvaluacionesDetalle to an array', () => {
        const evaluacionesDetalleArray: IEvaluacionesDetalle[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const evaluacionesDetalleCollection: IEvaluacionesDetalle[] = [sampleWithRequiredData];
        expectedResult = service.addEvaluacionesDetalleToCollectionIfMissing(evaluacionesDetalleCollection, ...evaluacionesDetalleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const evaluacionesDetalle: IEvaluacionesDetalle = sampleWithRequiredData;
        const evaluacionesDetalle2: IEvaluacionesDetalle = sampleWithPartialData;
        expectedResult = service.addEvaluacionesDetalleToCollectionIfMissing([], evaluacionesDetalle, evaluacionesDetalle2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evaluacionesDetalle);
        expect(expectedResult).toContain(evaluacionesDetalle2);
      });

      it('should accept null and undefined values', () => {
        const evaluacionesDetalle: IEvaluacionesDetalle = sampleWithRequiredData;
        expectedResult = service.addEvaluacionesDetalleToCollectionIfMissing([], null, evaluacionesDetalle, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evaluacionesDetalle);
      });

      it('should return initial array if no EvaluacionesDetalle is added', () => {
        const evaluacionesDetalleCollection: IEvaluacionesDetalle[] = [sampleWithRequiredData];
        expectedResult = service.addEvaluacionesDetalleToCollectionIfMissing(evaluacionesDetalleCollection, undefined, null);
        expect(expectedResult).toEqual(evaluacionesDetalleCollection);
      });
    });

    describe('compareEvaluacionesDetalle', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEvaluacionesDetalle(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEvaluacionesDetalle(entity1, entity2);
        const compareResult2 = service.compareEvaluacionesDetalle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEvaluacionesDetalle(entity1, entity2);
        const compareResult2 = service.compareEvaluacionesDetalle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEvaluacionesDetalle(entity1, entity2);
        const compareResult2 = service.compareEvaluacionesDetalle(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
