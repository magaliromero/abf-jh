import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PagosFormService } from './pagos-form.service';
import { PagosService } from '../service/pagos.service';
import { IPagos } from '../pagos.model';
import { IFuncionarios } from 'app/entities/funcionarios/funcionarios.model';
import { FuncionariosService } from 'app/entities/funcionarios/service/funcionarios.service';

import { PagosUpdateComponent } from './pagos-update.component';

describe('Pagos Management Update Component', () => {
  let comp: PagosUpdateComponent;
  let fixture: ComponentFixture<PagosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pagosFormService: PagosFormService;
  let pagosService: PagosService;
  let funcionariosService: FuncionariosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PagosUpdateComponent],
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
      .overrideTemplate(PagosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PagosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pagosFormService = TestBed.inject(PagosFormService);
    pagosService = TestBed.inject(PagosService);
    funcionariosService = TestBed.inject(FuncionariosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Funcionarios query and add missing value', () => {
      const pagos: IPagos = { id: 456 };
      const funcionarios: IFuncionarios = { id: 62792 };
      pagos.funcionarios = funcionarios;

      const funcionariosCollection: IFuncionarios[] = [{ id: 51622 }];
      jest.spyOn(funcionariosService, 'query').mockReturnValue(of(new HttpResponse({ body: funcionariosCollection })));
      const additionalFuncionarios = [funcionarios];
      const expectedCollection: IFuncionarios[] = [...additionalFuncionarios, ...funcionariosCollection];
      jest.spyOn(funcionariosService, 'addFuncionariosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pagos });
      comp.ngOnInit();

      expect(funcionariosService.query).toHaveBeenCalled();
      expect(funcionariosService.addFuncionariosToCollectionIfMissing).toHaveBeenCalledWith(
        funcionariosCollection,
        ...additionalFuncionarios.map(expect.objectContaining)
      );
      expect(comp.funcionariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pagos: IPagos = { id: 456 };
      const funcionarios: IFuncionarios = { id: 34733 };
      pagos.funcionarios = funcionarios;

      activatedRoute.data = of({ pagos });
      comp.ngOnInit();

      expect(comp.funcionariosSharedCollection).toContain(funcionarios);
      expect(comp.pagos).toEqual(pagos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPagos>>();
      const pagos = { id: 123 };
      jest.spyOn(pagosFormService, 'getPagos').mockReturnValue(pagos);
      jest.spyOn(pagosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pagos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pagos }));
      saveSubject.complete();

      // THEN
      expect(pagosFormService.getPagos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pagosService.update).toHaveBeenCalledWith(expect.objectContaining(pagos));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPagos>>();
      const pagos = { id: 123 };
      jest.spyOn(pagosFormService, 'getPagos').mockReturnValue({ id: null });
      jest.spyOn(pagosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pagos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pagos }));
      saveSubject.complete();

      // THEN
      expect(pagosFormService.getPagos).toHaveBeenCalled();
      expect(pagosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPagos>>();
      const pagos = { id: 123 };
      jest.spyOn(pagosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pagos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pagosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFuncionarios', () => {
      it('Should forward to funcionariosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(funcionariosService, 'compareFuncionarios');
        comp.compareFuncionarios(entity, entity2);
        expect(funcionariosService.compareFuncionarios).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
