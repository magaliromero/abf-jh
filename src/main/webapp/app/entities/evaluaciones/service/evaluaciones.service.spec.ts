import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEvaluaciones } from '../evaluaciones.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../evaluaciones.test-samples';

import { EvaluacionesService, RestEvaluaciones } from './evaluaciones.service';

const requireRestSample: RestEvaluaciones = {
  ...sampleWithRequiredData,
  fecha: sampleWithRequiredData.fecha?.format(DATE_FORMAT),
};

describe('Evaluaciones Service', () => {
  let service: EvaluacionesService;
  let httpMock: HttpTestingController;
  let expectedResult: IEvaluaciones | IEvaluaciones[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EvaluacionesService);
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

    it('should create a Evaluaciones', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const evaluaciones = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(evaluaciones).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Evaluaciones', () => {
      const evaluaciones = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(evaluaciones).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Evaluaciones', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Evaluaciones', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Evaluaciones', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEvaluacionesToCollectionIfMissing', () => {
      it('should add a Evaluaciones to an empty array', () => {
        const evaluaciones: IEvaluaciones = sampleWithRequiredData;
        expectedResult = service.addEvaluacionesToCollectionIfMissing([], evaluaciones);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evaluaciones);
      });

      it('should not add a Evaluaciones to an array that contains it', () => {
        const evaluaciones: IEvaluaciones = sampleWithRequiredData;
        const evaluacionesCollection: IEvaluaciones[] = [
          {
            ...evaluaciones,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEvaluacionesToCollectionIfMissing(evaluacionesCollection, evaluaciones);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Evaluaciones to an array that doesn't contain it", () => {
        const evaluaciones: IEvaluaciones = sampleWithRequiredData;
        const evaluacionesCollection: IEvaluaciones[] = [sampleWithPartialData];
        expectedResult = service.addEvaluacionesToCollectionIfMissing(evaluacionesCollection, evaluaciones);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evaluaciones);
      });

      it('should add only unique Evaluaciones to an array', () => {
        const evaluacionesArray: IEvaluaciones[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const evaluacionesCollection: IEvaluaciones[] = [sampleWithRequiredData];
        expectedResult = service.addEvaluacionesToCollectionIfMissing(evaluacionesCollection, ...evaluacionesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const evaluaciones: IEvaluaciones = sampleWithRequiredData;
        const evaluaciones2: IEvaluaciones = sampleWithPartialData;
        expectedResult = service.addEvaluacionesToCollectionIfMissing([], evaluaciones, evaluaciones2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evaluaciones);
        expect(expectedResult).toContain(evaluaciones2);
      });

      it('should accept null and undefined values', () => {
        const evaluaciones: IEvaluaciones = sampleWithRequiredData;
        expectedResult = service.addEvaluacionesToCollectionIfMissing([], null, evaluaciones, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evaluaciones);
      });

      it('should return initial array if no Evaluaciones is added', () => {
        const evaluacionesCollection: IEvaluaciones[] = [sampleWithRequiredData];
        expectedResult = service.addEvaluacionesToCollectionIfMissing(evaluacionesCollection, undefined, null);
        expect(expectedResult).toEqual(evaluacionesCollection);
      });
    });

    describe('compareEvaluaciones', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEvaluaciones(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEvaluaciones(entity1, entity2);
        const compareResult2 = service.compareEvaluaciones(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEvaluaciones(entity1, entity2);
        const compareResult2 = service.compareEvaluaciones(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEvaluaciones(entity1, entity2);
        const compareResult2 = service.compareEvaluaciones(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
