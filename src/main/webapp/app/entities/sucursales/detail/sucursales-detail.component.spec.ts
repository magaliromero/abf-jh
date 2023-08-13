import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SucursalesDetailComponent } from './sucursales-detail.component';

describe('Sucursales Management Detail Component', () => {
  let comp: SucursalesDetailComponent;
  let fixture: ComponentFixture<SucursalesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SucursalesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sucursales: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SucursalesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SucursalesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sucursales on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sucursales).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
