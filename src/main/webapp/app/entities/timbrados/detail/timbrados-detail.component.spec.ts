import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TimbradosDetailComponent } from './timbrados-detail.component';

describe('Timbrados Management Detail Component', () => {
  let comp: TimbradosDetailComponent;
  let fixture: ComponentFixture<TimbradosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TimbradosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ timbrados: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TimbradosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TimbradosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load timbrados on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.timbrados).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
