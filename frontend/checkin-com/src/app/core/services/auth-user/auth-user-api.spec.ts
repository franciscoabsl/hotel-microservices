import { TestBed } from '@angular/core/testing';

import { AuthUserApi } from './auth-user-api';

describe('AuthUserApi', () => {
  let service: AuthUserApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthUserApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
