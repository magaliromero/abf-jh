import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FacturaDetalleFormService } from './factura-detalle-form.service';
import { FacturaDetalleService } from '../service/factura-detalle.service';
import { IFacturaDetalle } from '../factura-detalle.model';
import { IProductos } from 'app/entities/productos/productos.model';
import { ProductosService } from 'app/entities/productos/service/productos.service';
import { IFacturas } from 'app/entities/facturas/facturas.model';
import { FacturasService } from 'app/entities/facturas/service/facturas.service';

import { FacturaDetalleUpdateComponent } from './factura-detalle-update.component';

describe('FacturaDetalle Management Update Component', () => {
  let comp: FacturaDetalleUpdateComponent;
  let fixture: ComponentFixture<FacturaDetalleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let facturaDetalleFormService: FacturaDetalleFormService;
  let facturaDetalleService: FacturaDetalleService;
  let productosService: ProductosService;
  let facturasService: FacturasService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FacturaDetalleUpdateComponent],
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
      .overrideTemplate(FacturaDetalleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FacturaDetalleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facturaDetalleFormService = TestBed.inject(FacturaDetalleFormService);
    facturaDetalleService = TestBed.inject(FacturaDetalleService);
    productosService = TestBed.inject(ProductosService);
    facturasService = TestBed.inject(FacturasService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Productos query and add missing value', () => {
      const facturaDetalle: IFacturaDetalle = { id: 456 };
      const producto: IProductos = { id: 10275 };
      facturaDetalle.producto = producto;

      const productosCollection: IProductos[] = [{ id: 25359 }];
      jest.spyOn(productosService, 'query').mockReturnValue(of(new HttpResponse({ body: productosCollection })));
      const additionalProductos = [producto];
      const expectedCollection: IProductos[] = [...additionalProductos, ...productosCollection];
      jest.spyOn(productosService, 'addProductosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facturaDetalle });
      comp.ngOnInit();

      expect(productosService.query).toHaveBeenCalled();
      expect(productosService.addProductosToCollectionIfMissing).toHaveBeenCalledWith(
        productosCollection,
        ...additionalProductos.map(expect.objectContaining)
      );
      expect(comp.productosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Facturas query and add missing value', () => {
      const facturaDetalle: IFacturaDetalle = { id: 456 };
      const factura: IFacturas = { id: 95427 };
      facturaDetalle.factura = factura;

      const facturasCollection: IFacturas[] = [{ id: 31200 }];
      jest.spyOn(facturasService, 'query').mockReturnValue(of(new HttpResponse({ body: facturasCollection })));
      const additionalFacturas = [factura];
      const expectedCollection: IFacturas[] = [...additionalFacturas, ...facturasCollection];
      jest.spyOn(facturasService, 'addFacturasToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facturaDetalle });
      comp.ngOnInit();

      expect(facturasService.query).toHaveBeenCalled();
      expect(facturasService.addFacturasToCollectionIfMissing).toHaveBeenCalledWith(
        facturasCollection,
        ...additionalFacturas.map(expect.objectContaining)
      );
      expect(comp.facturasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const facturaDetalle: IFacturaDetalle = { id: 456 };
      const producto: IProductos = { id: 40032 };
      facturaDetalle.producto = producto;
      const factura: IFacturas = { id: 64992 };
      facturaDetalle.factura = factura;

      activatedRoute.data = of({ facturaDetalle });
      comp.ngOnInit();

      expect(comp.productosSharedCollection).toContain(producto);
      expect(comp.facturasSharedCollection).toContain(factura);
      expect(comp.facturaDetalle).toEqual(facturaDetalle);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFacturaDetalle>>();
      const facturaDetalle = { id: 123 };
      jest.spyOn(facturaDetalleFormService, 'getFacturaDetalle').mockReturnValue(facturaDetalle);
      jest.spyOn(facturaDetalleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facturaDetalle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: facturaDetalle }));
      saveSubject.complete();

      // THEN
      expect(facturaDetalleFormService.getFacturaDetalle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facturaDetalleService.update).toHaveBeenCalledWith(expect.objectContaining(facturaDetalle));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFacturaDetalle>>();
      const facturaDetalle = { id: 123 };
      jest.spyOn(facturaDetalleFormService, 'getFacturaDetalle').mockReturnValue({ id: null });
      jest.spyOn(facturaDetalleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facturaDetalle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: facturaDetalle }));
      saveSubject.complete();

      // THEN
      expect(facturaDetalleFormService.getFacturaDetalle).toHaveBeenCalled();
      expect(facturaDetalleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFacturaDetalle>>();
      const facturaDetalle = { id: 123 };
      jest.spyOn(facturaDetalleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facturaDetalle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facturaDetalleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProductos', () => {
      it('Should forward to productosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productosService, 'compareProductos');
        comp.compareProductos(entity, entity2);
        expect(productosService.compareProductos).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFacturas', () => {
      it('Should forward to facturasService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(facturasService, 'compareFacturas');
        comp.compareFacturas(entity, entity2);
        expect(facturasService.compareFacturas).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
