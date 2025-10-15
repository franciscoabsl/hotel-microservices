import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthUserApi } from 'src/app/core/services/auth-user/auth-user-api';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterModule, CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login implements OnInit {
  formulario!: FormGroup;
  loading = false;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authUserApi: AuthUserApi,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
      this.formulario = this.fb.group({
        email: ['', [Validators.required, Validators.email]],
        senha: ['', [Validators.required, Validators.minLength(6)]],
      });
  }

  onSubmit(): void {
    this.loading = true;
    this.errorMessage = null;

    this.authUserApi.login(this.formulario.value).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['home']);
      },
      error: (err: unknown) => {
        this.loading = false;
        if (err instanceof Error) {
          this.errorMessage = err.message;
        } else {
          this.errorMessage = 'Erro ao autenticar. Tente novamente.';
        }
      },
    });
  }

}
