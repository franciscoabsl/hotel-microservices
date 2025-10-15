import { Component } from '@angular/core';
import { FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthUserApi } from 'src/app/core/services/auth-user/auth-user-api';

@Component({
  selector: 'app-cadastrar-usuario',
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './cadastrar-usuario.html',
  styleUrl: './cadastrar-usuario.scss'
})
export class CadastrarUsuario {
  formulario!: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authUserService: AuthUserApi,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.formulario = this.fb.group({
      nome: ['', [Validators.required]],
      cpf: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      perfil: [''],
    });
  }

  salvar(){
    if (this.formulario.valid) {
      this.authUserService.register(this.formulario.value).subscribe({
        next: (res: any) => {
          this.router.navigate(['login']);
        },
        error: (err: any) => console.error('Erro ao registrar na plataforma', err)
      });
    } else {
      console.warn('Formulário inválido!');
    }
  }

  cancelar() {
    this.router.navigate(['login']);
  }
}
