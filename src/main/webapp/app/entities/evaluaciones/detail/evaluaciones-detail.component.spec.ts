import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EvaluacionesDetailComponent } from './evaluaciones-detail.component';

describe('Evaluaciones Management Detail Component', () => {
  let comp: EvaluacionesDetailComponent;
  let fixture: ComponentFixture<EvaluacionesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EvaluacionesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ evaluaciones: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EvaluacionesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EvaluacionesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load evaluaciones on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.evaluaciones).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
