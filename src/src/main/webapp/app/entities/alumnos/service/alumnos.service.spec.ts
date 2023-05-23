import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAlumnos } from '../alumnos.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../alumnos.test-samples';

import { AlumnosService, RestAlumnos } from './alumnos.service';

const requireRestSample: RestAlumnos = {
  ...sampleWithRequiredData,
  fechaNacimiento: sampleWithRequiredData.fechaNacimiento?.format(DATE_FORMAT),
};

describe('Alumnos Service', () => {
  let service: AlumnosService;
  let httpMock: HttpTestingController;
  let expectedResult: IAlumnos | IAlumnos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AlumnosService);
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

    it('should create a Alumnos', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const alumnos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(alumnos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Alumnos', () => {
      const alumnos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(alumnos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Alumnos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Alumnos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Alumnos', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAlumnosToCollectionIfMissing', () => {
      it('should add a Alumnos to an empty array', () => {
        const alumnos: IAlumnos = sampleWithRequiredData;
        expectedResult = service.addAlumnosToCollectionIfMissing([], alumnos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alumnos);
      });

      it('should not add a Alumnos to an array that contains it', () => {
        const alumnos: IAlumnos = sampleWithRequiredData;
        const alumnosCollection: IAlumnos[] = [
          {
            ...alumnos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAlumnosToCollectionIfMissing(alumnosCollection, alumnos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Alumnos to an array that doesn't contain it", () => {
        const alumnos: IAlumnos = sampleWithRequiredData;
        const alumnosCollection: IAlumnos[] = [sampleWithPartialData];
        expectedResult = service.addAlumnosToCollectionIfMissing(alumnosCollection, alumnos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alumnos);
      });

      it('should add only unique Alumnos to an array', () => {
        const alumnosArray: IAlumnos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const alumnosCollection: IAlumnos[] = [sampleWithRequiredData];
        expectedResult = service.addAlumnosToCollectionIfMissing(alumnosCollection, ...alumnosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const alumnos: IAlumnos = sampleWithRequiredData;
        const alumnos2: IAlumnos = sampleWithPartialData;
        expectedResult = service.addAlumnosToCollectionIfMissing([], alumnos, alumnos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alumnos);
        expect(expectedResult).toContain(alumnos2);
      });

      it('should accept null and undefined values', () => {
        const alumnos: IAlumnos = sampleWithRequiredData;
        expectedResult = service.addAlumnosToCollectionIfMissing([], null, alumnos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alumnos);
      });

      it('should return initial array if no Alumnos is added', () => {
        const alumnosCollection: IAlumnos[] = [sampleWithRequiredData];
        expectedResult = service.addAlumnosToCollectionIfMissing(alumnosCollection, undefined, null);
        expect(expectedResult).toEqual(alumnosCollection);
      });
    });

    describe('compareAlumnos', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAlumnos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAlumnos(entity1, entity2);
        const compareResult2 = service.compareAlumnos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAlumnos(entity1, entity2);
        const compareResult2 = service.compareAlumnos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAlumnos(entity1, entity2);
        const compareResult2 = service.compareAlumnos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
