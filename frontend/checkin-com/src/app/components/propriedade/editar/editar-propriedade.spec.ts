import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarPropriedade } from './editar-propriedade';

describe('EditarPropriedade', () => {
  let component: EditarPropriedade;
  let fixture: ComponentFixture<EditarPropriedade>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditarPropriedade]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditarPropriedade);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
