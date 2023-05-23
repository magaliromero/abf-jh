import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITiposDocumentos } from '../tipos-documentos.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tipos-documentos.test-samples';

import { TiposDocumentosService } from './tipos-documentos.service';

const requireRestSample: ITiposDocumentos = {
  ...sampleWithRequiredData,
};

describe('TiposDocumentos Service', () => {
  let service: TiposDocumentosService;
  let httpMock: HttpTestingController;
  let expectedResult: ITiposDocumentos | ITiposDocumentos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TiposDocumentosService);
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

    it('should create a TiposDocumentos', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tiposDocumentos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tiposDocumentos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TiposDocumentos', () => {
      const tiposDocumentos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tiposDocumentos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TiposDocumentos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TiposDocumentos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TiposDocumentos', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTiposDocumentosToCollectionIfMissing', () => {
      it('should add a TiposDocumentos to an empty array', () => {
        const tiposDocumentos: ITiposDocumentos = sampleWithRequiredData;
        expectedResult = service.addTiposDocumentosToCollectionIfMissing([], tiposDocumentos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tiposDocumentos);
      });

      it('should not add a TiposDocumentos to an array that contains it', () => {
        const tiposDocumentos: ITiposDocumentos = sampleWithRequiredData;
        const tiposDocumentosCollection: ITiposDocumentos[] = [
          {
            ...tiposDocumentos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTiposDocumentosToCollectionIfMissing(tiposDocumentosCollection, tiposDocumentos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TiposDocumentos to an array that doesn't contain it", () => {
        const tiposDocumentos: ITiposDocumentos = sampleWithRequiredData;
        const tiposDocumentosCollection: ITiposDocumentos[] = [sampleWithPartialData];
        expectedResult = service.addTiposDocumentosToCollectionIfMissing(tiposDocumentosCollection, tiposDocumentos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tiposDocumentos);
      });

      it('should add only unique TiposDocumentos to an array', () => {
        const tiposDocumentosArray: ITiposDocumentos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tiposDocumentosCollection: ITiposDocumentos[] = [sampleWithRequiredData];
        expectedResult = service.addTiposDocumentosToCollectionIfMissing(tiposDocumentosCollection, ...tiposDocumentosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tiposDocumentos: ITiposDocumentos = sampleWithRequiredData;
        const tiposDocumentos2: ITiposDocumentos = sampleWithPartialData;
        expectedResult = service.addTiposDocumentosToCollectionIfMissing([], tiposDocumentos, tiposDocumentos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tiposDocumentos);
        expect(expectedResult).toContain(tiposDocumentos2);
      });

      it('should accept null and undefined values', () => {
        const tiposDocumentos: ITiposDocumentos = sampleWithRequiredData;
        expectedResult = service.addTiposDocumentosToCollectionIfMissing([], null, tiposDocumentos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tiposDocumentos);
      });

      it('should return initial array if no TiposDocumentos is added', () => {
        const tiposDocumentosCollection: ITiposDocumentos[] = [sampleWithRequiredData];
        expectedResult = service.addTiposDocumentosToCollectionIfMissing(tiposDocumentosCollection, undefined, null);
        expect(expectedResult).toEqual(tiposDocumentosCollection);
      });
    });

    describe('compareTiposDocumentos', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTiposDocumentos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTiposDocumentos(entity1, entity2);
        const compareResult2 = service.compareTiposDocumentos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTiposDocumentos(entity1, entity2);
        const compareResult2 = service.compareTiposDocumentos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTiposDocumentos(entity1, entity2);
        const compareResult2 = service.compareTiposDocumentos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
