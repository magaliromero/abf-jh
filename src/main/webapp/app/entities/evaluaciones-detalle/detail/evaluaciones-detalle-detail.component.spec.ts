import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EvaluacionesDetalleDetailComponent } from './evaluaciones-detalle-detail.component';

describe('EvaluacionesDetalle Management Detail Component', () => {
  let comp: EvaluacionesDetalleDetailComponent;
  let fixture: ComponentFixture<EvaluacionesDetalleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EvaluacionesDetalleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ evaluacionesDetalle: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EvaluacionesDetalleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EvaluacionesDetalleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load evaluacionesDetalle on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.evaluacionesDetalle).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
