import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RegistroClasesFormService } from './registro-clases-form.service';
import { RegistroClasesService } from '../service/registro-clases.service';
import { IRegistroClases } from '../registro-clases.model';
import { ITemas } from 'app/entities/temas/temas.model';
import { TemasService } from 'app/entities/temas/service/temas.service';
import { IFuncionarios } from 'app/entities/funcionarios/funcionarios.model';
import { FuncionariosService } from 'app/entities/funcionarios/service/funcionarios.service';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';

import { RegistroClasesUpdateComponent } from './registro-clases-update.component';

describe('RegistroClases Management Update Component', () => {
  let comp: RegistroClasesUpdateComponent;
  let fixture: ComponentFixture<RegistroClasesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let registroClasesFormService: RegistroClasesFormService;
  let registroClasesService: RegistroClasesService;
  let temasService: TemasService;
  let funcionariosService: FuncionariosService;
  let alumnosService: AlumnosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RegistroClasesUpdateComponent],
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
      .overrideTemplate(RegistroClasesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RegistroClasesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    registroClasesFormService = TestBed.inject(RegistroClasesFormService);
    registroClasesService = TestBed.inject(RegistroClasesService);
    temasService = TestBed.inject(TemasService);
    funcionariosService = TestBed.inject(FuncionariosService);
    alumnosService = TestBed.inject(AlumnosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Temas query and add missing value', () => {
      const registroClases: IRegistroClases = { id: 456 };
      const temas: ITemas = { id: 74210 };
      registroClases.temas = temas;

      const temasCollection: ITemas[] = [{ id: 80835 }];
      jest.spyOn(temasService, 'query').mockReturnValue(of(new HttpResponse({ body: temasCollection })));
      const additionalTemas = [temas];
      const expectedCollection: ITemas[] = [...additionalTemas, ...temasCollection];
      jest.spyOn(temasService, 'addTemasToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ registroClases });
      comp.ngOnInit();

      expect(temasService.query).toHaveBeenCalled();
      expect(temasService.addTemasToCollectionIfMissing).toHaveBeenCalledWith(
        temasCollection,
        ...additionalTemas.map(expect.objectContaining)
      );
      expect(comp.temasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Funcionarios query and add missing value', () => {
      const registroClases: IRegistroClases = { id: 456 };
      const funcionario: IFuncionarios = { id: 42041 };
      registroClases.funcionario = funcionario;

      const funcionariosCollection: IFuncionarios[] = [{ id: 74664 }];
      jest.spyOn(funcionariosService, 'query').mockReturnValue(of(new HttpResponse({ body: funcionariosCollection })));
      const additionalFuncionarios = [funcionario];
      const expectedCollection: IFuncionarios[] = [...additionalFuncionarios, ...funcionariosCollection];
      jest.spyOn(funcionariosService, 'addFuncionariosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ registroClases });
      comp.ngOnInit();

      expect(funcionariosService.query).toHaveBeenCalled();
      expect(funcionariosService.addFuncionariosToCollectionIfMissing).toHaveBeenCalledWith(
        funcionariosCollection,
        ...additionalFuncionarios.map(expect.objectContaining)
      );
      expect(comp.funcionariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Alumnos query and add missing value', () => {
      const registroClases: IRegistroClases = { id: 456 };
      const alumnos: IAlumnos = { id: 33826 };
      registroClases.alumnos = alumnos;

      const alumnosCollection: IAlumnos[] = [{ id: 99964 }];
      jest.spyOn(alumnosService, 'query').mockReturnValue(of(new HttpResponse({ body: alumnosCollection })));
      const additionalAlumnos = [alumnos];
      const expectedCollection: IAlumnos[] = [...additionalAlumnos, ...alumnosCollection];
      jest.spyOn(alumnosService, 'addAlumnosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ registroClases });
      comp.ngOnInit();

      expect(alumnosService.query).toHaveBeenCalled();
      expect(alumnosService.addAlumnosToCollectionIfMissing).toHaveBeenCalledWith(
        alumnosCollection,
        ...additionalAlumnos.map(expect.objectContaining)
      );
      expect(comp.alumnosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const registroClases: IRegistroClases = { id: 456 };
      const temas: ITemas = { id: 99655 };
      registroClases.temas = temas;
      const funcionario: IFuncionarios = { id: 14486 };
      registroClases.funcionario = funcionario;
      const alumnos: IAlumnos = { id: 82089 };
      registroClases.alumnos = alumnos;

      activatedRoute.data = of({ registroClases });
      comp.ngOnInit();

      expect(comp.temasSharedCollection).toContain(temas);
      expect(comp.funcionariosSharedCollection).toContain(funcionario);
      expect(comp.alumnosSharedCollection).toContain(alumnos);
      expect(comp.registroClases).toEqual(registroClases);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRegistroClases>>();
      const registroClases = { id: 123 };
      jest.spyOn(registroClasesFormService, 'getRegistroClases').mockReturnValue(registroClases);
      jest.spyOn(registroClasesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registroClases });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: registroClases }));
      saveSubject.complete();

      // THEN
      expect(registroClasesFormService.getRegistroClases).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(registroClasesService.update).toHaveBeenCalledWith(expect.objectContaining(registroClases));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRegistroClases>>();
      const registroClases = { id: 123 };
      jest.spyOn(registroClasesFormService, 'getRegistroClases').mockReturnValue({ id: null });
      jest.spyOn(registroClasesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registroClases: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: registroClases }));
      saveSubject.complete();

      // THEN
      expect(registroClasesFormService.getRegistroClases).toHaveBeenCalled();
      expect(registroClasesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRegistroClases>>();
      const registroClases = { id: 123 };
      jest.spyOn(registroClasesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registroClases });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(registroClasesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTemas', () => {
      it('Should forward to temasService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(temasService, 'compareTemas');
        comp.compareTemas(entity, entity2);
        expect(temasService.compareTemas).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFuncionarios', () => {
      it('Should forward to funcionariosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(funcionariosService, 'compareFuncionarios');
        comp.compareFuncionarios(entity, entity2);
        expect(funcionariosService.compareFuncionarios).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAlumnos', () => {
      it('Should forward to alumnosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(alumnosService, 'compareAlumnos');
        comp.compareAlumnos(entity, entity2);
        expect(alumnosService.compareAlumnos).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
