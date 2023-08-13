import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPuntoDeExpedicion } from '../punto-de-expedicion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../punto-de-expedicion.test-samples';

import { PuntoDeExpedicionService } from './punto-de-expedicion.service';

const requireRestSample: IPuntoDeExpedicion = {
  ...sampleWithRequiredData,
};

describe('PuntoDeExpedicion Service', () => {
  let service: PuntoDeExpedicionService;
  let httpMock: HttpTestingController;
  let expectedResult: IPuntoDeExpedicion | IPuntoDeExpedicion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PuntoDeExpedicionService);
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

    it('should create a PuntoDeExpedicion', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const puntoDeExpedicion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(puntoDeExpedicion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PuntoDeExpedicion', () => {
      const puntoDeExpedicion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(puntoDeExpedicion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PuntoDeExpedicion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PuntoDeExpedicion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PuntoDeExpedicion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPuntoDeExpedicionToCollectionIfMissing', () => {
      it('should add a PuntoDeExpedicion to an empty array', () => {
        const puntoDeExpedicion: IPuntoDeExpedicion = sampleWithRequiredData;
        expectedResult = service.addPuntoDeExpedicionToCollectionIfMissing([], puntoDeExpedicion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(puntoDeExpedicion);
      });

      it('should not add a PuntoDeExpedicion to an array that contains it', () => {
        const puntoDeExpedicion: IPuntoDeExpedicion = sampleWithRequiredData;
        const puntoDeExpedicionCollection: IPuntoDeExpedicion[] = [
          {
            ...puntoDeExpedicion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPuntoDeExpedicionToCollectionIfMissing(puntoDeExpedicionCollection, puntoDeExpedicion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PuntoDeExpedicion to an array that doesn't contain it", () => {
        const puntoDeExpedicion: IPuntoDeExpedicion = sampleWithRequiredData;
        const puntoDeExpedicionCollection: IPuntoDeExpedicion[] = [sampleWithPartialData];
        expectedResult = service.addPuntoDeExpedicionToCollectionIfMissing(puntoDeExpedicionCollection, puntoDeExpedicion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(puntoDeExpedicion);
      });

      it('should add only unique PuntoDeExpedicion to an array', () => {
        const puntoDeExpedicionArray: IPuntoDeExpedicion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const puntoDeExpedicionCollection: IPuntoDeExpedicion[] = [sampleWithRequiredData];
        expectedResult = service.addPuntoDeExpedicionToCollectionIfMissing(puntoDeExpedicionCollection, ...puntoDeExpedicionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const puntoDeExpedicion: IPuntoDeExpedicion = sampleWithRequiredData;
        const puntoDeExpedicion2: IPuntoDeExpedicion = sampleWithPartialData;
        expectedResult = service.addPuntoDeExpedicionToCollectionIfMissing([], puntoDeExpedicion, puntoDeExpedicion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(puntoDeExpedicion);
        expect(expectedResult).toContain(puntoDeExpedicion2);
      });

      it('should accept null and undefined values', () => {
        const puntoDeExpedicion: IPuntoDeExpedicion = sampleWithRequiredData;
        expectedResult = service.addPuntoDeExpedicionToCollectionIfMissing([], null, puntoDeExpedicion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(puntoDeExpedicion);
      });

      it('should return initial array if no PuntoDeExpedicion is added', () => {
        const puntoDeExpedicionCollection: IPuntoDeExpedicion[] = [sampleWithRequiredData];
        expectedResult = service.addPuntoDeExpedicionToCollectionIfMissing(puntoDeExpedicionCollection, undefined, null);
        expect(expectedResult).toEqual(puntoDeExpedicionCollection);
      });
    });

    describe('comparePuntoDeExpedicion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePuntoDeExpedicion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePuntoDeExpedicion(entity1, entity2);
        const compareResult2 = service.comparePuntoDeExpedicion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePuntoDeExpedicion(entity1, entity2);
        const compareResult2 = service.comparePuntoDeExpedicion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePuntoDeExpedicion(entity1, entity2);
        const compareResult2 = service.comparePuntoDeExpedicion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
