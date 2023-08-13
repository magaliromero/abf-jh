import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SucursalesFormService } from './sucursales-form.service';
import { SucursalesService } from '../service/sucursales.service';
import { ISucursales } from '../sucursales.model';

import { SucursalesUpdateComponent } from './sucursales-update.component';

describe('Sucursales Management Update Component', () => {
  let comp: SucursalesUpdateComponent;
  let fixture: ComponentFixture<SucursalesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sucursalesFormService: SucursalesFormService;
  let sucursalesService: SucursalesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SucursalesUpdateComponent],
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
      .overrideTemplate(SucursalesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SucursalesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sucursalesFormService = TestBed.inject(SucursalesFormService);
    sucursalesService = TestBed.inject(SucursalesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sucursales: ISucursales = { id: 456 };

      activatedRoute.data = of({ sucursales });
      comp.ngOnInit();

      expect(comp.sucursales).toEqual(sucursales);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISucursales>>();
      const sucursales = { id: 123 };
      jest.spyOn(sucursalesFormService, 'getSucursales').mockReturnValue(sucursales);
      jest.spyOn(sucursalesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sucursales });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sucursales }));
      saveSubject.complete();

      // THEN
      expect(sucursalesFormService.getSucursales).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sucursalesService.update).toHaveBeenCalledWith(expect.objectContaining(sucursales));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISucursales>>();
      const sucursales = { id: 123 };
      jest.spyOn(sucursalesFormService, 'getSucursales').mockReturnValue({ id: null });
      jest.spyOn(sucursalesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sucursales: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sucursales }));
      saveSubject.complete();

      // THEN
      expect(sucursalesFormService.getSucursales).toHaveBeenCalled();
      expect(sucursalesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISucursales>>();
      const sucursales = { id: 123 };
      jest.spyOn(sucursalesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sucursales });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sucursalesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
