import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FacturasFormService } from './facturas-form.service';
import { FacturasService } from '../service/facturas.service';
import { IFacturas } from '../facturas.model';

import { FacturasUpdateComponent } from './facturas-update.component';

describe('Facturas Management Update Component', () => {
  let comp: FacturasUpdateComponent;
  let fixture: ComponentFixture<FacturasUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let facturasFormService: FacturasFormService;
  let facturasService: FacturasService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FacturasUpdateComponent],
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
      .overrideTemplate(FacturasUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FacturasUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facturasFormService = TestBed.inject(FacturasFormService);
    facturasService = TestBed.inject(FacturasService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const facturas: IFacturas = { id: 456 };

      activatedRoute.data = of({ facturas });
      comp.ngOnInit();

      expect(comp.facturas).toEqual(facturas);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFacturas>>();
      const facturas = { id: 123 };
      jest.spyOn(facturasFormService, 'getFacturas').mockReturnValue(facturas);
      jest.spyOn(facturasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facturas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: facturas }));
      saveSubject.complete();

      // THEN
      expect(facturasFormService.getFacturas).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facturasService.update).toHaveBeenCalledWith(expect.objectContaining(facturas));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFacturas>>();
      const facturas = { id: 123 };
      jest.spyOn(facturasFormService, 'getFacturas').mockReturnValue({ id: null });
      jest.spyOn(facturasService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facturas: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: facturas }));
      saveSubject.complete();

      // THEN
      expect(facturasFormService.getFacturas).toHaveBeenCalled();
      expect(facturasService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFacturas>>();
      const facturas = { id: 123 };
      jest.spyOn(facturasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facturas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facturasService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
