import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FuncionariosFormService } from './funcionarios-form.service';
import { FuncionariosService } from '../service/funcionarios.service';
import { IFuncionarios } from '../funcionarios.model';
import { ITiposDocumentos } from 'app/entities/tipos-documentos/tipos-documentos.model';
import { TiposDocumentosService } from 'app/entities/tipos-documentos/service/tipos-documentos.service';

import { FuncionariosUpdateComponent } from './funcionarios-update.component';

describe('Funcionarios Management Update Component', () => {
  let comp: FuncionariosUpdateComponent;
  let fixture: ComponentFixture<FuncionariosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let funcionariosFormService: FuncionariosFormService;
  let funcionariosService: FuncionariosService;
  let tiposDocumentosService: TiposDocumentosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FuncionariosUpdateComponent],
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
      .overrideTemplate(FuncionariosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FuncionariosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    funcionariosFormService = TestBed.inject(FuncionariosFormService);
    funcionariosService = TestBed.inject(FuncionariosService);
    tiposDocumentosService = TestBed.inject(TiposDocumentosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TiposDocumentos query and add missing value', () => {
      const funcionarios: IFuncionarios = { id: 456 };
      const tipoDocumentos: ITiposDocumentos = { id: 55663 };
      funcionarios.tipoDocumentos = tipoDocumentos;

      const tiposDocumentosCollection: ITiposDocumentos[] = [{ id: 69543 }];
      jest.spyOn(tiposDocumentosService, 'query').mockReturnValue(of(new HttpResponse({ body: tiposDocumentosCollection })));
      const additionalTiposDocumentos = [tipoDocumentos];
      const expectedCollection: ITiposDocumentos[] = [...additionalTiposDocumentos, ...tiposDocumentosCollection];
      jest.spyOn(tiposDocumentosService, 'addTiposDocumentosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ funcionarios });
      comp.ngOnInit();

      expect(tiposDocumentosService.query).toHaveBeenCalled();
      expect(tiposDocumentosService.addTiposDocumentosToCollectionIfMissing).toHaveBeenCalledWith(
        tiposDocumentosCollection,
        ...additionalTiposDocumentos.map(expect.objectContaining)
      );
      expect(comp.tiposDocumentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const funcionarios: IFuncionarios = { id: 456 };
      const tipoDocumentos: ITiposDocumentos = { id: 71300 };
      funcionarios.tipoDocumentos = tipoDocumentos;

      activatedRoute.data = of({ funcionarios });
      comp.ngOnInit();

      expect(comp.tiposDocumentosSharedCollection).toContain(tipoDocumentos);
      expect(comp.funcionarios).toEqual(funcionarios);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuncionarios>>();
      const funcionarios = { id: 123 };
      jest.spyOn(funcionariosFormService, 'getFuncionarios').mockReturnValue(funcionarios);
      jest.spyOn(funcionariosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ funcionarios });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: funcionarios }));
      saveSubject.complete();

      // THEN
      expect(funcionariosFormService.getFuncionarios).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(funcionariosService.update).toHaveBeenCalledWith(expect.objectContaining(funcionarios));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuncionarios>>();
      const funcionarios = { id: 123 };
      jest.spyOn(funcionariosFormService, 'getFuncionarios').mockReturnValue({ id: null });
      jest.spyOn(funcionariosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ funcionarios: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: funcionarios }));
      saveSubject.complete();

      // THEN
      expect(funcionariosFormService.getFuncionarios).toHaveBeenCalled();
      expect(funcionariosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuncionarios>>();
      const funcionarios = { id: 123 };
      jest.spyOn(funcionariosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ funcionarios });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(funcionariosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTiposDocumentos', () => {
      it('Should forward to tiposDocumentosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tiposDocumentosService, 'compareTiposDocumentos');
        comp.compareTiposDocumentos(entity, entity2);
        expect(tiposDocumentosService.compareTiposDocumentos).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
