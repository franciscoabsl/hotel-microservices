import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Propriedade } from './propriedade';

describe('Propriedade', () => {
  let component: Propriedade;
  let fixture: ComponentFixture<Propriedade>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Propriedade]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Propriedade);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
