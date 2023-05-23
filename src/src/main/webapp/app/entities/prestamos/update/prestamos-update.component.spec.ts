import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PrestamosFormService } from './prestamos-form.service';
import { PrestamosService } from '../service/prestamos.service';
import { IPrestamos } from '../prestamos.model';
import { IMateriales } from 'app/entities/materiales/materiales.model';
import { MaterialesService } from 'app/entities/materiales/service/materiales.service';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';

import { PrestamosUpdateComponent } from './prestamos-update.component';

describe('Prestamos Management Update Component', () => {
  let comp: PrestamosUpdateComponent;
  let fixture: ComponentFixture<PrestamosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prestamosFormService: PrestamosFormService;
  let prestamosService: PrestamosService;
  let materialesService: MaterialesService;
  let alumnosService: AlumnosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PrestamosUpdateComponent],
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
      .overrideTemplate(PrestamosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrestamosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prestamosFormService = TestBed.inject(PrestamosFormService);
    prestamosService = TestBed.inject(PrestamosService);
    materialesService = TestBed.inject(MaterialesService);
    alumnosService = TestBed.inject(AlumnosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Materiales query and add missing value', () => {
      const prestamos: IPrestamos = { id: 456 };
      const materiales: IMateriales = { id: 38783 };
      prestamos.materiales = materiales;

      const materialesCollection: IMateriales[] = [{ id: 40409 }];
      jest.spyOn(materialesService, 'query').mockReturnValue(of(new HttpResponse({ body: materialesCollection })));
      const additionalMateriales = [materiales];
      const expectedCollection: IMateriales[] = [...additionalMateriales, ...materialesCollection];
      jest.spyOn(materialesService, 'addMaterialesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prestamos });
      comp.ngOnInit();

      expect(materialesService.query).toHaveBeenCalled();
      expect(materialesService.addMaterialesToCollectionIfMissing).toHaveBeenCalledWith(
        materialesCollection,
        ...additionalMateriales.map(expect.objectContaining)
      );
      expect(comp.materialesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Alumnos query and add missing value', () => {
      const prestamos: IPrestamos = { id: 456 };
      const alumnos: IAlumnos = { id: 73984 };
      prestamos.alumnos = alumnos;

      const alumnosCollection: IAlumnos[] = [{ id: 86220 }];
      jest.spyOn(alumnosService, 'query').mockReturnValue(of(new HttpResponse({ body: alumnosCollection })));
      const additionalAlumnos = [alumnos];
      const expectedCollection: IAlumnos[] = [...additionalAlumnos, ...alumnosCollection];
      jest.spyOn(alumnosService, 'addAlumnosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prestamos });
      comp.ngOnInit();

      expect(alumnosService.query).toHaveBeenCalled();
      expect(alumnosService.addAlumnosToCollectionIfMissing).toHaveBeenCalledWith(
        alumnosCollection,
        ...additionalAlumnos.map(expect.objectContaining)
      );
      expect(comp.alumnosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prestamos: IPrestamos = { id: 456 };
      const materiales: IMateriales = { id: 77994 };
      prestamos.materiales = materiales;
      const alumnos: IAlumnos = { id: 81930 };
      prestamos.alumnos = alumnos;

      activatedRoute.data = of({ prestamos });
      comp.ngOnInit();

      expect(comp.materialesSharedCollection).toContain(materiales);
      expect(comp.alumnosSharedCollection).toContain(alumnos);
      expect(comp.prestamos).toEqual(prestamos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrestamos>>();
      const prestamos = { id: 123 };
      jest.spyOn(prestamosFormService, 'getPrestamos').mockReturnValue(prestamos);
      jest.spyOn(prestamosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestamos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prestamos }));
      saveSubject.complete();

      // THEN
      expect(prestamosFormService.getPrestamos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(prestamosService.update).toHaveBeenCalledWith(expect.objectContaining(prestamos));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrestamos>>();
      const prestamos = { id: 123 };
      jest.spyOn(prestamosFormService, 'getPrestamos').mockReturnValue({ id: null });
      jest.spyOn(prestamosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestamos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prestamos }));
      saveSubject.complete();

      // THEN
      expect(prestamosFormService.getPrestamos).toHaveBeenCalled();
      expect(prestamosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrestamos>>();
      const prestamos = { id: 123 };
      jest.spyOn(prestamosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestamos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prestamosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMateriales', () => {
      it('Should forward to materialesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(materialesService, 'compareMateriales');
        comp.compareMateriales(entity, entity2);
        expect(materialesService.compareMateriales).toHaveBeenCalledWith(entity, entity2);
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
