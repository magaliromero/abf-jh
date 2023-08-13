import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PuntoDeExpedicionDetailComponent } from './punto-de-expedicion-detail.component';

describe('PuntoDeExpedicion Management Detail Component', () => {
  let comp: PuntoDeExpedicionDetailComponent;
  let fixture: ComponentFixture<PuntoDeExpedicionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PuntoDeExpedicionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ puntoDeExpedicion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PuntoDeExpedicionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PuntoDeExpedicionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load puntoDeExpedicion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.puntoDeExpedicion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
