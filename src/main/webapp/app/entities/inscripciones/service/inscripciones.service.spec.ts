import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInscripciones } from '../inscripciones.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../inscripciones.test-samples';

import { InscripcionesService, RestInscripciones } from './inscripciones.service';

const requireRestSample: RestInscripciones = {
  ...sampleWithRequiredData,
  fechaInscripcion: sampleWithRequiredData.fechaInscripcion?.format(DATE_FORMAT),
};

describe('Inscripciones Service', () => {
  let service: InscripcionesService;
  let httpMock: HttpTestingController;
  let expectedResult: IInscripciones | IInscripciones[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InscripcionesService);
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

    it('should create a Inscripciones', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const inscripciones = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inscripciones).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Inscripciones', () => {
      const inscripciones = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inscripciones).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Inscripciones', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Inscripciones', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Inscripciones', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInscripcionesToCollectionIfMissing', () => {
      it('should add a Inscripciones to an empty array', () => {
        const inscripciones: IInscripciones = sampleWithRequiredData;
        expectedResult = service.addInscripcionesToCollectionIfMissing([], inscripciones);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inscripciones);
      });

      it('should not add a Inscripciones to an array that contains it', () => {
        const inscripciones: IInscripciones = sampleWithRequiredData;
        const inscripcionesCollection: IInscripciones[] = [
          {
            ...inscripciones,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInscripcionesToCollectionIfMissing(inscripcionesCollection, inscripciones);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Inscripciones to an array that doesn't contain it", () => {
        const inscripciones: IInscripciones = sampleWithRequiredData;
        const inscripcionesCollection: IInscripciones[] = [sampleWithPartialData];
        expectedResult = service.addInscripcionesToCollectionIfMissing(inscripcionesCollection, inscripciones);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inscripciones);
      });

      it('should add only unique Inscripciones to an array', () => {
        const inscripcionesArray: IInscripciones[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inscripcionesCollection: IInscripciones[] = [sampleWithRequiredData];
        expectedResult = service.addInscripcionesToCollectionIfMissing(inscripcionesCollection, ...inscripcionesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inscripciones: IInscripciones = sampleWithRequiredData;
        const inscripciones2: IInscripciones = sampleWithPartialData;
        expectedResult = service.addInscripcionesToCollectionIfMissing([], inscripciones, inscripciones2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inscripciones);
        expect(expectedResult).toContain(inscripciones2);
      });

      it('should accept null and undefined values', () => {
        const inscripciones: IInscripciones = sampleWithRequiredData;
        expectedResult = service.addInscripcionesToCollectionIfMissing([], null, inscripciones, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inscripciones);
      });

      it('should return initial array if no Inscripciones is added', () => {
        const inscripcionesCollection: IInscripciones[] = [sampleWithRequiredData];
        expectedResult = service.addInscripcionesToCollectionIfMissing(inscripcionesCollection, undefined, null);
        expect(expectedResult).toEqual(inscripcionesCollection);
      });
    });

    describe('compareInscripciones', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInscripciones(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInscripciones(entity1, entity2);
        const compareResult2 = service.compareInscripciones(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInscripciones(entity1, entity2);
        const compareResult2 = service.compareInscripciones(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInscripciones(entity1, entity2);
        const compareResult2 = service.compareInscripciones(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
