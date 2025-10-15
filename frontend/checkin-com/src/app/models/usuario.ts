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
