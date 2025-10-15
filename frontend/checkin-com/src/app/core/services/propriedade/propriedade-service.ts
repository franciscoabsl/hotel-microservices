import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class PropriedadeService {
  private url = environment.apiUrl + 'propriedades';

  constructor(private http: HttpClient) {}

  getDados(): Observable<any> {
    return this.http.get<any>(this.url);
  }

  cadastrar(propriedade: any): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders({
          'Content-Type': 'application/json',
          ...(token ? { Authorization: `Bearer ${token}` } : {})
        });
    return this.http.post<any>(this.url, propriedade, { headers });
  }

  atualizar(propriedade: any): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders({
          'Content-Type': 'application/json',
          ...(token ? { Authorization: `Bearer ${token}` } : {})
        });
    return this.http.put<any>(this.url + `/${propriedade.id}`, propriedade, { headers });
  }

  excluir(id: string): Observable<any> {
    return this.http.delete<any>(this.url + `/${id}`);
  }

  obterPorId(id: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.getToken()}`, // exemplo se usar token
      'Accept': 'application/json'
    });

    return this.http.get(`${this.url}/${id}`, { headers, withCredentials: false });
  }

  private getToken(): string {
    // pega token do localStorage / serviço de auth
    return localStorage.getItem('token') ?? '';
  }
}
