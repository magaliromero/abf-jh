import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AlumnosFormService } from './alumnos-form.service';
import { AlumnosService } from '../service/alumnos.service';
import { IAlumnos } from '../alumnos.model';
import { ITiposDocumentos } from 'app/entities/tipos-documentos/tipos-documentos.model';
import { TiposDocumentosService } from 'app/entities/tipos-documentos/service/tipos-documentos.service';

import { AlumnosUpdateComponent } from './alumnos-update.component';

describe('Alumnos Management Update Component', () => {
  let comp: AlumnosUpdateComponent;
  let fixture: ComponentFixture<AlumnosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alumnosFormService: AlumnosFormService;
  let alumnosService: AlumnosService;
  let tiposDocumentosService: TiposDocumentosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AlumnosUpdateComponent],
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
      .overrideTemplate(AlumnosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlumnosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alumnosFormService = TestBed.inject(AlumnosFormService);
    alumnosService = TestBed.inject(AlumnosService);
    tiposDocumentosService = TestBed.inject(TiposDocumentosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TiposDocumentos query and add missing value', () => {
      const alumnos: IAlumnos = { id: 456 };
      const tipoDocumentos: ITiposDocumentos = { id: 87820 };
      alumnos.tipoDocumentos = tipoDocumentos;

      const tiposDocumentosCollection: ITiposDocumentos[] = [{ id: 52994 }];
      jest.spyOn(tiposDocumentosService, 'query').mockReturnValue(of(new HttpResponse({ body: tiposDocumentosCollection })));
      const additionalTiposDocumentos = [tipoDocumentos];
      const expectedCollection: ITiposDocumentos[] = [...additionalTiposDocumentos, ...tiposDocumentosCollection];
      jest.spyOn(tiposDocumentosService, 'addTiposDocumentosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alumnos });
      comp.ngOnInit();

      expect(tiposDocumentosService.query).toHaveBeenCalled();
      expect(tiposDocumentosService.addTiposDocumentosToCollectionIfMissing).toHaveBeenCalledWith(
        tiposDocumentosCollection,
        ...additionalTiposDocumentos.map(expect.objectContaining)
      );
      expect(comp.tiposDocumentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const alumnos: IAlumnos = { id: 456 };
      const tipoDocumentos: ITiposDocumentos = { id: 59854 };
      alumnos.tipoDocumentos = tipoDocumentos;

      activatedRoute.data = of({ alumnos });
      comp.ngOnInit();

      expect(comp.tiposDocumentosSharedCollection).toContain(tipoDocumentos);
      expect(comp.alumnos).toEqual(alumnos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlumnos>>();
      const alumnos = { id: 123 };
      jest.spyOn(alumnosFormService, 'getAlumnos').mockReturnValue(alumnos);
      jest.spyOn(alumnosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alumnos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alumnos }));
      saveSubject.complete();

      // THEN
      expect(alumnosFormService.getAlumnos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alumnosService.update).toHaveBeenCalledWith(expect.objectContaining(alumnos));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlumnos>>();
      const alumnos = { id: 123 };
      jest.spyOn(alumnosFormService, 'getAlumnos').mockReturnValue({ id: null });
      jest.spyOn(alumnosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alumnos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alumnos }));
      saveSubject.complete();

      // THEN
      expect(alumnosFormService.getAlumnos).toHaveBeenCalled();
      expect(alumnosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlumnos>>();
      const alumnos = { id: 123 };
      jest.spyOn(alumnosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alumnos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alumnosService.update).toHaveBeenCalled();
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
