import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CobrosDetailComponent } from './cobros-detail.component';

describe('Cobros Management Detail Component', () => {
  let comp: CobrosDetailComponent;
  let fixture: ComponentFixture<CobrosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CobrosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cobros: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CobrosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CobrosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cobros on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cobros).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
