import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TimbradosFormService } from './timbrados-form.service';
import { TimbradosService } from '../service/timbrados.service';
import { ITimbrados } from '../timbrados.model';
import { ISucursales } from 'app/entities/sucursales/sucursales.model';
import { SucursalesService } from 'app/entities/sucursales/service/sucursales.service';

import { TimbradosUpdateComponent } from './timbrados-update.component';

describe('Timbrados Management Update Component', () => {
  let comp: TimbradosUpdateComponent;
  let fixture: ComponentFixture<TimbradosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let timbradosFormService: TimbradosFormService;
  let timbradosService: TimbradosService;
  let sucursalesService: SucursalesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TimbradosUpdateComponent],
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
      .overrideTemplate(TimbradosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TimbradosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    timbradosFormService = TestBed.inject(TimbradosFormService);
    timbradosService = TestBed.inject(TimbradosService);
    sucursalesService = TestBed.inject(SucursalesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call sucursales query and add missing value', () => {
      const timbrados: ITimbrados = { id: 456 };
      const sucursales: ISucursales = { id: 9257 };
      timbrados.sucursales = sucursales;

      const sucursalesCollection: ISucursales[] = [{ id: 18829 }];
      jest.spyOn(sucursalesService, 'query').mockReturnValue(of(new HttpResponse({ body: sucursalesCollection })));
      const expectedCollection: ISucursales[] = [sucursales, ...sucursalesCollection];
      jest.spyOn(sucursalesService, 'addSucursalesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ timbrados });
      comp.ngOnInit();

      expect(sucursalesService.query).toHaveBeenCalled();
      expect(sucursalesService.addSucursalesToCollectionIfMissing).toHaveBeenCalledWith(sucursalesCollection, sucursales);
      expect(comp.sucursalesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const timbrados: ITimbrados = { id: 456 };
      const sucursales: ISucursales = { id: 33107 };
      timbrados.sucursales = sucursales;

      activatedRoute.data = of({ timbrados });
      comp.ngOnInit();

      expect(comp.sucursalesCollection).toContain(sucursales);
      expect(comp.timbrados).toEqual(timbrados);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimbrados>>();
      const timbrados = { id: 123 };
      jest.spyOn(timbradosFormService, 'getTimbrados').mockReturnValue(timbrados);
      jest.spyOn(timbradosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timbrados });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: timbrados }));
      saveSubject.complete();

      // THEN
      expect(timbradosFormService.getTimbrados).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(timbradosService.update).toHaveBeenCalledWith(expect.objectContaining(timbrados));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimbrados>>();
      const timbrados = { id: 123 };
      jest.spyOn(timbradosFormService, 'getTimbrados').mockReturnValue({ id: null });
      jest.spyOn(timbradosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timbrados: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: timbrados }));
      saveSubject.complete();

      // THEN
      expect(timbradosFormService.getTimbrados).toHaveBeenCalled();
      expect(timbradosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimbrados>>();
      const timbrados = { id: 123 };
      jest.spyOn(timbradosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timbrados });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(timbradosService.update).toHaveBeenCalled();
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
