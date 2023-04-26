import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FichaPartidasTorneosFormService } from './ficha-partidas-torneos-form.service';
import { FichaPartidasTorneosService } from '../service/ficha-partidas-torneos.service';
import { IFichaPartidasTorneos } from '../ficha-partidas-torneos.model';
import { ITorneos } from 'app/entities/torneos/torneos.model';
import { TorneosService } from 'app/entities/torneos/service/torneos.service';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';

import { FichaPartidasTorneosUpdateComponent } from './ficha-partidas-torneos-update.component';

describe('FichaPartidasTorneos Management Update Component', () => {
  let comp: FichaPartidasTorneosUpdateComponent;
  let fixture: ComponentFixture<FichaPartidasTorneosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fichaPartidasTorneosFormService: FichaPartidasTorneosFormService;
  let fichaPartidasTorneosService: FichaPartidasTorneosService;
  let torneosService: TorneosService;
  let alumnosService: AlumnosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FichaPartidasTorneosUpdateComponent],
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
      .overrideTemplate(FichaPartidasTorneosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FichaPartidasTorneosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fichaPartidasTorneosFormService = TestBed.inject(FichaPartidasTorneosFormService);
    fichaPartidasTorneosService = TestBed.inject(FichaPartidasTorneosService);
    torneosService = TestBed.inject(TorneosService);
    alumnosService = TestBed.inject(AlumnosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Torneos query and add missing value', () => {
      const fichaPartidasTorneos: IFichaPartidasTorneos = { id: 456 };
      const torneos: ITorneos = { id: 62546 };
      fichaPartidasTorneos.torneos = torneos;

      const torneosCollection: ITorneos[] = [{ id: 6114 }];
      jest.spyOn(torneosService, 'query').mockReturnValue(of(new HttpResponse({ body: torneosCollection })));
      const additionalTorneos = [torneos];
      const expectedCollection: ITorneos[] = [...additionalTorneos, ...torneosCollection];
      jest.spyOn(torneosService, 'addTorneosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fichaPartidasTorneos });
      comp.ngOnInit();

      expect(torneosService.query).toHaveBeenCalled();
      expect(torneosService.addTorneosToCollectionIfMissing).toHaveBeenCalledWith(
        torneosCollection,
        ...additionalTorneos.map(expect.objectContaining)
      );
      expect(comp.torneosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Alumnos query and add missing value', () => {
      const fichaPartidasTorneos: IFichaPartidasTorneos = { id: 456 };
      const alumnos: IAlumnos = { id: 84042 };
      fichaPartidasTorneos.alumnos = alumnos;

      const alumnosCollection: IAlumnos[] = [{ id: 4197 }];
      jest.spyOn(alumnosService, 'query').mockReturnValue(of(new HttpResponse({ body: alumnosCollection })));
      const additionalAlumnos = [alumnos];
      const expectedCollection: IAlumnos[] = [...additionalAlumnos, ...alumnosCollection];
      jest.spyOn(alumnosService, 'addAlumnosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fichaPartidasTorneos });
      comp.ngOnInit();

      expect(alumnosService.query).toHaveBeenCalled();
      expect(alumnosService.addAlumnosToCollectionIfMissing).toHaveBeenCalledWith(
        alumnosCollection,
        ...additionalAlumnos.map(expect.objectContaining)
      );
      expect(comp.alumnosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const fichaPartidasTorneos: IFichaPartidasTorneos = { id: 456 };
      const torneos: ITorneos = { id: 76716 };
      fichaPartidasTorneos.torneos = torneos;
      const alumnos: IAlumnos = { id: 36623 };
      fichaPartidasTorneos.alumnos = alumnos;

      activatedRoute.data = of({ fichaPartidasTorneos });
      comp.ngOnInit();

      expect(comp.torneosSharedCollection).toContain(torneos);
      expect(comp.alumnosSharedCollection).toContain(alumnos);
      expect(comp.fichaPartidasTorneos).toEqual(fichaPartidasTorneos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFichaPartidasTorneos>>();
      const fichaPartidasTorneos = { id: 123 };
      jest.spyOn(fichaPartidasTorneosFormService, 'getFichaPartidasTorneos').mockReturnValue(fichaPartidasTorneos);
      jest.spyOn(fichaPartidasTorneosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fichaPartidasTorneos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fichaPartidasTorneos }));
      saveSubject.complete();

      // THEN
      expect(fichaPartidasTorneosFormService.getFichaPartidasTorneos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fichaPartidasTorneosService.update).toHaveBeenCalledWith(expect.objectContaining(fichaPartidasTorneos));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFichaPartidasTorneos>>();
      const fichaPartidasTorneos = { id: 123 };
      jest.spyOn(fichaPartidasTorneosFormService, 'getFichaPartidasTorneos').mockReturnValue({ id: null });
      jest.spyOn(fichaPartidasTorneosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fichaPartidasTorneos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fichaPartidasTorneos }));
      saveSubject.complete();

      // THEN
      expect(fichaPartidasTorneosFormService.getFichaPartidasTorneos).toHaveBeenCalled();
      expect(fichaPartidasTorneosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFichaPartidasTorneos>>();
      const fichaPartidasTorneos = { id: 123 };
      jest.spyOn(fichaPartidasTorneosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fichaPartidasTorneos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fichaPartidasTorneosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTorneos', () => {
      it('Should forward to torneosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(torneosService, 'compareTorneos');
        comp.compareTorneos(entity, entity2);
        expect(torneosService.compareTorneos).toHaveBeenCalledWith(entity, entity2);
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
