import { TestBed } from '@angular/core/testing';

import { PropriedadeService } from './propriedade-service';

describe('PropriedadeService', () => {
  let service: PropriedadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PropriedadeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
