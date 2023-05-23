import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFuncionarios } from '../funcionarios.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../funcionarios.test-samples';

import { FuncionariosService, RestFuncionarios } from './funcionarios.service';

const requireRestSample: RestFuncionarios = {
  ...sampleWithRequiredData,
  fechaNacimiento: sampleWithRequiredData.fechaNacimiento?.format(DATE_FORMAT),
};

describe('Funcionarios Service', () => {
  let service: FuncionariosService;
  let httpMock: HttpTestingController;
  let expectedResult: IFuncionarios | IFuncionarios[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FuncionariosService);
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

    it('should create a Funcionarios', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const funcionarios = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(funcionarios).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Funcionarios', () => {
      const funcionarios = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(funcionarios).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Funcionarios', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Funcionarios', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Funcionarios', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFuncionariosToCollectionIfMissing', () => {
      it('should add a Funcionarios to an empty array', () => {
        const funcionarios: IFuncionarios = sampleWithRequiredData;
        expectedResult = service.addFuncionariosToCollectionIfMissing([], funcionarios);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(funcionarios);
      });

      it('should not add a Funcionarios to an array that contains it', () => {
        const funcionarios: IFuncionarios = sampleWithRequiredData;
        const funcionariosCollection: IFuncionarios[] = [
          {
            ...funcionarios,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFuncionariosToCollectionIfMissing(funcionariosCollection, funcionarios);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Funcionarios to an array that doesn't contain it", () => {
        const funcionarios: IFuncionarios = sampleWithRequiredData;
        const funcionariosCollection: IFuncionarios[] = [sampleWithPartialData];
        expectedResult = service.addFuncionariosToCollectionIfMissing(funcionariosCollection, funcionarios);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(funcionarios);
      });

      it('should add only unique Funcionarios to an array', () => {
        const funcionariosArray: IFuncionarios[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const funcionariosCollection: IFuncionarios[] = [sampleWithRequiredData];
        expectedResult = service.addFuncionariosToCollectionIfMissing(funcionariosCollection, ...funcionariosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const funcionarios: IFuncionarios = sampleWithRequiredData;
        const funcionarios2: IFuncionarios = sampleWithPartialData;
        expectedResult = service.addFuncionariosToCollectionIfMissing([], funcionarios, funcionarios2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(funcionarios);
        expect(expectedResult).toContain(funcionarios2);
      });

      it('should accept null and undefined values', () => {
        const funcionarios: IFuncionarios = sampleWithRequiredData;
        expectedResult = service.addFuncionariosToCollectionIfMissing([], null, funcionarios, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(funcionarios);
      });

      it('should return initial array if no Funcionarios is added', () => {
        const funcionariosCollection: IFuncionarios[] = [sampleWithRequiredData];
        expectedResult = service.addFuncionariosToCollectionIfMissing(funcionariosCollection, undefined, null);
        expect(expectedResult).toEqual(funcionariosCollection);
      });
    });

    describe('compareFuncionarios', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFuncionarios(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFuncionarios(entity1, entity2);
        const compareResult2 = service.compareFuncionarios(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFuncionarios(entity1, entity2);
        const compareResult2 = service.compareFuncionarios(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFuncionarios(entity1, entity2);
        const compareResult2 = service.compareFuncionarios(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
