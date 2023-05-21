import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductosDetailComponent } from './productos-detail.component';

describe('Productos Management Detail Component', () => {
  let comp: ProductosDetailComponent;
  let fixture: ComponentFixture<ProductosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProductosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
