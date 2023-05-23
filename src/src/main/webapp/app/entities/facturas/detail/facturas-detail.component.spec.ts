import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FacturasDetailComponent } from './facturas-detail.component';

describe('Facturas Management Detail Component', () => {
  let comp: FacturasDetailComponent;
  let fixture: ComponentFixture<FacturasDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FacturasDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ facturas: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FacturasDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FacturasDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load facturas on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.facturas).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
