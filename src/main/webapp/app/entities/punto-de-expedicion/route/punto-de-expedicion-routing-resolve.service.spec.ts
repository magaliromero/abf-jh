import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPuntoDeExpedicion } from '../punto-de-expedicion.model';
import { PuntoDeExpedicionService } from '../service/punto-de-expedicion.service';

import { PuntoDeExpedicionRoutingResolveService } from './punto-de-expedicion-routing-resolve.service';

describe('PuntoDeExpedicion routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PuntoDeExpedicionRoutingResolveService;
  let service: PuntoDeExpedicionService;
  let resultPuntoDeExpedicion: IPuntoDeExpedicion | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(PuntoDeExpedicionRoutingResolveService);
    service = TestBed.inject(PuntoDeExpedicionService);
    resultPuntoDeExpedicion = undefined;
  });

  describe('resolve', () => {
    it('should return IPuntoDeExpedicion returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPuntoDeExpedicion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPuntoDeExpedicion).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPuntoDeExpedicion = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPuntoDeExpedicion).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPuntoDeExpedicion>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPuntoDeExpedicion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPuntoDeExpedicion).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
