import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICursos } from '../cursos.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cursos.test-samples';

import { CursosService, RestCursos } from './cursos.service';

const requireRestSample: RestCursos = {
  ...sampleWithRequiredData,
  fechaInicio: sampleWithRequiredData.fechaInicio?.format(DATE_FORMAT),
  fechaFin: sampleWithRequiredData.fechaFin?.format(DATE_FORMAT),
};

describe('Cursos Service', () => {
  let service: CursosService;
  let httpMock: HttpTestingController;
  let expectedResult: ICursos | ICursos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CursosService);
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

    it('should create a Cursos', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const cursos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cursos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cursos', () => {
      const cursos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cursos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cursos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cursos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cursos', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCursosToCollectionIfMissing', () => {
      it('should add a Cursos to an empty array', () => {
        const cursos: ICursos = sampleWithRequiredData;
        expectedResult = service.addCursosToCollectionIfMissing([], cursos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cursos);
      });

      it('should not add a Cursos to an array that contains it', () => {
        const cursos: ICursos = sampleWithRequiredData;
        const cursosCollection: ICursos[] = [
          {
            ...cursos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCursosToCollectionIfMissing(cursosCollection, cursos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cursos to an array that doesn't contain it", () => {
        const cursos: ICursos = sampleWithRequiredData;
        const cursosCollection: ICursos[] = [sampleWithPartialData];
        expectedResult = service.addCursosToCollectionIfMissing(cursosCollection, cursos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cursos);
      });

      it('should add only unique Cursos to an array', () => {
        const cursosArray: ICursos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cursosCollection: ICursos[] = [sampleWithRequiredData];
        expectedResult = service.addCursosToCollectionIfMissing(cursosCollection, ...cursosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cursos: ICursos = sampleWithRequiredData;
        const cursos2: ICursos = sampleWithPartialData;
        expectedResult = service.addCursosToCollectionIfMissing([], cursos, cursos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cursos);
        expect(expectedResult).toContain(cursos2);
      });

      it('should accept null and undefined values', () => {
        const cursos: ICursos = sampleWithRequiredData;
        expectedResult = service.addCursosToCollectionIfMissing([], null, cursos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cursos);
      });

      it('should return initial array if no Cursos is added', () => {
        const cursosCollection: ICursos[] = [sampleWithRequiredData];
        expectedResult = service.addCursosToCollectionIfMissing(cursosCollection, undefined, null);
        expect(expectedResult).toEqual(cursosCollection);
      });
    });

    describe('compareCursos', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCursos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCursos(entity1, entity2);
        const compareResult2 = service.compareCursos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCursos(entity1, entity2);
        const compareResult2 = service.compareCursos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCursos(entity1, entity2);
        const compareResult2 = service.compareCursos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
