import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PagosDetailComponent } from './pagos-detail.component';

describe('Pagos Management Detail Component', () => {
  let comp: PagosDetailComponent;
  let fixture: ComponentFixture<PagosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PagosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pagos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PagosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PagosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pagos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pagos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
