import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PuntoDeExpedicionFormService } from './punto-de-expedicion-form.service';
import { PuntoDeExpedicionService } from '../service/punto-de-expedicion.service';
import { IPuntoDeExpedicion } from '../punto-de-expedicion.model';
import { ISucursales } from 'app/entities/sucursales/sucursales.model';
import { SucursalesService } from 'app/entities/sucursales/service/sucursales.service';

import { PuntoDeExpedicionUpdateComponent } from './punto-de-expedicion-update.component';

describe('PuntoDeExpedicion Management Update Component', () => {
  let comp: PuntoDeExpedicionUpdateComponent;
  let fixture: ComponentFixture<PuntoDeExpedicionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let puntoDeExpedicionFormService: PuntoDeExpedicionFormService;
  let puntoDeExpedicionService: PuntoDeExpedicionService;
  let sucursalesService: SucursalesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PuntoDeExpedicionUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PuntoDeExpedicionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PuntoDeExpedicionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    puntoDeExpedicionFormService = TestBed.inject(PuntoDeExpedicionFormService);
    puntoDeExpedicionService = TestBed.inject(PuntoDeExpedicionService);
    sucursalesService = TestBed.inject(SucursalesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sucursales query and add missing value', () => {
      const puntoDeExpedicion: IPuntoDeExpedicion = { id: 456 };
      const sucursales: ISucursales = { id: 64385 };
      puntoDeExpedicion.sucursales = sucursales;

      const sucursalesCollection: ISucursales[] = [{ id: 97694 }];
      jest.spyOn(sucursalesService, 'query').mockReturnValue(of(new HttpResponse({ body: sucursalesCollection })));
      const additionalSucursales = [sucursales];
      const expectedCollection: ISucursales[] = [...additionalSucursales, ...sucursalesCollection];
      jest.spyOn(sucursalesService, 'addSucursalesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ puntoDeExpedicion });
      comp.ngOnInit();

      expect(sucursalesService.query).toHaveBeenCalled();
      expect(sucursalesService.addSucursalesToCollectionIfMissing).toHaveBeenCalledWith(
        sucursalesCollection,
        ...additionalSucursales.map(expect.objectContaining)
      );
      expect(comp.sucursalesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const puntoDeExpedicion: IPuntoDeExpedicion = { id: 456 };
      const sucursales: ISucursales = { id: 48449 };
      puntoDeExpedicion.sucursales = sucursales;

      activatedRoute.data = of({ puntoDeExpedicion });
      comp.ngOnInit();

      expect(comp.sucursalesSharedCollection).toContain(sucursales);
      expect(comp.puntoDeExpedicion).toEqual(puntoDeExpedicion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPuntoDeExpedicion>>();
      const puntoDeExpedicion = { id: 123 };
      jest.spyOn(puntoDeExpedicionFormService, 'getPuntoDeExpedicion').mockReturnValue(puntoDeExpedicion);
      jest.spyOn(puntoDeExpedicionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ puntoDeExpedicion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: puntoDeExpedicion }));
      saveSubject.complete();

      // THEN
      expect(puntoDeExpedicionFormService.getPuntoDeExpedicion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(puntoDeExpedicionService.update).toHaveBeenCalledWith(expect.objectContaining(puntoDeExpedicion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPuntoDeExpedicion>>();
      const puntoDeExpedicion = { id: 123 };
      jest.spyOn(puntoDeExpedicionFormService, 'getPuntoDeExpedicion').mockReturnValue({ id: null });
      jest.spyOn(puntoDeExpedicionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ puntoDeExpedicion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: puntoDeExpedicion }));
      saveSubject.complete();

      // THEN
      expect(puntoDeExpedicionFormService.getPuntoDeExpedicion).toHaveBeenCalled();
      expect(puntoDeExpedicionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPuntoDeExpedicion>>();
      const puntoDeExpedicion = { id: 123 };
      jest.spyOn(puntoDeExpedicionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ puntoDeExpedicion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(puntoDeExpedicionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSucursales', () => {
      it('Should forward to sucursalesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sucursalesService, 'compareSucursales');
        comp.compareSucursales(entity, entity2);
        expect(sucursalesService.compareSucursales).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
