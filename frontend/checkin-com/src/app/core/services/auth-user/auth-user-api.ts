import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, tap, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

export interface LoginPayload {
  email: string;
  senha: string;
}

export interface UsuarioPayload {
  nome: string;
  email: string;
  cpf: string;
  senha: string;
  perfil: string;
}

export interface UsuarioResponse {
  id: string;
  nome: string;
  email: string;
  cpf: string;
  senha: string;
  perfil: string;
}

export interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthUserApi {
  private url = environment.apiUrl;
  private tokenKey = 'auth_token';

  constructor(private http: HttpClient) {}

  login(payload: LoginPayload): Observable<void> {
    return this.http.post<LoginResponse>(this.url+'auth/login', payload).pipe(
      tap((res) => {
        if (!res || !res.token) {
          throw new Error('Resposta de login inválida: token ausente.');
        }
      }),
      map((res) => res.token),
      tap((token) => this.saveToken(token)),
      map(() => void 0),
      catchError(this.handleError)
    );
  }

  register(payload: UsuarioPayload): Observable<void> {
    payload.perfil = 'ADMIN';
    return this.http.post<UsuarioResponse>(this.url+'auth/register', payload).pipe(
      tap((res) => {
        payload.email = res.email;
        payload.senha = res.senha;
        this.login(payload);
      }),
      map(() => void 0),
      catchError(this.handleError)
        );
  }

  /** Salva token (localStorage) */
  private saveToken(token: string) {
    try {
      localStorage.setItem(this.tokenKey, token);
    } catch (err) {
      console.error('Erro ao salvar token no localStorage', err);
    }
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }



  /** Erro padrão para requisições de autenticação */
  private handleError(error: HttpErrorResponse) {
    let message = 'Erro desconhecido.';

    if (error.error && typeof error.error === 'string') {
      message = error.error;
    } else if (error.error && error.error.message) {
      message = error.error.message;
    } else if (error.status === 0) {
      message = 'Não foi possível conectar ao servidor (verifique se o backend está rodando).';
    } else if (error.status >= 400 && error.status < 500) {
      message = 'Credenciais inválidas ou requisição inválida.';
    } else if (error.status >= 500) {
      message = 'Erro no servidor. Tente novamente mais tarde.';
    }

    return throwError(() => new Error(message));
  }

  getUserFromToken(): any | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload;
    } catch (e) {
      console.error('Erro ao decodificar token', e);
      return null;
    }
  }

  getUsername(): string {
    const user = this.getUserFromToken();
    return user?.name || user?.username || '';
  }
}
