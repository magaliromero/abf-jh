import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FacturaDetalleDetailComponent } from './factura-detalle-detail.component';

describe('FacturaDetalle Management Detail Component', () => {
  let comp: FacturaDetalleDetailComponent;
  let fixture: ComponentFixture<FacturaDetalleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FacturaDetalleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ facturaDetalle: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FacturaDetalleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FacturaDetalleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load facturaDetalle on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.facturaDetalle).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
